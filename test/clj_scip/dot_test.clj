(ns clj-scip.dot-test
  (:require [clj-scip.dot :refer :all]
            [clojure.test :refer :all]))

(deftest dot-test
  (testing "Gate"
    (is (= "my_gate [shape=box]\n"
           (with-out-str (#'clj-scip.dot/gates-to-dot ['my-gate])))))

  (testing "Connections"
    (is (= "f1 -> t1 [label=w1]\n"
           (with-out-str
             (#'clj-scip.dot/connections-to-dot [{:wire 'w1 :from 'f1 :to 't1}])))))

  (testing "Inputs"
    (is (= "w1 -> t1 [label=w1]\n"
           (with-out-str
             (#'clj-scip.dot/inputs-to-dot [{:wire 'w1 :from nil :to 't1}])))))

   (testing "Outputs"
    (is (= "f1 -> w1 [label=w1]\n"
           (with-out-str
             (#'clj-scip.dot/outputs-to-dot [{:wire 'w1 :from 'f1 :to nil}]))))))




