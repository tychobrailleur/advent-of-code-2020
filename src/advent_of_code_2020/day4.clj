(ns advent-of-code-2020.day4
  (:require [clojure.string :as str]))

(defn parse-details [state details]
  (let [[_ key val] (re-matches #"([^:]+):(.*)" details)]
    (assoc state key val)))

(defn parse-passport [pass]
  (let [details (str/split pass #"\s")]
    (reduce parse-details {} details)))

(defn read-passports [file]
  (let [content (slurp file)]
    (str/split content #"\n\n")))

(defn valid-passport? [pass]
  ;; Very loose rules for passport validity check.
  (and (contains? pass "ecl")
       (contains? pass "pid")
       (contains? pass "eyr")
       (contains? pass "hcl")
       (contains? pass "byr")
       (contains? pass "iyr")
       (contains? pass "hgt")))

(defn validate-year [year min max]
  (and (re-matches #"[0-9]{4}" year)
       (and (<= min (Integer/parseInt year))
            (>= max (Integer/parseInt year)))))

(defn validate-height [height]
  (if (re-matches #"([0-9]+)(cm|in)" height)
    (let [[_ h unit] (re-matches #"([0-9]+)(cm|in)" height)]
      (or (and (= unit "cm")
               (<= 150 (Integer/parseInt h))
               (>= 193 (Integer/parseInt h)))
          (and (= unit "in")
               (<= 59 (Integer/parseInt h))
               (>= 76 (Integer/parseInt h)))))
    false))

(defn validate-hair [hair]
  (re-matches #"#[0-9a-f]{6}" hair))

(defn validate-eyes [eyes]
  (.contains '("amb" "blu" "brn" "gry" "grn" "hzl" "oth") eyes))

(defn validate-pid [pid]
  (re-matches #"[0-9]{9}" pid))

(defn valid-strict-passport? [pass]
  (and (valid-passport? pass)
       (validate-year (pass "byr") 1920 2002)
       (validate-year (pass "iyr") 2010 2020)
       (validate-year (pass "eyr") 2020 2030)
       (validate-hair (pass "hcl"))
       (validate-eyes (pass "ecl"))
       (validate-height (pass "hgt"))
       (validate-pid (pass "pid"))))

(defn validate-file [file validate-fn]
  (->> file
       read-passports
       (map parse-passport)
       (filter #(apply validate-fn (list %)))
       count))

(defn -main [& args]
  (println (validate-file "resources/input4.txt" valid-passport?))
  (println (validate-file "resources/input4.txt" valid-strict-passport?)))
