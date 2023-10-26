(ns zicl.dungeon)

(defn ROOM [room-name description]
  (-> (create-ns 'zicl.temp.rooms)
      (intern (symbol room-name)
              {:key (keyword room-name) :desc description})
      (alter-meta! assoc :object :room)))

;; (ROOM "a" "b")