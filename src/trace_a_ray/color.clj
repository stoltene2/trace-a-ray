(ns trace-a-ray.color
  (:refer-clojure :rename {+ core-+
                           - core--
                           * core-*}))

(defn color [r g b]
  [(double r) (double g) (double b)])

(defn red
  "The red component of a color"
  [[r _ _]]
  r)

(defn green
  "The green component of a color"
  [[_ g _]]
  g)

(defn blue
  "The blue component of a color"
  [[_ _ b]]
  b)

;; I should create a protocol here
(defn +
  "Add two colors together"
  [[r1 g1 b1] [r2 g2 b2]]
  (color (core-+ r1 r2) (core-+ g1 g2) (core-+ b1 b2)))

(defn -
  "Subtract two colors"
  [[r1 g1 b1] [r2 g2 b2]]
  (color (core-- r1 r2) (core-- g1 g2) (core-- b1 b2)))

(defmulti mul
  "Either scale a color or perform a Hadamard multiplication (componentwise) if two colors are given."
  (fn [a b] (if (and (vector? a) (vector? b))
              :hadamard
              :scalar)))

(defmethod mul :hadamard [[r1 g1 b1] [r2 g2 b2]]
  [(core-* r1 r2) (core-* g1 g2) (core-* b1 b2)])

(defmethod mul :scalar [n [r g b]]
  [(core-* n r) (core-* n g) (core-* n b)])
