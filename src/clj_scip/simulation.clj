(ns clj-scip.simulation)
;; Helpers.

(defn invoke-all [fns]
  (doall (map #(%) fns)))

;; Signals.

(defn get-signal [wire] (wire :get-signal))
(defn set-signal! [wire v] ((wire :set-signal!) v))
(defn add-action! [wire p] ((wire :add-action!) p))

;; Wires.

(defn make-wire []
  (let [signal (atom false)
        effects (atom (list))]
    (letfn [(set-signal! [new]
              (if (not (= signal new))
                (do (reset! signal new)
                    (invoke-all @effects))
                :done))

            (add-action! [f]
              (swap! effects conj f)
              (f))

            (dispatch [action]
              (condp = action
                :get-signal @signal
                :set-signal! set-signal!
                :add-action! add-action!
                (assert false (str "Unknown operation " action " in make-wire."))))]
      dispatch)))

(defn probe [name wire]
  (add-action! wire
               (fn []
                 (println (str name " New value = " (get-signal wire))))))
