(ns zicl.room)

(defn ROOM [game room-key room-name & exits]
  (-> game
      (assoc-in [:rooms room-key] {:name room-name})
      (assoc-in [:rooms room-key :exits] (apply merge exits))))