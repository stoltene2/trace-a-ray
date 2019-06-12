(ns trace-a-ray.helpers)

(def ^:private EPSILON 0.0000001)

(defn tuple= [t1 t2]
  "Compares each component of a tuple to verify it is within
  EPSILON."
  (let [components (map vector t1 t2)
        within-epsilon (fn [[x y]] (< (Math/abs (- x y)) EPSILON))]
    (every? identity (map within-epsilon components))))


(defn matrix= [m1 m2]
  "Compares each component of matrices to verify they are within
  EPSILON."
  (tuple= (flatten m1) (flatten m2)))
