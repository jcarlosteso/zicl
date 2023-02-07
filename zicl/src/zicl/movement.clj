(ns zicl.movement
  (:require [clojure.string :refer [lower-case]]))

(defmacro MOVE [game where]
  `(if-let [room# (get-in ~game [:rooms ~where])]
     (assoc-in ~game [:player :location] {~where room#})
     (throw (Exception. (str "Room " ~where " does not exist.")))))

(defmacro HERE [game]
  `(->> (get-in ~game [:player :location])
        (into [] cat)
        (zipmap [:key :room])))

(defmacro WALK [game direction-str]
  `(let [room# (-> ~game HERE :room)
         direction# (-> ~direction-str lower-case keyword)]
     (if (contains? (:exits room#) direction#)
       (MOVE ~game (get-in room# [:exits direction#]))
       (do
         (println "You can't go that way.\n")
         ~game))))