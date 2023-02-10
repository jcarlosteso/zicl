(ns test.zicl.exit
  (:require [clojure.test :refer [are deftest function? is testing]]
            [test.helpers :refer [has-key? have-key?]]
            [zicl.exit :refer [DOWN EAST ELSE EXIT-SPEC IF IS NORTH OPEN OUT PER SORRY SOUTH TO UP WEST]]))

(def ^:private nexit-message "There's a reason why you can't go that way.")

(def implicit-uexit (EAST :somewhere))
(def explicit-uexit (WEST TO :somewhere))
(def implicit-nexit (NORTH nexit-message))
(def explicit-nexit (SOUTH SORRY nexit-message))
(def cexit (UP TO :somewhere IF :condition ELSE nexit-message))
(def dexit (DOWN TO :somewhere IF :door IS OPEN ELSE nexit-message))
(def fexit (OUT PER :random-teleport))

(deftest test-exit-definitions
  (testing "Exits are prop functions"
    (are [exit] (function? exit)
      implicit-uexit
      explicit-uexit
      implicit-nexit
      explicit-nexit
      cexit))
  (testing "Exit definitions add an :exits key to the room"
    (have-key? :exits
               (implicit-uexit {})
               (explicit-uexit {})
               (implicit-nexit {})
               (explicit-nexit {})
               (cexit {}))))

(deftest test-unconditional-exits
  (are [exit key] (= {:to :somewhere} (get-in exit [:exits key]))
    (implicit-uexit {}) :east
    (explicit-uexit {}) :west))

(deftest test-non-exits
  (are [exit key] (= {:sorry nexit-message} (get-in exit [:exits key]))
    (implicit-nexit {}) :north
    (explicit-nexit {}) :south))

(deftest test-conditional-exits
  (let [room (cexit {})
        exit (get-in room [:exits :up])]
    (has-key? (:exits room) :up)
    (has-key? exit :global)
    (has-key? exit :to)
    (has-key? exit :sorry)
    (is (= :condition (:global exit)))
    (is (= :somewhere (:to exit)))
    (is (= nexit-message (:sorry exit)))))

(deftest test-door-exits
  (let [room (dexit {})
        exit (get-in room [:exits :down])]
    (has-key? (:exits room) :down)
    (has-key? exit :shared)
    (has-key? exit :to)
    (has-key? exit :sorry)
    (is (= :door (:shared exit)))
    (is (= :somewhere (:to exit)))
    (is (= nexit-message (:sorry exit)))))

(deftest test-flexible-exits
  (let [room (fexit {})
        exit (get-in room  [:exits :out])]
    (has-key? (:exits room) :out)
    (has-key? exit :action)
    (is (= :random-teleport (:action exit)))))

(deftest test-invalid-exits
  (are [spec] (nil? spec)
    (EXIT-SPEC 3)
    (EXIT-SPEC TO IF :whatever)
    (EXIT-SPEC PER :aspera TO :astra)
    (EXIT-SPEC TO TO IF IF ELSE "else")))