(ns test.zicl.room
  (:require [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key?]]
            [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST]]
            [zicl.room :refer [ROOM]]
            [zicl.state :refer [*STATE* INITIAL-STATE]]))

(defmacro with-setup [& fs]
  `(binding [*STATE* (atom INITIAL-STATE)]
     ~@fs))

(defmacro has-room? [state room-key]
  `(has-key? (:rooms ~state) ~room-key))

(defmacro has-prop? [state room-key prop-key]
  `(has-key? (get-in ~state [:rooms ~room-key]) ~prop-key))

(deftest test-rooms
  (testing "Creating a room"
    (with-setup
      (ROOM :test-room)
      (is (map? @*STATE*))
      (has-room? @*STATE* :test-room)))
  (testing "Creating a room with a description"
    (with-setup
      (ROOM :with-desc (DESC "Test Room"))
      (has-room? @*STATE* :with-desc)
      (has-prop? @*STATE* :with-desc :description)
      (is (= "Test Room" (get-in @*STATE* [:rooms :with-desc :description])))))
  (testing "Creating a room with an exit"
    (with-setup
      (ROOM :with-exit (EAST :somewhere-else))
      (has-room? @*STATE* :with-exit)
      (has-prop? @*STATE* :with-exit :exits)
      (has-key? (get-in @*STATE* [:rooms :with-exit :exits]) :east))))