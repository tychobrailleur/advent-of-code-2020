(ns advent-of-code-2020.day10
  (:require [advent-of-code-2020.util :as util]))


(defn load-numbers [file]
  (sort (with-open [rdr (clojure.java.io/reader file)]
          (doall (map #(Long/parseLong %) (line-seq rdr))))))


(defn walk-adapters [adapters steps state]
  (if (empty? adapters)
    state
    (let [current (state :index)]
      (loop [s (first steps)
             r (rest steps)]
        (if (some #(= % (+ s current)) adapters)
          (walk-adapters (remove #(= (+ s current) %) adapters)
                         steps
                         {:index (+ s current)
                          :deltas (conj (state :deltas) s)})
          (recur (first r) (rest r)))))))

(def count-all-adapters
  (memoize
   (fn
     ([x] 1)
     ([x y & rest]
      (if (> (+ x y) 3)
        (apply count-all-adapters y rest)
        (+ (apply count-all-adapters y rest)
           (apply count-all-adapters (+ x y) rest)))))))

(defn add-extrema [list]
  (sort (conj list 0 (+ 3 (apply max list)))))

(defn deltas [list]
  (->> list
       (partition 2 1)
       (map (fn [n] (- (second n) (first n))))))

(defn try-all-adapters [file]
  (->> file
       load-numbers
       add-extrema
       deltas
       (apply count-all-adapters)))

(defn try-adapters [file]
  (-> file
      load-numbers
      (walk-adapters [1 2 3] {:index 0 :deltas [3]})
      :deltas
      frequencies
      vals
      util/multiply-all))


(defn -main [& args]
  (println (try-adapters "resources/input10.txt"))
  (println (try-all-adapters "resources/input10.txt")))
