(ns advent-of-code-2020.day11
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [advent-of-code-2020.util :as util]))

(def medium-grid ["L.LL.LL.LL"
                  "LLLLLLL.LL"
                  "L.L.L..L.."
                  "LLLL.LL.LL"
                  "L.LL.LL.LL"
                  "L.LLLLL.LL"
                  "..L.L....."
                  "LLLLLLLLLL"
                  "L.LLLLLL.L"
                  "L.LLLLL.LL"])

(defn count-char [str c]
  (count (filter #(= c %) str)))

(defn coord-state
  "Returns the state of the seat at coordinates `seat` in map `m`."
  [m seat]
  (let [[i j] seat]
    (-> m
        (nth i)
        (nth j))))

(defn cartesian-product [a b]
  (for [i a
        j b]
    [i j]))


(defn neighbours
  "Returns the coordinates of the neighbours of seat `seat` in map `m`."
  [m seat]
  (let [[i j] seat
        adjacents (remove #(= % [0 0]) (cartesian-product [-1 0 1] [-1 0 1]))]
    (->> adjacents
         (map (fn [delta] (let [[di dj] delta]
                            [(+ i di) (+ j dj)])))
         (remove (fn [elt]
                   (let [[i j] elt] (or (or (< i 0) (< j 0))
                                        (or (>= i (count m))
                                            (>= j (count (first m)))))))))))

(defn flip-seat [m seat]
  (let [[x y] seat
        str (nth m x)
        val (nth str y)]
    (assoc m x (util/replace-char-at str y (case val
                                             \L "#"
                                             \# "L"
                                             \. ".")))))

(defn adjacent-occupied [m seat]
  (let [[i j] seat
        n (neighbours m seat)]
    (->> n
         (map #(coord-state m %))
         (filter #(= \# %))
         count)))

(defn count-occupied
  "Counts the number of occupied seats in the whole map."
  [m]
  (count-char (str/join m) \#))

(defn check-position [m seat]
  (let [[i j] seat
        state (coord-state m seat)
        count (adjacent-occupied m seat)]
    (if (and (= \L state) (= 0 count))
      [i j]
      (if (and (= \# state) (>= count 4))
        [i j]
        []))))

(defn apply-flips
  [m flips]
  (loop [mm m
         f flips]
    (if (empty? f)
      mm
      (recur (flip-seat mm (first f))
             (rest f)))))

(defn check-map [m]
  (let [coords (cartesian-product (range (count m))
                                  (range (count (first m))))]
    (loop [mm m]
      (let [f (remove empty? (map #(check-position mm %) coords))]
        (if (= (count f) 0)
          (do
            (pp/pprint mm)
          (count-occupied mm))
          (recur (apply-flips mm f)))))))


(defn load-seats [file]
  (->> (with-open [rdr (clojure.java.io/reader file)]
         (doall (line-seq rdr)))
       (into [])))


(defn run-simulation [file]
  (-> file
      load-seats
      check-map))

(defn -main [& args]
  (println (run-simulation "resources/input11.txt")))
