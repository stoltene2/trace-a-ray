(ns trace-a-ray.helpers)

(def ^:private EPSILON 0.0000001)

(defn tuple=
  "Compares each component of a tuple to verify it is within
  EPSILON."
  [t1 t2]
  (let [components (map vector t1 t2)
        within-epsilon (fn [[x y]] (< (Math/abs (- x y)) EPSILON))]
    (every? identity (map within-epsilon components))))


(defn matrix=
  "Compares each component of matrices to verify they are within
  EPSILON."
  [m1 m2]
  (tuple= (flatten m1) (flatten m2)))
