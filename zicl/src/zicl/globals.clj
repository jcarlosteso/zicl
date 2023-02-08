(ns zicl.globals
  (:require [zicl.state :refer [*STATE*]]))

(defmacro GLOBAL [global-key initial-value]
  `(swap! *STATE* assoc-in [:globals ~global-key] ~initial-value))