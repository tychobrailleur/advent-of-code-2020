(ns advent-of-code-2020.day2
  (:require [clojure.string :as str]))

(defn string->char [str]
  (first (char-array str)))

(defn char-at-index [str char index]
  (if (= char (nth str (- index 1))) 1 0))

(defn min-max-freq-policy [num1 num2 letter pass]
  (let [num (get (frequencies pass) letter 0)]
    (and (<= num1 num) (>= num2 num))))

(defn position-policy [num1 num2 letter pass]
  (= 1 (+ (char-at-index pass letter num1)
          (char-at-index pass letter num2))))

(defn validate-policy [elt policy-fn]
  (let [[policy pass] elt
        [_ num1 num2 letter] (re-matches #"([0-9]+)-([0-9]+) ([a-z])" policy)]
    (apply policy-fn (list (Integer/parseInt num1)
                           (Integer/parseInt num2)
                           (string->char letter)
                           pass))))

(defn parse-line [line]
  (let [[_ policy pass] (re-matches #"([^:]+): (.*)" line)]
    (list policy pass)))

(defn load-passwords [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(defn count-valid [file policy-fn]
  (->> (load-passwords file)
       (map #(parse-line %))
       (filter #(validate-policy % policy-fn))
       count
       println))

(defn -main [& args]
  (count-valid "resources/input2.txt" min-max-freq-policy)
  (count-valid "resources/input2.txt" position-policy))
