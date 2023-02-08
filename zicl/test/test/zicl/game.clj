(ns test.zicl.game
  (:require [clojure.string :refer [includes?]]
            [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME NEXT]]
            [zicl.movement :refer [MOVE]]
            [zicl.room :refer [ROOM]]
            [zicl.state :refer [*STATE*]]))

(deftest test-initial-state
  (is (map? GAME))
  (has-key? GAME :rooms)
  (has-key? GAME :objects)
  (has-key? (:objects GAME) :global)
  (has-key? (:objects GAME) :shared)
  (has-key? GAME :player)
  (has-key? (:player GAME) :location))

(def ^:private before
  (-> GAME
      (ROOM :one (DESC "One") (EAST :two) (WEST :three))
      (ROOM :two (DESC "Two") (WEST :one))
      (ROOM :three (DESC "Three") (EAST :one))))

(defmacro test-loop-iteration-fixture [& fs]
  `(binding [*STATE* (atom before)]
     (MOVE :one)
     ~@fs))

(deftest test-loop-iteration
  (testing "Valid input"
    (test-loop-iteration-fixture
     (mute (with-in-str "walk east" (NEXT)))
     (is (map? @*STATE*))
     (has-key? (get-in @*STATE* [:player :location]) :two))
    (test-loop-iteration-fixture
     (mute (with-in-str "west" (NEXT)))
     (is (map? @*STATE*))
     (has-key? (get-in @*STATE* [:player :location]) :three)))
  (testing "Valid input, but no exit"
    (test-loop-iteration-fixture
     (let [before @*STATE*]
       (mute (with-in-str "north" (NEXT)))
       (is (= before @*STATE*)))))
  (testing "No exit message"
    (test-loop-iteration-fixture
     (let [message (with-out-str (with-in-str "north" (NEXT)))]
       (is (true? (includes? message "You can't go that way."))))))
  (testing "Invalid input"
    (test-loop-iteration-fixture
     (let [before @*STATE*]
       (mute (with-in-str "go away" (NEXT)))
       (is (= before @*STATE*)))))
  (testing "Invalid input message"
    (test-loop-iteration-fixture
     (let [message (with-out-str (with-in-str "go away" (NEXT)))]
       (is (true? (includes? message "I don't know how to do that.")))))))