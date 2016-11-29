(ns clj-scip.functions)

(defn logic-and [& rest]
  (every? true? rest))

(defn logic-or [& rest]
  (boolean (some true? rest)))

