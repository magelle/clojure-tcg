(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg

  (testing "test a test !"
    (is (= 1 1)))

  (testing "apply"
    (is (apply []) {}))

  (testing "begin a game"
    (is (=
         (receive [] (class {:cmd "StartGame" :g 1}))
         [(class {:evt "GameStarted"})]))))
