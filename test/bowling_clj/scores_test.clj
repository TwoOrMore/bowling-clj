(ns bowling-clj.scores-test
  (:require [clojure.test :refer :all]
            [bowling-clj.future-no-frame :as fnf]
            [bowling-clj.future-no-frame-for :as fnff ]
            [bowling-clj.past-no-frame :as pnf]
            [bowling-clj.past-frame :as pf]))

(def score-functions [fnf/score fnff/score pnf/score pf/score])

(deftest rolling-singles
  (testing "Rolling all zeros"
    (doseq [score score-functions]
      (let [result (score [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 0 result)))))

  (testing "Rolling all ones"
    (doseq [score score-functions]
      (let [result (score [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1])]
        (is (= 20 result)))))

  (testing "Rolling all nines"
    (doseq [score score-functions]
      (let [result (score [9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9])]
        (is (= 180 result))))))

(deftest rolling-spares
  (testing "Single spare"
    (doseq [score score-functions]
      (let [result (score [9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 10 result)))))

  (testing "Single spare plus additional score"
    (doseq [score score-functions]
      (let [result (score [9 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 12 result)))))

  (testing "Multiple spare plus additional score"
    (doseq [score score-functions]
      (let [result (score [9 1 1 9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 23 result)))))

  (testing "All spares"
    (doseq [score score-functions]
      (let [result (score [9 1 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1 9 1])]
        (is (= 110 result))))))

(deftest rolling-strikes
  (testing "Single strike with bonuses"
    (doseq [score score-functions]
      (let [result (score [10 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 14 result)))))

  (testing "Two consecutive strikes with bonuses"
    (doseq [score score-functions]
      (let [result (score [10 10 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0])]
        (is (= 35 result)))))
  
  (testing "All strikes"
    (doseq [score score-functions]
      (let [result (score [10 10 10 10 10 10 10 10 10 10 10 10])]
        (is (= 300 result))))))
