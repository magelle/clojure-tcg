(ns tcg.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn applyEvts
  [events]
  (class {}))

(defn decide
  [state cmd]
  (let [
        player1Deck (:player1Deck cmd)
        player2Deck (:player2Deck cmd)
        [player1Card1 player1Card2 player1Card3] player1Deck
        [player2Card1 player2Card2 player2Card3, player2Card4] player2Deck
  ]
  [{:evt "GameStarted"
    :player1Deck player1Deck
    :player2Deck player2Deck}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked player1Card1}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked player1Card2}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked player1Card3}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked player2Card1}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked player2Card2}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked player2Card3}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked player2Card4}]))

(defn receive
  [events command]
  (decide (applyEvts events) command))
