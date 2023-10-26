(ns sample.core
  (:require [zicl.core :refer [start]]
            [zicl.dungeon :refer [ROOM]]))

(ROOM "living-room" "Living Room")

(ROOM "kitchen" "Kitchen")

(defn -main []
  (start))
