(ns zicl.room)

(defmacro ROOM [game room-key & properties]
  `(assoc-in ~game
             [:rooms ~room-key]
             (reduce
              (fn [room# prop-fn#] (prop-fn# room#))
              {}
              ~(vec properties))))