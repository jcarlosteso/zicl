(ns test.zicl.movement
  (:require [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME]]
            [zicl.movement :refer [MOVE WALK]]
            [zicl.room :refer [ROOM]]
            [zicl.state :refer [*STATE*]]))

(def ^:private initial-state
  (-> GAME
      (ROOM :entrance (EAST :east) (WEST :west))
      (ROOM :east (EAST :far-east) (WEST :entrance))
      (ROOM :west (EAST :entrance) (WEST :far-west))
      (ROOM :far-east (WEST :east))
      (ROOM :far-west (EAST :west))))

(defmacro test-move-fixture [& fs]
  `(binding [*STATE* (atom initial-state)]
     ~@fs))

(deftest test-move
  (testing "Existing room"
    (test-move-fixture
      (MOVE :entrance)
      (is (map? @*STATE*))
      (has-key? (-> @*STATE* :player :location) :entrance)))
  (testing "Nonexistent room"
    (test-move-fixture
      (is (thrown-with-msg? Exception
                            #"Room :nowhere does not exist."
                            (MOVE :nowhere))))))

(defmacro test-walk-fixture [& fs]
  `(binding [*STATE* (atom initial-state)]
     (MOVE :entrance)
     ~@fs))

(deftest test-walk
  (testing "Existing exit"
    (test-walk-fixture
     (mute (WALK "west"))
     (has-key? (-> @*STATE* :player :location) :west)))
  (testing "No exit => no movement"
    (test-walk-fixture
     (mute (WALK "north"))
     (has-key? (-> @*STATE* :player :location) :entrance)))
  (testing "No exit => error message"
    (let [message (with-out-str (test-walk-fixture (WALK "north")))]
      (is (= "You can't go that way.\n\n" message)))))