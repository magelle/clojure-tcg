(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg

  (testing "test a test !"
    (is (= 1 1)))

  (testing "begin a game"
    (let [a 1
          b 1]
      (is (= a b)))))
