(ns advent-of-code-2020.day9
  (:require [clojure.math.combinatorics :as combo]))


(defn load-numbers [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (map #(Long/parseLong %) (line-seq rdr)))))

(defn sublist [list start end]
  (subvec (into [] list) start end))

(defn add-all [list]
  (reduce + list))

(defn find-pairs [list]
  (combo/combinations list 2))

(defn pair-sum? [total list]
  (->> list
       find-pairs
       (map add-all)
       (some #(= total %))))

(defn find-first-non-sum [file preamble]
  (let [numbers (load-numbers file)]
    (loop [index preamble]
      (let [num (nth numbers index)
            list (sublist numbers (- index preamble) index)]
        (if (not (pair-sum? num list))
          num
          (recur (inc index)))))))

(defn sum-consecutive [file glitch]
  (let [numbers (load-numbers file)]
    (for [start (range (dec (count numbers)))
          end (range start (dec (count numbers)))
          :let [s (reduce + (sublist numbers start end))]
          :when (= s glitch)]
      (sublist numbers start end))))

(defn sum-extrema [file glitch]
  (let [[sub _] (sum-consecutive file glitch)]
    (+ (apply min sub) (apply max sub))))

(defn -main [& args]
  (let [glitch (find-first-non-sum "resources/input9.txt" 25)]
    (println glitch)
    (println (sum-extrema "resources/input9.txt" glitch))))
