(ns zicl.exit)

(defmacro EAST [room-key] 
`(fn [room#] (update room# :exits merge {:east ~room-key})))
(defmacro WEST [room-key] 
`(fn [room#] (update room# :exits merge {:west ~room-key})))