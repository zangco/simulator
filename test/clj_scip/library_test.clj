(ns clj-scip.library-test
  (:require [clj-scip.library :refer :all]
            [clojure.test :refer :all]))

(def gates
  ['(two-one-gate [in1 in2] [[out1 logic-and] [out2 logic-or]])
   '(two-one-gate [in3 out1] [[out3 logic-and]])])

(def gate (first gates))

(deftest library-test
  (testing "Get wires from gates"
    (is (= #{'in1 'in2 'in3 'out1 'out2 'out3}
           (get-wires gates))))
  (testing "Get wires grouped"
    (is (= {:inputs #{'in1 'in2 'in3 'out1} :outputs #{'out1 'out2 'out3}}
           (get-wires-grouped gates))))
  (testing "Gates: list to map"
    (is (= '({:gate two-one-gate :inputs #{in1 in2} :outputs #{out1 out2}})
           (gates-list->map [gate]))))
  (testing "Gates containng"
    (is (= '(two-one-gate)
           (get-gate-contains '({:gate two-one-gate :inputs #{in1 in2} :outputs #{out1 out2}}) 'out2  :outputs)))))

