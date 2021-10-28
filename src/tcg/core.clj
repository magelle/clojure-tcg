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
  [{:evt "GameStarted"
    :player1Deck (:player1Deck cmd)
    :player2Deck (:player2Deck cmd)}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked 0}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked 1}
   {:evt "PlayerPickedACard" :player "Player 1" :cardPicked 2}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked 5}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked 4}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked 3}
   {:evt "PlayerPickedACard" :player "Player 2" :cardPicked 2}])

(defn receive
  [events command]
  (decide (applyEvts events) command))
