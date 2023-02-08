(ns test.zicl.exit
  (:require [clojure.test :refer [are deftest function? testing]]
            [test.helpers :refer [have-key?]]
            [zicl.exit :refer [EAST NORTH SORRY SOUTH TO WEST]]))

(def ^:private nexit-message "There's a reason why you can't go that way.")

(deftest test-exit
  (let [east-uexit (EAST :somewhere)
        west-uexit (WEST TO :somewhere)
        north-nexit (NORTH nexit-message)
        south-nexit (SOUTH SORRY nexit-message)]
    (testing "Exits are prop functions"
      (are [exit] (function? exit)
        east-uexit
        west-uexit
        north-nexit
        south-nexit))
    (testing "Add an :exits key to the room"
      (have-key? :exits
                 (east-uexit {})
                 (west-uexit {})
                 (north-nexit {})
                 (south-nexit {})))
    (testing "Unconditional exits"
      (are [exit key] (= {:to :somewhere} (get-in exit [:exits key]))
        (east-uexit {}) :east
        (west-uexit {}) :west))
    (testing "Non-exits"
      (are [exit key] (= {:sorry nexit-message} (get-in exit [:exits key]))
        (north-nexit {}) :north
        (south-nexit {}) :south))))