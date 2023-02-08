(ns test.zicl.movement
  (:require [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME]]
            [zicl.movement :refer [MOVE WALK]]
            [zicl.room :refer [ROOM]]))

(def ^:private initial-state
  (-> GAME
      (ROOM :entrance (EAST :east) (WEST :west))
      (ROOM :east (EAST :far-east) (WEST :entrance))
      (ROOM :west (EAST :entrance) (WEST :far-west))
      (ROOM :far-east (WEST :east))
      (ROOM :far-west (EAST :west))))

(deftest test-move
  (testing "Existing room"
    (let [state (MOVE initial-state :entrance)]
      (is (map? state))
      (has-key? (-> state :player :location) :entrance)))
  (testing "Nonexistent room"
    (is (thrown-with-msg? Exception
                          #"Room :nowhere does not exist."
                          (MOVE initial-state :nowhere)))))

(deftest test-walk
  (testing "Existing exit"
    (let [state (-> initial-state (MOVE :entrance) (WALK "west"))]
      (has-key? (-> state :player :location) :west)))
  (testing "No exit => no movement"
    (let [state (mute (-> initial-state (MOVE :entrance) (WALK "north")))]
      (has-key? (-> state :player :location) :entrance)))
  (testing "No exit => error message"
    (let [message (with-out-str (-> initial-state (MOVE :entrance) (WALK "north")))]
      (is (= "You can't go that way.\n\n" message)))))