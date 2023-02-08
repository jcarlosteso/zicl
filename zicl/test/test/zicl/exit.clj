(ns test.zicl.exit
  (:require [clojure.test :refer [are deftest function? testing]]
            [test.helpers :refer [have-key?]]
            [zicl.exit :refer [EAST WEST]]))

(deftest test-exit
  (let [east-fn (EAST :somewhere)
        west-fn (WEST :somewhere)]
    (testing "Exits are prop functions"
      (are [exit] (function? exit)
        east-fn
        west-fn))
    (testing "Add an :exits key to the room"
      (have-key? :exits
                 (east-fn {})
                 (west-fn {})))))