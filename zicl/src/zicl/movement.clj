(ns zicl.movement
  (:require [clojure.string :refer [lower-case]]
            [zicl.state :refer [*STATE*]]))

(defmacro MOVE [where]
  `(if-let [room# (get-in @*STATE* [:rooms ~where])]
     (swap! *STATE* assoc-in [:player :location] {~where room#})
     (throw (Exception. (str "Room " ~where " does not exist.")))))

(defmacro HERE []
  `(->> (get-in @*STATE* [:player :location])
        (into [] cat)
        (zipmap [:key :room])))

(defn has-exit? [room direction]
  (contains? (:exits room) direction))

(defmacro WALK [direction-str]
  `(let [room# (:room (HERE))
         direction# (-> ~direction-str lower-case keyword)]
     (cond
       (nil? room#) (throw (Exception. "The player has not been placed in any room."))
       (has-exit? room# direction#) (MOVE (get-in room# [:exits direction#]))
       :else (println "You can't go that way.\n"))))