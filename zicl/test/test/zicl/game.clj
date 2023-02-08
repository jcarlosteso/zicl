(ns test.zicl.game
  (:require [clojure.test :refer [deftest is]]
            [test.helpers :refer [has-key?]]
            [zicl.game :refer [GAME]]))

(deftest test-initial-state
  (is (map? GAME))
  (has-key? GAME :rooms)
  (has-key? GAME :objects)
  (has-key? (:objects GAME) :global)
  (has-key? (:objects GAME) :shared)
  (has-key? GAME :player)
  (has-key? (:player GAME) :location))