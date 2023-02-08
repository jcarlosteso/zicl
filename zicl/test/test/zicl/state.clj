(ns test.zicl.state
  (:require [clojure.test :refer [deftest is]]
            [test.helpers :refer [has-key?]]
            [zicl.state :refer [INITIAL-STATE]]))


(deftest test-initial-state
  (is (map? INITIAL-STATE))
  (has-key? INITIAL-STATE :rooms)
  (has-key? INITIAL-STATE :objects)
  (has-key? (:objects INITIAL-STATE) :global)
  (has-key? (:objects INITIAL-STATE) :shared)
  (has-key? INITIAL-STATE :player)
  (has-key? (:player INITIAL-STATE) :location))
