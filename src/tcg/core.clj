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
  [state command]
  [{:evt "GameStarted"
    :player1Deck []
    :player2Deck []}
   {:evt "PlayerPickedACard" :player "Player 1"}
   {:evt "PlayerPickedACard" :player "Player 1"}
   {:evt "PlayerPickedACard" :player "Player 1"}
   {:evt "PlayerPickedACard" :player "Player 2"}
   {:evt "PlayerPickedACard" :player "Player 2"}
   {:evt "PlayerPickedACard" :player "Player 2"}])

(defn receive
  [events command]
  (decide (applyEvts events) command))
