(ns advent-of-code-2020.day9
  (:require [clojure.math.combinatorics :as combo]
            [advent-of-code-2020.util :as util]))


(defn load-numbers [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (map #(Long/parseLong %) (line-seq rdr)))))

(defn find-pairs [list]
  (combo/combinations list 2))

(defn pair-sum? [total list]
  (->> list
       find-pairs
       (map util/add-all)
       (some #(= total %))))

(defn find-first-non-sum [file preamble]
  (let [numbers (load-numbers file)]
    (loop [index preamble]
      (let [num (nth numbers index)
            list (util/sublist numbers (- index preamble) index)]
        (if (not (pair-sum? num list))
          num
          (recur (inc index)))))))

(defn sum-consecutive [file glitch]
  (let [numbers (load-numbers file)]
    (for [start (range (dec (count numbers)))
          end (range start (dec (count numbers)))
          :let [s (reduce + (util/sublist numbers start end))]
          :when (= s glitch)]
      (util/sublist numbers start end))))

(defn sum-extrema [file glitch]
  (let [[sub _] (sum-consecutive file glitch)]
    (+ (apply min sub) (apply max sub))))

(defn -main [& args]
  (let [glitch (find-first-non-sum "resources/input9.txt" 25)]
    (println glitch)
    (println (sum-extrema "resources/input9.txt" glitch))))
