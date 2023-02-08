(ns test.zicl.game
  (:require [clojure.string :refer [includes?]]
            [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [mute]]
            [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [NEXT]]
            [zicl.movement :refer [HERE MOVE]]
            [zicl.room :refer [ROOM]]
            [zicl.state :refer [*STATE* INITIAL-STATE]]))

(defmacro with-setup [& fs]
  `(binding [*STATE* (atom INITIAL-STATE)]
     (ROOM :one (DESC "One") (EAST :two) (WEST :three))
     (ROOM :two (DESC "Two") (WEST :one))
     (ROOM :three (DESC "Three") (EAST :one))
     (MOVE :one)
     ~@fs))

(deftest test-loop-iteration
  (testing "Valid input"
    (with-setup
     (mute (with-in-str "walk east" (NEXT)))
     (is (map? @*STATE*))
     (is (= :two (:key (HERE)))))
    (with-setup
     (mute (with-in-str "west" (NEXT)))
     (is (map? @*STATE*))
     (is (= :three (:key (HERE))))))
  (testing "Valid input, but no exit"
    (with-setup
     (let [before @*STATE*]
       (mute (with-in-str "north" (NEXT)))
       (is (= before @*STATE*)))))
  (testing "No exit message"
    (with-setup
     (let [message (with-out-str (with-in-str "north" (NEXT)))]
       (is (true? (includes? message "You can't go that way."))))))
  (testing "Invalid input"
    (with-setup
     (let [before @*STATE*]
       (mute (with-in-str "go away" (NEXT)))
       (is (= before @*STATE*)))))
  (testing "Invalid input message"
    (with-setup
     (let [message (with-out-str (with-in-str "go away" (NEXT)))]
       (is (true? (includes? message "I don't know how to do that.")))))))