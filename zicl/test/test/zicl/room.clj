(ns test.zicl.room
  (:require [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key?]]
            [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST]]
            [zicl.game :refer [GAME]]
            [zicl.room :refer [ROOM]]))

(defmacro has-room? [state room-key]
  `(has-key? (:rooms ~state) ~room-key))

(defmacro has-prop? [state room-key prop-key]
  `(has-key? (get-in ~state [:rooms ~room-key]) ~prop-key))

(deftest test-rooms
  (testing "Creating a room"
    (let [state (-> GAME (ROOM :test-room))]
      (is (map? state))
      (has-room? state :test-room)))
  (testing "Creating a room with a description"
    (let [state (-> GAME (ROOM :with-desc (DESC "Test Room")))]
      (has-room? state :with-desc)
      (has-prop? state :with-desc :description)
      (is (= "Test Room" (get-in state [:rooms :with-desc :description])))))
  (testing "Creating a room with an exit"
    (let [state (-> GAME (ROOM :with-exit (EAST :somewhere-else)))]
      (has-room? state :with-exit)
      (has-prop? state :with-exit :exits)
      (has-key? (get-in state [:rooms :with-exit :exits]) :east))))