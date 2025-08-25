(ns bowling-clj.future-no-frame-test
  (:require [clojure.test :refer :all]
            [bowling-clj.future-no-frame :refer :all]))

(deftest rolling-singles
  (testing "Rolling all zeros"
    (let [result (score [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 0 result))))

  (testing "Rolling all ones"
    (let [result (score [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1])]
      (is (= 20 result))))

  (testing "Rolling all nines"
    (let [result (score [9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9])]
      (is (= 180 result)))))

(deftest rolling-spares
  (testing "Single spare"
    (let [result (score [9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 10 result))))

  (testing "Single spare plus additional score"
    (let [result (score [9 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 12 result))))

  (testing "Multiple spare plus additional score"
    (let [result (score [9 1 1 9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 23 result))))

  (testing "All spares"
    (let [result (score [9 1 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1])]
      (is (= 110 result)))))

(deftest rolling-strikes
  (testing "Single strike with bonuses"
    (let [result (score [10 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 14 result))))

  (testing "Two consecutive strikes with bonuses"
    (let [result (score [10 10 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
      (is (= 35 result))))

  (testing "All strikes"
    (let [result (score [10 10 10 10 10 10 10 10 10 10 10 10])]
      (is (= 300 result)))))
