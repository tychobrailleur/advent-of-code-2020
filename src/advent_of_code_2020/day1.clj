(ns advent-of-code-2020.day1
    (:require [clojure.math.combinatorics :as combo]))

(defn load-numbers [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (map #(Integer/parseInt %) (line-seq rdr)))))

(defn add-all [list]
  (reduce + list))

(defn multiply-all [list]
  (reduce * list))

(defn find-items [file i]
  (let [numbers (load-numbers file)
        combinations (combo/combinations numbers i)]
    (for [l (filter #(= 2020 (add-all %)) combinations)]
      (multiply-all l))))

(defn -main [& args]
  (println (find-items "resources/input1.txt" 2))
  (println (find-items "resources/input1.txt" 3)))
