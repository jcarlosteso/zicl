(ns test.zicl.movement
  (:require [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.movement :refer [HERE MOVE WALK]]
            [zicl.room :refer [ROOM]]
            [zicl.state :refer [*STATE* INITIAL-STATE]]))

(defmacro with-dungeon [& fs]
  `(binding [*STATE* (atom INITIAL-STATE)]
     (ROOM :entrance (EAST :east) (WEST :west))
     (ROOM :east (EAST :far-east) (WEST :entrance))
     (ROOM :west (EAST :entrance) (WEST :far-west))
     (ROOM :far-east (WEST :east))
     (ROOM :far-west (EAST :west))
     ~@fs))

(deftest test-move
  (testing "Existing room"
    (with-dungeon
      (MOVE :entrance)
      (is (map? @*STATE*))
      (is (= :entrance (:key (HERE))))))
  (testing "Nonexistent room"
    (with-dungeon
      (is (thrown-with-msg? Exception
                            #"Room :nowhere does not exist."
                            (MOVE :nowhere))))))

(defmacro with-player-in-dungeon [& fs]
  `(with-dungeon
     (MOVE :entrance)
     ~@fs))

(deftest test-walk
  (testing "Existing exit"
    (with-player-in-dungeon
     (mute (WALK "west"))
     (is (= :west (:key (HERE))))))
  (testing "No exit => no movement"
    (with-player-in-dungeon
     (mute (WALK "north"))
     (is (= :entrance (:key (HERE))))))
  (testing "No exit => error message"
    (let [message (with-out-str (with-player-in-dungeon (WALK "north")))]
      (is (= "You can't go that way.\n\n" message))))
  (testing "No player in dungeon"
    (with-dungeon
      (is (thrown-with-msg? Exception
                            #"The player has not been placed in any room."
                            (mute (WALK "east")))))))

(deftest test-here
  (testing "Is well formed"
    (with-player-in-dungeon
     (let [here (HERE)]
       (is (map? here))
       (has-key? here :key)
       (has-key? here :room)
       (is (keyword? (:key here)))
       (is (map? (:room here))))))
  (testing "Player didn't move"
    (with-player-in-dungeon
     (is (= :entrance (:key (HERE))))))
  (testing "Player moved"
    (with-player-in-dungeon
     (WALK "west")
     (WALK "west")
     (is (= :far-west (:key (HERE))))))
  (testing "Player is nowhere"
    (with-dungeon
     (let [here (HERE)]
       (is (nil? (:key here)))
       (is (nil? (:room here)))))))