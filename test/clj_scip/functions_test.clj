(ns clj-scip.functions-test
  (:require [clojure.test :refer :all]
            [clj-scip.functions :refer :all]))

(deftest or-test
  (testing "All false."
    (is (= false (logic-or false false false))))

  (testing "One true two false"
    (is (= true (logic-or true false false))))

  (testing "All true"
    (is (= true (logic-or true true true)))))

(deftest and-test
  (testing "All false."
    (is (= false (logic-and false false false))))

  (testing "One true two false"
    (is (= false (logic-and true false false))))

  (testing "All true"
    (is (= true (logic-and true true true)))))

