(ns tcg.core-test
  (:require [clojure.test :refer :all]
            [tcg.core :refer :all]))

(deftest tcg

  (testing "test a test !"
    (is (= 1 1)))

  (testing "begin a game"
    (is (=
         (receive [] {:cmd :StartGame
                      :player1Deck [0 1 2 3 4 5]
                      :player2Deck [5 4 3 2 1 0]})
         [{:evt :GameStarted
           :player1Deck [0 1 2 3 4 5]
           :player2Deck [5 4 3 2 1 0]}
          {:evt :PlayerPickedACard :player :Player1 :cardPicked 0}
          {:evt :PlayerPickedACard :player :Player1 :cardPicked 1}
          {:evt :PlayerPickedACard :player :Player1 :cardPicked 2}
          {:evt :PlayerPickedACard :player :Player2 :cardPicked 5}
          {:evt :PlayerPickedACard :player :Player2 :cardPicked 4}
          {:evt :PlayerPickedACard :player :Player2 :cardPicked 3}
          {:evt :PlayerPickedACard :player :Player2 :cardPicked 2}
          {:evt :PlayerBecameActive :player :Player1}]))))
