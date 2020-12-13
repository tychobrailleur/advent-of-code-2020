(ns advent-of-code-2020.day1
  (:require [clojure.math.combinatorics :as combo]
            [advent-of-code-2020.util :as util]))

(defn load-numbers [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (map #(Integer/parseInt %) (line-seq rdr)))))

(defn find-items [file i]
  (let [numbers (load-numbers file)
        combinations (combo/combinations numbers i)]
    (for [l (filter #(= 2020 (util/add-all %)) combinations)]
      (util/multiply-all l))))

(defn -main [& args]
  (println (find-items "resources/input1.txt" 2))
  (println (find-items "resources/input1.txt" 3)))
