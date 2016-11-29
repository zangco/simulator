(ns clj-scip.html
  (:require [hiccup.core :refer :all]))

(def circuit ['(two-one-gate [in6 in2] [[out1 logic-and] [out2 logic-or]])
              '(two-one-gate1 [in3 out1] [[out3 logic-and]])])

(def gate-height 100)
(def gate-width 50)
(def y-border 10)
(def x-border 10)

(defn- ports-with-coord
  [ports x-offset y-offset port-name]
  (map-indexed
   (fn [i p]
     {:port (port-name p)
      :x x-offset
      :y (+ y-offset (* i 50))})
   ports))

(defn- gates-with-coord
  [circuit]
  (map-indexed
   (fn [i g]
     (let [x (+ (* i (+ gate-width 50)) x-border)
           y y-border]
       {:gate (first g)
        :x x
        :y y
        :inputs (ports-with-coord (second g) x y identity)
        :outputs (ports-with-coord (nth g 2) (+ x gate-width)  y first)
        }))
   circuit))

(defn- connections
  [g]
  (for [i (mapcat :inputs g)
        o (mapcat :outputs g)
        :when (= (:port i) (:port o))]
    [o i]))

(defn- connections->svg
  [cs]
  (map
   (fn [c]
         (let [[o i] c]
           [:line { :x1 (:x o) :y1 (:y o) :x2 (:x i) :y2 (:y i)}]))
   cs))

(defn- input->svg [p]
  [:circle {:stroke "red" :fill "white" :cx (:x p) :cy (:y p) :r 5}])

(defn- port-y-offset
  [y n]
  (+ y (* 20 n)))

(defn- gate->svg [g]
  [:rect
   {:x (:x g)
    :y (:y g)
    :stroke "black"
    :stroke-width 1
    :fill "white"
    :height gate-height
    :width gate-width}])

(defn- full-gate->svg
  [g]
  (list
   (gate->svg g)
   (map input->svg (:inputs g))
   (map input->svg (:outputs g))))

(defn- label-gates
  [gates]
  (map
   (fn [g]
     [:text {:y (+ (/ gate-height 2) (:y g)) :x (:x g) :stroke "black"} (:gate g)])
   gates))

(let [gates (gates-with-coord circuit)]
  (->>
   (html
    [:html
     [:head]
     [:body
      [:div
       [:svg {:width 500 :height 500 :stroke "green"}
        (map full-gate->svg gates)
        (->>
         gates
         connections
         connections->svg)
        (label-gates gates)]]]])
   (spit "test.html")))
