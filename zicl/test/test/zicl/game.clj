(ns test.zicl.game
  (:require [clojure.string :refer [includes?]]
            [clojure.test :refer [deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.common :refer [DESC]]
            [zicl.exit :refer [EAST WEST]]
            [zicl.game :refer [GAME NEXT]]
            [zicl.movement :refer [MOVE]]
            [zicl.room :refer [ROOM]]))

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
      (ROOM :three (DESC "Three") (EAST :one))
      (MOVE :one)))

(deftest test-loop-iteration
  (testing "Valid input"
    (let [after (mute (with-in-str "walk east" (NEXT before)))]
      (is (map? after))
      (has-key? (get-in after [:player :location]) :two))
    (let [after (mute (with-in-str "west" (NEXT before)))]
      (is (map? after))
      (has-key? (get-in after [:player :location]) :three)))
  (testing "Valid input, but no exit"
    (let [after (mute (with-in-str "north" (NEXT before)))]
      (is (= before after))))
  (testing "No exit message"
    (let [message (with-out-str (with-in-str "north" (NEXT before)))]
      (is (true? (includes? message "You can't go that way.")))))
  (testing "Invalid input"
    (let [after (mute (with-in-str "go away" (NEXT before)))]
      (is (= before after))))
  (testing "Invalid input message"
    (let [message (with-out-str (with-in-str "go away" (NEXT before)))]
      (is (true? (includes? message "I don't know how to do that."))))))