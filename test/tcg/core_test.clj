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
          {:evt :PlayerBecameActive :player :Player1}
          {:evt :ReceivedManaSlot :player :Player1}
          {:evt :ManaSlotsFilled :player :Player1}
          {:evt :PlayerPickedACard :player :Player1 :cardPicked 3}])))

  (testing "Play a card"
    (is (=
         (receive [{:evt :GameStarted
                    :player1Deck [0 1 2 3 4 5]
                    :player2Deck [5 4 3 2 1 0]}
                   {:evt :PlayerPickedACard :player :Player1 :cardPicked 1}
                   {:evt :PlayerBecameActive :player :Player1}
                   {:evt :ReceivedManaSlot :player :Player1}
                   {:evt :ManaSlotsFilled :player :Player1}] {:cmd :PlayCard :card 1})
         [{:evt :CardPlayed :player :Player1 :card 1}
          {:evt :HealthLost :player :Player2}])))

  (testing "Can't play a card when not enough mana"
    (is (=
         (receive [{:evt :GameStarted
                    :player1Deck [0 1 2 3 4 5]
                    :player2Deck [5 4 3 2 1 0]}
                   {:evt :PlayerPickedACard :player :Player1 :cardPicked 2}
                   {:evt :PlayerBecameActive :player :Player1}
                   {:evt :ReceivedManaSlot :player :Player1}
                   {:evt :ManaSlotsFilled :player :Player1}] {:cmd :PlayCard :card 2})
         [{:error "not enough mana"}])))

  (testing "Can end a turn"
    (is (=
         (receive [{:evt :GameStarted
                    :player1Deck [0 1 2 3 4 5]
                    :player2Deck [5 4 3 2 1 0]}] {:cmd :EndTurn})
         [{:evt :PlayerEndedTurn :player :Player1}
          {:evt :PlayerBecameActive :player :Player2}
          {:evt :ReceivedManaSlot :player :Player2}
          {:evt :ManaSlotsFilled :player :Player2}
          {:evt :PlayerPickedACard :player :Player2 :cardPicked 5}])))

  (testing "Player1 can win a game when player deals with 30 damages"
    (is (=
         (receive (concat [{:evt :GameStarted
                            :player1Deck [0 1 2 3 4 5]
                            :player2Deck [5 4 3 2 1 0]}
                           {:evt :PlayerPickedACard :player :Player1 :cardPicked 1}
                           {:evt :PlayerBecameActive :player :Player1}
                           {:evt :ReceivedManaSlot :player :Player1}
                           {:evt :ManaSlotsFilled :player :Player1}] (repeat 29 {:evt :HealthLost :player :Player2})) {:cmd :PlayCard :card 1})
         [{:evt :CardPlayed :player :Player1 :card 1}
          {:evt :HealthLost :player :Player2}
          {:evt :WinGame :player :Player1}])))

  (testing "Player2 can win a game when player deals with 30 damages"
    (is (=
         (receive (concat [{:evt :GameStarted
                            :player1Deck [0 1 2 3 4 5]
                            :player2Deck [5 4 1 2 3 0]}
                           {:evt :PlayerEndedTurn :player :Player1}
                           {:evt :PlayerPickedACard :player :Player2 :cardPicked 1}
                           {:evt :PlayerBecameActive :player :Player2}
                           {:evt :ReceivedManaSlot :player :Player2}
                           {:evt :ManaSlotsFilled :player :Player2}] (repeat 29 {:evt :HealthLost :player :Player1})) {:cmd :PlayCard :card 1})
         [{:evt :CardPlayed :player :Player2 :card 1}
          {:evt :HealthLost :player :Player1}
          {:evt :WinGame :player :Player2}]))))
