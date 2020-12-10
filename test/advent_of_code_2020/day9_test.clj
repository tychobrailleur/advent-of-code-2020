(ns advent-of-code-2020.day9-test
  (:require [advent-of-code-2020.day9 :as sut]
            [clojure.test :as t]))

(t/deftest test-sublist
  (t/testing "Returns a sublist of a list"
    (t/is (= (sut/sublist (list 1 2 3 4 5 6) 0 3) (list 1 2 3)))))
