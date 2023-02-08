(ns test.zicl.exit
  (:require [clojure.test :refer [are deftest function? testing]]
            [test.helpers :refer [have-key?]]
            [zicl.exit :refer [EAST WEST TO]]))

(deftest test-exit
  (let [east-uexit (EAST :somewhere)
        west-uexit (WEST TO :somewhere)]
    (testing "Exits are prop functions"
      (are [exit] (function? exit)
        east-uexit
        west-uexit))
    (testing "Add an :exits key to the room"
      (have-key? :exits
                 (east-uexit {})
                 (west-uexit {})))))