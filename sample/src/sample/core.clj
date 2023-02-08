(ns sample.core
  (:require [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GO]]
            [zicl.movement :refer [MOVE]]
            [zicl.room :refer [ROOM]]))

(ROOM :living-room
      (DESC "Living Room")
      (EAST :kitchen))

(ROOM :kitchen
      (DESC "Kitchen")
      (WEST :living-room))

(defn -main [] 
  (MOVE :living-room)
  (GO))