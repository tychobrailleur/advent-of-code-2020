(ns advent-of-code-2020.day6
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn load-forms [file]
  (let [content (slurp file)]
    (str/split content #"\n\n")))

(defn count-forms [forms]
  (let [lines (str/split forms #"\n")]
    (->> lines
         (map #(into #{} (str/split % #"")))
         (reduce set/union #{})
         count)))

(defn count-intersect [forms]
  (let [lines (str/split forms #"\n")]
    (->> lines
         (map #(into #{} (str/split % #"")))
         (reduce set/intersection)
         count)))

(defn count-all-forms [file count-fn]
  (let [forms (load-forms file)]
    (->> forms
         (map #(apply count-fn (list %)))
         (reduce + 0))))

(defn -main [& rest]
  (println (count-all-forms "resources/input6.txt" count-forms))
  (println (count-all-forms "resources/input6.txt" count-intersect)))
