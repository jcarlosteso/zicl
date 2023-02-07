(ns sample.core
  (:require [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME GO]]
            [zicl.room :refer [ROOM]]))

(defn -main []
  (-> GAME
      (ROOM :living-room
            "Living Room"
            (EAST :kitchen))
      (ROOM :kitchen
            "Kitchen"
            (WEST :living-room))
      GO))