(ns test.core
  (:require [clojure.string :refer [starts-with?]]
            [clojure.test :refer [run-tests]]
            [test.zicl.common]
            [test.zicl.exit]
            [test.zicl.game]
            [test.zicl.movement]
            [test.zicl.parser]
            [test.zicl.room]
            [test.zicl.state]))

(defn- test-ns? [namespace]
  (-> namespace
      ns-name
      (starts-with? "test.zicl")))

(defn start [_]
  (->> (all-ns)
       (filter test-ns?)
       (apply run-tests)))