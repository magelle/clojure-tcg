(ns tcg.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn removeFirst [list remove]
  (let [[n m] (split-with (partial not= remove) list)]
    (concat n (rest m))))

(def initialState
  {:activePlayer :Player1
   :activePlayerState {:mana 0 :manaSlots 0 :hand [] :deck [] :health 30}
   :opponentState {:mana 0 :manaSlots 0 :hand [] :deck [] :health 30}
   :opponent :Player2})

(defn gameStarted [state evt]
  (let [activePlayerDeck (:player1Deck evt)
        opponentDeck (:player2Deck evt)
        activePlayerState (:activePlayerState state)
        opponentState (:opponentState state)
        activePlayerState (assoc activePlayerState :deck activePlayerDeck)
        opponentState (assoc opponentState :deck opponentDeck)]
    (-> state
        (assoc :activePlayerState activePlayerState)
        (assoc :opponentState opponentState))))

(defn manaSlotsFilled [state evt]
  (let [activePlayerState (:activePlayerState state)
        manaSlots (:manaSlots activePlayerState)
        mana (:mana activePlayerState)
        newActivePlayerState (assoc activePlayerState :mana manaSlots)]
    (assoc state :activePlayerState newActivePlayerState)))

(defn playerPickedACard [state evt]
  (let [cardPicked (:cardPicked evt)
        player (:player evt)
        playerState (if (= (:activePlayer state) player)  (:activePlayerState state)    (:opponentState state))
        deck (:deck playerState)
        newDeck (removeFirst deck cardPicked)
        newPlayerState (-> playerState
                           (assoc :deck newDeck)
                           (assoc :hand [cardPicked]))]
    (assoc state (if (= (:activePlayer state) player) :activePlayerState :opponentState) newPlayerState)))

(defn invertPlayers [state]
  (let [activePlayer (:activePlayer state)
        opponent (:opponent state)
        activePlayerState (:activePlayerState state)
        opponentState (:opponentState state)]
    (-> state
        (assoc :activePlayer opponent)
        (assoc :activePlayerState opponentState)
        (assoc :opponent activePlayer)
        (assoc :opponentState activePlayerState))))

(defn playerBecameActive [state evt]
  (let [newActivePlayer (:player evt)
        doesChange (not= newActivePlayer (:activePlayer state))]
    (if doesChange (invertPlayers state) state)))

(defn receivedManaSlot [state evt]
  (let [activePlayerState (:activePlayerState state)
        manaSlots (:manaSlots activePlayerState)
        newManaSlots (+ manaSlots 1)
        newActivePlayer (assoc activePlayerState :manaSlots newManaSlots)]
    (assoc state :activePlayerState newActivePlayer)))

(defn manaSlotsFilled [state evt]
  (let [activePlayerState (:activePlayerState state)
        manaSlots (:manaSlots activePlayerState)
        newActivePlayer (assoc activePlayerState :mana manaSlots)]
    (assoc state :activePlayerState newActivePlayer)))

(defn playerLooseHealth [state evt]
  (let [player (:player evt)
        playerToUpdate (if (= (:activePlayer state) player) :activePlayerState :opponentState)
        playerState (playerToUpdate state)
        actualHealth (:health playerState)
        newHealth (dec actualHealth)
        newState (assoc playerState :health newHealth)]
    (assoc state playerToUpdate newState)))

(defn applyEvt [state evt]
  (cond
    (= (:evt evt) :GameStarted) (gameStarted state evt)
    (= (:evt evt) :PlayerBecameActive) (playerBecameActive state evt)
    (= (:evt evt) :ManaSlotsFilled) (manaSlotsFilled state, evt)
    (= (:evt evt) :ReceivedManaSlot) (receivedManaSlot state evt)
    (= (:evt evt) :ManaSlotsFilled) (manaSlotsFilled state evt)
    (= (:evt evt) :PlayerPickedACard) (playerPickedACard state evt)
    (= (:evt evt) :HealthLost) (playerLooseHealth state evt)
    :else state))

(defn applyEvts
  [events]
  (reduce applyEvt initialState events))

(defn startGame [state cmd]
  (let [player1Deck (:player1Deck cmd)
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

(defn playCard [state cmd]
  (let [playedCard (:card cmd)
        activePlayer (:activePlayer state)
        activePlayerMana (:mana (:activePlayerState state))
        opponent (:opponent state)
        opponentState (:opponentState state)
        opponentHealth (:health opponentState)]
    (if (>= activePlayerMana playedCard)
      (if (= opponentHealth 1)
        [{:evt :CardPlayed :player activePlayer :card playedCard}
         {:evt :HealthLost :player opponent}
         {:evt :WinGame :player activePlayer}]
        [{:evt :CardPlayed :player activePlayer :card playedCard}
         {:evt :HealthLost :player opponent}])
      [{:error "not enough mana"}])))

(defn endTurn [state cmd]
  (let [activePlayer (:activePlayer state)
        opponent (:opponent state)
        opponentManaSlots (:manaSlots (:opponentState state))
        player2Deck (:deck (:opponentState state))
        [player2Card1] player2Deck]
    (concat
     (concat [{:evt :PlayerEndedTurn :player activePlayer} {:evt :PlayerBecameActive :player opponent}]
             (if (< opponentManaSlots 10) [{:evt :ReceivedManaSlot :player opponent}] []))
     [{:evt :ManaSlotsFilled :player opponent}
      {:evt :PlayerPickedACard :player opponent :cardPicked player2Card1}])))

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
