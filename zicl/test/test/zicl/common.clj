(ns test.zicl.common
  (:require [clojure.test :refer [deftest function? is testing]]
            [test.helpers :refer [has-key?]]
            [zicl.common :refer [DESC]]))

(deftest test-description
  (testing "Is a prop function"
    (is (function? (DESC "Test description"))))
  (testing "Adds description prop to object"
    (let [prop-fn (DESC "Test description")
          result (prop-fn {})]
      (has-key? result :description)
      (is (= "Test description") (:description result)))))