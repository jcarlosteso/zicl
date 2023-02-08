(ns zicl.room
  (:require [zicl.state :refer [*STATE*]]))

(defmacro ROOM [room-key & properties]
  `(swap! *STATE* assoc-in [:rooms ~room-key]
          (reduce
           (fn [room# prop-fn#] (prop-fn# room#))
           {}
           ~(vec properties))))