(ns advent-of-code-2020.day7
  (:require [clojure.string :as str]))

(defn parse-bag [bag]
  (if (= "no other bags" bag)
    [0 "_"]
    (let [[_ num type] (re-matches #" *([0-9]+) ([a-z ]+?) bags?" bag)]
      [(Integer/parseInt num) type])))

(defn parse-bags [bags]
  (let [list-bags (str/split bags #",")]
    (map parse-bag list-bags)))

(defn parse-rule [line]
  (let [[_ container inside] (re-matches #"([a-z ]+?) bags contain (.*)\." line)]
    {container (parse-bags inside)}))

(defn load-rules [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(declare check-rules)

(defn check-rule [bag rules rule]
  (let [r (second rule)]
    (cond
      (= r "_") false
      (= r bag) true
      :else (check-rules bag rules r))))

(defn check-rules [bag rules rule]
  (let [contained (get rules rule)]
    (some true? (map #(check-rule bag rules %) contained))))

(defn follow-rules
  "For each rule in the ruleset `rules`, follow it to see if we find `bag`."
  [rules bag]
  (count (filter #(check-rules bag rules %) (keys rules))))


(defn count-bags [rules bag]
  (if (= bag "_") 0
      (reduce + (map (fn [elt]
                       (+ (first elt)
                          (* (first elt) (count-bags rules (second elt))))) (get rules bag)))))

(defn count-all-bags [file bag]
  (let [rules (->> file
                   load-rules
                   (map parse-rule)
                   (reduce merge))]
    (count-bags rules bag)))

(defn display-bags [file bag]
  (let [rules (->> file
                   load-rules
                   (map parse-rule)
                   (reduce merge))]
    (follow-rules rules bag)))



(defn -main [& args]
  (println (display-bags "resources/input7.txt" "shiny gold"))
  (println (count-all-bags "resources/input7.txt" "shiny gold")))
