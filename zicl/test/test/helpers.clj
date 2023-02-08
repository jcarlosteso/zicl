(ns test.helpers
  (:require [clojure.test :refer [are is]]))

(defmacro has-key? [m k]
  `(is (true? (contains? ~m ~k))))

(defmacro have-key? [k & exprs]
  `(are [m#] (has-key? m# ~k)
     ~@exprs))

(defmacro mute [& exprs]
  `(let [out# (new java.io.StringWriter)]
     (binding [*out* out#]
       ~@exprs)))