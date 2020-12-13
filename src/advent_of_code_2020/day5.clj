(ns advent-of-code-2020.day5
  (:require [clojure.pprint :as pp]
            [advent-of-code-2020.util :as util]))

(defn upper-range [range]
  (let [width (- (second range) (first range))]
    [(inc (+ (first range) (quot width 2))) (second range)]))


(defn lower-range [range]
  (let [width (- (second range) (first range))]
    [(first range) (+ (first range) (quot width 2))]))


(defn parse-seats [path state]
  (if (empty? path) {:index (state :index)
                     :range (state :range)
                     :seat (max (state :seat) (+ (* 8 (first (state :range)))
                                                 (first (state :seat-range))))
                     :seat-range (state :seat-range)}
      (let [index (state :index)
            [min-row max-row] (state :range)
            [min-seat max-seat] (state :seat-range)
            seat (state :seat)]
        (cond
          (= (first path) \F) (parse-seats (rest path) {:index (inc index)
                                                        :range (lower-range (state :range))
                                                        :seat (state :seat)
                                                        :seat-range (state :seat-range)})
          (= (first path) \B) (parse-seats (rest path) {:index (inc index)
                                                        :range (upper-range (state :range))
                                                        :seat (state :seat)
                                                        :seat-range (state :seat-range)})
          (= (first path) \L) (parse-seats (rest path) {:index (inc index)
                                                        :range [min-row max-row]
                                                        :seat (state :seat)
                                                        :seat-range (lower-range (state :seat-range))})
          (= (first path) \R) (parse-seats (rest path) {:index (inc index)
                                                        :range [min-row max-row]
                                                        :seat (state :seat)
                                                        :seat-range (upper-range (state :seat-range))})))))

(defn load-seats [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(defn find-max-seat [file]
  (->> file
      load-seats
      (map #(parse-seats % {:index 0 :range [0 127] :seat 0 :seat-range [0 7]}))
      (map :seat)))


(defn -main [& args]
  (let [all-seats (find-max-seat "resources/input5.txt")
        min-seat (reduce min (sort all-seats))
        max-seat (reduce max (sort all-seats))]
    (println (reduce max all-seats))
    ;; find the missing seat by diff'ing the list of possible seats
    ;; with the actual ones we found.
    (println (util/diff (range min-seat (inc max-seat)) all-seats))))
