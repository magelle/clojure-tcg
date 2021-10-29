(ns tcg.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn applyEvts
  [events]
  {:activePlayer :Player1
   :activePlayerState {:mana 1}
   :opponentState {:deck [5]}
   :opponent :Player2})

(defn startGame [state cmd] (let [player1Deck (:player1Deck cmd)
                                  player2Deck (:player2Deck cmd)
                                  [player1Card1 player1Card2 player1Card3, player1Card4] player1Deck
                                  [player2Card1 player2Card2 player2Card3, player2Card4] player2Deck]
                              [{:evt :GameStarted
                                :player1Deck player1Deck
                                :player2Deck player2Deck}
                               {:evt :PlayerPickedACard :player :Player1 :cardPicked player1Card1}
                               {:evt :PlayerPickedACard :player :Player1 :cardPicked player1Card2}
                               {:evt :PlayerPickedACard :player :Player1 :cardPicked player1Card3}
                               {:evt :PlayerPickedACard :player :Player2 :cardPicked player2Card1}
                               {:evt :PlayerPickedACard :player :Player2 :cardPicked player2Card2}
                               {:evt :PlayerPickedACard :player :Player2 :cardPicked player2Card3}
                               {:evt :PlayerPickedACard :player :Player2 :cardPicked player2Card4}
                               {:evt :PlayerBecameActive :player :Player1}
                               {:evt :ReceivedManaSlot :player :Player1}
                               {:evt :ManaSlotsFilled :player :Player1}
                               {:evt :PlayerPickedACard :player :Player1 :cardPicked player1Card4}]))

(defn playCard [state cmd] (let [playedCard (:card cmd)
                                 activePlayer (:activePlayer state)
                                 activePlayerMana (:mana (:activePlayerState state))
                                 opponent (:opponent state)]
                             (if (>= activePlayerMana playedCard)
                               [{:evt :CardPlayed :player activePlayer :card playedCard}
                                {:evt :HealthLost :player opponent}]
                               [])))

(defn endTurn [state cmd] (let [
                                activePlayer (:activePlayer state)
                                opponent (:opponent state)
                                player2Deck (:deck (:opponentState state))
                                [player2Card1] player2Deck
]
                            [{:evt :PlayerEndedTurn :player activePlayer}
                           {:evt :PlayerBecameActive :player opponent}
                           {:evt :ReceivedManaSlot :player opponent}
                           {:evt :ManaSlotsFilled :player opponent}
                           {:evt :PlayerPickedACard :player opponent :cardPicked player2Card1}]))

(defn decide
  [state cmd]
  (cond
    (= (:cmd cmd) :StartGame) (startGame state cmd)
    (= (:cmd cmd) :PlayCard) (playCard state cmd)
    (= (:cmd cmd) :EndTurn) (endTurn state cmd)
    :else []))

(defn receive
  [events command]
  (decide (applyEvts events) command))
