
(ns trace-a-ray.tuple)

(defn vec? [tuple]
  "Check if we have a point or a vector."
  (== 0 (nth tuple 3)))

(defn point? [tuple]
  "Check if tuple is a point."
  (not (vec? tuple)))

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


(defn mk-point [x y z]
  "Creates a point."
  [x y z 1.0])

(defn mk-vec [x y z]
  "Creates a vector."
  [x y z 0.0])

(defn add [t1 t2]
  "Add two tuples together componentwise"
  (map + t1 t2))

(defn sub [t1 t2]
  "Add two tuples together componentwise"
  (map - t1 t2))
