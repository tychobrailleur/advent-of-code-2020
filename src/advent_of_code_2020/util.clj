(ns advent-of-code-2020.util)

(defn add-all
  "Adds all the elements in the list `list`."
  [list]
  (reduce + list))

(defn multiply-all
  "Multiplies all the elements in the list `list`."
  [list]
  (reduce * list))

(defn sublist
  "Returns a sublist of list `list` starting at index `start` and ending at index `end`."
  [list start end]
  (subvec (into [] list) start end))


;; Cf. https://stackoverflow.com/a/23200627/289466
(defn diff
  "Diffs two lists."
  [s1 s2]
  (mapcat
    (fn [[x n]] (repeat n x))
    (apply merge-with - (map frequencies [s1 s2]))))


(defn string->char
  "Converts a string to char.
  If the string is more than one char long, it converts the first character."
  [str]
  (first (char-array str)))

(defn replace-char-at [s pos char]
  (let [sb (StringBuilder. s)]
    (str (.replace sb pos (inc pos) char))))
