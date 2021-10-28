(ns tcg.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn apply
  [events]
  (class {}))

(defn decide
  [state command]
  ["GameStarted"])

(defn receive
  [events command]
  (decide (apply events) command))
