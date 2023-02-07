(ns sample.core
  (:require [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME GO]]
            [zicl.movement :refer [MOVE]]
            [zicl.room :refer [ROOM]]))

(defn -main []
  (-> GAME
      (ROOM :living-room
            (DESC "Living Room")
            (EAST :kitchen))
      (ROOM :kitchen
            (DESC "Kitchen")
            (WEST :living-room))
      (MOVE :living-room)
      GO))