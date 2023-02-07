(ns zicl.common)

(defmacro DESC [text]
  `(fn [room#] (assoc room# :description ~text)))