(ns zicl.game
  (:require [clojure.pprint :refer [pprint]]
            [zicl.movement :refer [WALK]]
            [zicl.parser :refer [PARSER]]
            [zicl.state :refer [*STATE*]]))

(def GAME
  {:rooms {}
   :objects {:global {} :shared {}}
   :player {:location {}}})

(defn NEXT []
  (pprint @*STATE*)
  (if-let [{:keys [_verb direct]} (PARSER)]
    (WALK direct)
    (println "I don't know how to do that.\n")))

(defn MAIN-LOOP []
  (loop []
    (NEXT)
    (recur)))

(defn GO [game]
  (println "Starting game...")
  (binding [*STATE* (atom GAME)]
    (MAIN-LOOP)))