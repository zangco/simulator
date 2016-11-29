(ns clj-scip.core
  (:gen-class)
  (:require [clj-scip.library :refer :all]
            [clj-scip.functions :refer :all]
            [clj-scip.simulation :refer :all]
            [clj-scip.dot :refer :all]))

(defn -main []
  (let [in-outs 
        ['(two-one-gate [in6 in2] [[out1 logic-and] [out2 logic-or]])
         '(two-one-gate1 [in3 out1] [[out3 logic-and]])]]
    (to-dot
     (map #(first %) in-outs)
     (get-connections in-outs)))

  (let [c
        (circuit
         (two-one-gate [in1 in2] [[out1 logic-and] [out2 logic-or]])
         (two-one-gate [in3 out1] [[out3 logic-and]]))]
    (probe :out1 (:out1 c))
    (probe :out2 (:out2 c))
    (probe :out3 (:out3 c))
    (println :in1)
    (set-signal! (:in1 c) true)
    (println :in2)
    (set-signal! (:in2 c) true)
    (println :in3)
    (set-signal! (:in3 c) true))
  (println "running"))
