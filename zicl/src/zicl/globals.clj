(ns zicl.globals
  (:require [clojure.string :refer [upper-case]]))

(defmacro GLOBAL [var-name value]
  `(def ~(-> var-name name upper-case symbol) (atom ~value)))

(defmacro CONSTANT [var-name value]
  `(def ~(-> var-name name upper-case symbol) ~value))

(defn GVAL [arg] @arg)

(def SETG reset!)