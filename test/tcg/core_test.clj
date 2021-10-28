(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg

  (testing "test a test !"
    (is (= 1 1)))

  (testing "begin a game"
    (is (=
         (receive [] {:cmd "StartGame"
                      :player1Deck []
                      :player2Deck []})
         [{:evt "GameStarted"
           :player1Deck []
           :player2Deck []}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 2"}
          {:evt "PlayerPickedACard" :player "Player 2"}
          {:evt "PlayerPickedACard" :player "Player 2"}]))))
