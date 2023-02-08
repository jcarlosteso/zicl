(ns test.zicl.parser
  (:require [clojure.test :refer [are deftest is testing]]
            [test.helpers :refer [has-key? mute]]
            [zicl.parser :refer [PARSER]]))

(defmacro submit [text]
  `(mute (with-in-str ~text (PARSER))))

(deftest test-parser
  (testing "Valid verb+direct object input"
    (let [result (submit "walk north")]
      (is (map? result))
      (has-key? result :verb)
      (has-key? result :direct)
      (is (= :walk (:verb result)))
      (is (= "north" (:direct result)))))
  (testing "Valid one-word input"
    (are [input] (= :walk (:verb (submit input)))
      "east"
      "west"
      "whatever")
    (are [input] (= input (:direct (submit input)))
      "east"
      "west"
      "whatever"))
  (testing "Invalid input"
    (is (nil? (submit "go somewhere")))))