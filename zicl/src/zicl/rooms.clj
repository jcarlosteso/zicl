(ns zicl.rooms)

(defmacro FSET? [room flag]
  `(->> ~room
        :flags
        (contains? ~flag)))