(ns clj-scip.dot)

(defn- to-valid-dot-name [s]
  (clojure.string/replace s "-" "_"))

(defn- filter-between-gates [connections]
  (filter #(and  (not (nil? (:to %))) (not (nil? (:from %)))) connections))

(defn- filter-connections [f connections]
  (filter #(nil? (f %)) connections))

(def filter-outputs (partial filter-connections :to))
(def filter-inputs (partial filter-connections :from))

(defn- gates-to-dot [gates]
  (doseq [g gates]
    (println (to-valid-dot-name g)
             "[shape=box]")))

(defn- print-connection [from to label]
  (let [f (to-valid-dot-name from)
        t (to-valid-dot-name to)
        l (to-valid-dot-name label)]
    (println
     (str f " -> " t " [label=" l"]"))))

(defn- connections-to-dot [connections]
  (doseq [{:keys [wire from to]}
          (filter-between-gates connections)]
    (print-connection from to wire)))

(defn- outputs-to-dot [connections]
  (doseq [{:keys [wire from]}
          (filter-outputs connections)]
    (print-connection from wire wire)))

(defn- inputs-to-dot [connections]
  (doseq [{:keys [wire to]}
          (filter-inputs connections)]
    (print-connection wire to wire )))

(defn to-dot [gates connections]
  (println "digraph g {")
  (gates-to-dot gates)
  (connections-to-dot connections)
  (outputs-to-dot connections)
  (inputs-to-dot connections)
  (println "}"))

#_(spit "test.dot"
      (with-out-str
        (to-dot
         (map #(first %) in-outs)
         (get-connections in-outs))))

