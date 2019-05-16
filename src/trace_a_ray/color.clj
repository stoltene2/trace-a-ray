(ns trace-a-ray.color
  (:require [trace-a-ray.tuple :as t])
  (:refer-clojure :rename {+ core-+
                           - core--
                           * core-*}))

(defn color [r g b]
  [r g b])

(defn red [[r _ _]]
  "The red component of a color"
  r)

(defn green [[_ g _]]
  "The green component of a color"
  g)

(defn blue [[_ _ b]]
  "The blue component of a color"
  b)

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


(defmethod mul :hadamard [c1 c2]
  (vec (map core-* c1 c2)))

(defmethod mul :scalar [n c]
  (t/* n c))
