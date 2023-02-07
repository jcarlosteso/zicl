(ns zicl.game
  (:require [clojure.pprint :refer [pprint]]
            [zicl.movement :refer [WALK]]
            [zicl.parser :refer [PARSER]]))

(def GAME
  {:rooms {}
   :objects {:global {} :shared {}}
   :player {:location {}}})

(defn MAIN-LOOP [initial-state]
  (loop [current-state initial-state]
    (pprint current-state)
    (if-let [{:keys [_verb direct]} (PARSER)]
      (recur (WALK current-state direct))
      (do
        (println "I don't know how to do that.\n")
        (recur current-state)))))

(defn GO [game]
  (println "Starting game...")
  (MAIN-LOOP game))