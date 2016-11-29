(ns clj-scip.library
  (:require [clj-scip.simulation :refer :all]))

; outputs with function setting them
(defn two-one-gate [inputs outputs]
  (letfn [(update-outputs []
            (doseq [[output f] outputs]
              (let [new (apply f (map get-signal inputs))]
                (set-signal! output new))))]
    (doseq [in inputs]
      (add-action! in update-outputs))))

(defn get-wires [gates]
  (into #{}
   (flatten [(map #(second %) gates)
            (map (fn [x] (map first (nth x 2))) gates)])))

(defn get-wires-grouped [gates]
        {:inputs (into #{} (flatten (map #(second %) gates)))
         :outputs (into #{} (flatten (map (fn [x] (map first (nth x 2))) gates)))})

(defn get-gate-contains [gates output colsel]
  (map :gate (filter (fn [x] (contains? (colsel x) output)) gates)))

(defn gates-list->map [gates]
  (map (fn [x] (assoc (get-wires-grouped [x]) :gate (first x))) gates))

(defn ports->wires [ports gates]
  (for [o ports]
    {:wire o
     :from (first (get-gate-contains gates o :outputs))
     :to (first (get-gate-contains gates o :inputs ))}))

(defn get-connections [in-outs]
  (let [{:keys [inputs outputs]} (get-wires-grouped in-outs)
        gates (gates-list->map in-outs)]
    (->
     #{}
     (into (ports->wires outputs gates))
     (into (ports->wires inputs gates)))))

(defmacro circuit [& gates]
  (let [wires (get-wires gates)]
    `(let [~@(mapcat (fn [x] [x '(make-wire)]) wires)]
       ~@gates
       ~(into {} (map (fn [x] [(keyword x) x]) wires)))))

