(ns zicl.game
  (:require [clojure.pprint :refer [pprint]]))

(def GAME
  {:rooms {}
   :objects {:global {} :shared {}}})

(defn GO [game]
  (println "Starting game...")
  (pprint game))