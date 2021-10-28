(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg

  (testing "test a test !"
    (is (= 1 1)))

  (testing "begin a game"
    (is (=
         (receive [] {:cmd "StartGame"
                      :player1Deck [0, 1, 2, 3, 4, 5]
                      :player2Deck [0, 1, 2, 3, 4, 5]})
         [{:evt "GameStarted"
           :player1Deck [0, 1, 2, 3, 4, 5]
           :player2Deck [0, 1, 2, 3, 4, 5]}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 1"}
          {:evt "PlayerPickedACard" :player "Player 2"}
          {:evt "PlayerPickedACard" :player "Player 2"}
          {:evt "PlayerPickedACard" :player "Player 2"}]))))
