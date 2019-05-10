
(ns trace-a-ray.tuple
  (:refer-clojure :rename {vector core-vector
                           vector? core-vector?
                           + core-+
                           - core--
                           * core-*}))

(defn vector? [tuple]
  "Check if we have a point or a vector. Be careful using :refer :all
  with this is it shadows core."
  (== 0 (nth tuple 3)))

(defn point? [tuple]
  "Check if tuple is a point."
  (not (vector? tuple)))

(defn x [tuple]
  "Get x-coordinate"
  (first tuple))

(defn y [tuple]
  "Get x-coordinate"
  (second tuple))

(defn z [tuple]
  "Get x-coordinate"
  (nth tuple 2))

(defn w [tuple]
  "Get w-coordinate"
  (nth tuple 3))

(defn point [x y z]
  "Creates a point."
  [x y z 1.0])

(defn vector [x y z]
  "Creates a vector."
  [x y z 0.0])

(defn + [t1 t2]
  "Add two tuples together componentwise"
  (map core-+ t1 t2))

(defn -
  "S two tuples together componentwise"
  ([t1] (map core-- t1))
  ([t1 t2] (map core-- t1 t2)))

(defn * [a v]
  "Scale a vector v by a factor of a."
  (map #(core-* a %) v))

(defn magnitude [v]
  "Determine the length or magnitude of a vector"
  (let [sqr (fn [x] (core-* x x))]
    (Math/sqrt (apply core-+ (map sqr v)))))

(defn normalize [v]
  "Normalize a non-zero vector."
  (let [mag (magnitude v)
        x1 (/ (x v) mag)
        y1 (/ (y v) mag)
        z1 (/ (z v) mag)]
    ;; I purposefully didn't scale w because it is always 0
    (vector x1 y1 z1)))
