
(ns trace-a-ray.tuple
  (:refer-clojure :rename {vector core-vector
                           vector? core-vector?
                           + core-+
                           - core--
                           * core-*}))


(defmacro vector? [tuple]
  "Check if we have a point or a vector. Be careful using :refer :all
  with this is it shadows core."
  `(== 0 (nth ~tuple 3)))

(defmacro point? [tuple]
  "Check if tuple is a point."
  `(not (vector? ~tuple)))

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

(defmacro point [x y z]
  "Creates a point."
  `[(double ~x) (double ~y) (double ~z) 1.0])

(defmacro vector [x y z]
  "Creates a vector."
  `[(double ~x) (double ~y) (double ~z) 0.0])

(def world-origin
  "Center point in the world scene"
  (point 0 0 0))

(defn +
  [[x1 y1 z1 w1] [x2 y2 z2 w2]]
  [(double (core-+ x1 x2))
   (double (core-+ y1 y2))
   (double (core-+ z1 z2))
   (double (core-+ (or w1 0) (or w2 0)))])


;; TODO: Use criterion here to see if it actually matters to cast as double
(defn -
  ([t1] (map core-- t1))
  ([t1 t2] (map #(core-- ^double %1 ^double %2) t1 t2)))

(defn * [ a [x y z]]
  "Scale a vector v by a factor of a."
  (vector (core-* a x) (core-* a y) (core-* a z)))

(defn magnitude ^double [[x y z w]]
  "Determine the length or magnitude of a 3d or 4d vector"
  (Math/sqrt (core-+ (Math/pow x 2) (Math/pow y 2) (Math/pow z 2) (Math/pow (or w 0) 2)))
  ;; (Math/sqrt (core-+ (core-* x x) (core-* y y) (core-* z z) (core-* (or w 0) (or w 0))))
  )

(defn normalize [[x y z :as v]]
  "Normalize a non-zero vector."
  (let [mag (magnitude v)]
    ;; I purposefully didn't scale w because it is always 0
    (vector (/ x mag) (/ y mag) (/ z mag))))

(defn dot ^double [[x1 y1 z1 w1] [x2 y2 z2 w2]]
  "Compute the dot product of two vectors."
  (core-+ (core-* x1 x2) (core-* y1 y2) (core-* z1 z2) (core-* (or w1 0) (or w2 0))))


(defn cross [[ax ay az] [bx by bz]]
  "Compute the cross product of two vectors"
  (let [x (core-- (core-* ay bz) (core-* az by))
        y (core-- (core-* az bx) (core-* ax bz))
        z (core-- (core-* ax by) (core-* ay bx))]
    (vector x y z)))

;; This was really a pain using regular core--. Maybe I need to rethink that.
