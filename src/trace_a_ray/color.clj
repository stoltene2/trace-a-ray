(ns trace-a-ray.color
  (:require [trace-a-ray.tuple :as t])
  (:refer-clojure :rename {+ core-+
                           - core--
                           * core-*}))

(defn color [r g b]
  [(double r) (double g) (double b)])

(defn red [[r _ _]]
  "The red component of a color"
  r)

(defn green [[_ g _]]
  "The green component of a color"
  g)

(defn blue [[_ _ b]]
  "The blue component of a color"
  b)

;; I should create a protocol here
(defn + [c1 c2]
  "Add two colors together"
  (t/+ c1 c2))

(defn - [c1 c2]
  "Subtract two colors"
  (t/- c1 c2))

(defmulti mul
  "Either scale a color or perform a Hadamard multiplication (componentwise) if two colors are given."
  (fn [a b] (if (and (vector? a) (vector? b))
              :hadamard
              :scalar)))


(defmethod mul :hadamard [[r1 g1 b1] [r2 g2 b2]]
  [(core-* r1 r2) (core-* g1 g2) (core-* b1 b2)])

(defmethod mul :scalar [n [r g b]]
  [(core-* n r) (core-* n g) (core-* n b)])
