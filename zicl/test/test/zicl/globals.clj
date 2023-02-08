(ns test.zicl.globals
  (:require [clojure.test :refer [deftest is]]
            [test.helpers :refer [has-key?]]
            [zicl.globals :refer [GLOBAL]]
            [zicl.state :refer [*STATE* INITIAL-STATE]]))

(defmacro with-setup [& fs]
  `(binding [*STATE* (atom INITIAL-STATE)]
     ~@fs))

(deftest test-globals
  (with-setup
    (GLOBAL :dead false)
    (is (map? @*STATE*))
    (has-key? (:globals @*STATE*) :dead)
    (is (false? (get-in @*STATE* [:globals :dead])))))