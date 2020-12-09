(ns advent-of-code-2020.day7-test
  (:require [advent-of-code-2020.day7 :refer :all]
            [clojure.test :refer :all]))



(deftest test-follow-rules
  (testing "FIXME, I fail."
    (is (= (follow-rules {"blue" (list [1 "shiny"] )} "shiny") 1))
    (testing "Checking follow"
      (is (= (follow-rules {"blue" (list [1 "red"])
                            "red" (list [1 "green"] [1 "yellow"])
                            "green" (list [0 "_"])
                            "yellow" (list [1 "shiny"])} "shiny") 3)))))
