(ns zicl.movement
  (:require [clojure.string :refer [lower-case]]
            [zicl.state :refer [*STATE*]]))

(defmacro MOVE [where]
  `(if-let [room# (get-in @*STATE* [:rooms ~where])]
     (swap! *STATE* assoc-in [:player :location] {~where room#})
     (throw (Exception. (str "Room " ~where " does not exist.")))))

(defmacro HERE [game]
  `(->> (get-in ~game [:player :location])
        (into [] cat)
        (zipmap [:key :room])))

(defmacro WALK [direction-str]
  `(let [room# (:room (HERE @*STATE*))
         direction# (-> ~direction-str lower-case keyword)]
     (if (contains? (:exits room#) direction#)
       (MOVE (get-in room# [:exits direction#]))
       (println "You can't go that way.\n"))))