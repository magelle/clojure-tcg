(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg
  (testing "begin a game"
    (is (= 1 1))))
