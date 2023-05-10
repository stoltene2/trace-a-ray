
(ns trace-a-ray.tuple
  (:refer-clojure :rename {vector core-vector
                           vector? core-vector?
                           + core-+
                           - core--
                           * core-*})
  (:require [clojure.core.matrix :as m]))

(comment
  (require '[clojure.core.matrix :as m])
  (m/set-current-implementation :vectorz)

  ;; "Elapsed time: 1152.9354 msecs"
  (time (dotimes [_ 1000000]
          (m/dot (m/matrix [(rand) (rand) (rand) (rand)])
                 (m/matrix [(rand) (rand) (rand) (rand)]))))


  ;; "Elapsed time: 208.147632 msecs"
  (time (dotimes [_ 1000000]
          (dot (vector (rand) (rand) (rand))
                 (vector (rand) (rand) (rand)))))
  )


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
  (m/mget tuple 2))

(defn w [tuple]
  "Get w-coordinate"
  (nth tuple 3))

(defn point [x y z]
  "Creates a point."
  (m/matrix [(double x) (double y) (double z) 1.0]))

(defn vector [x y z]
  "Creates a vector."
  (m/matrix [(double x) (double y) (double z) 0.0]))

(def world-origin
  "Center point in the world scene"
  (point 0 0 0))


;; Where are there no tests for this?
(defn +
  "Add two tuples together componentwise"
  [v1 v2]
  (m/add v1 v2))



(comment
  (defn apoint
    "Create a point based on arrays"
    [x y z]
    (double-array 4 [(double x) (double y) (double z) 1.0]))

  (defn avector
    "Create a point based on arrays"
    [x y z]
    (double-array 4 [(double x) (double y) (double z) 1.0]))

  (def p1 (avector 1 2 3))
  (def p2 (avector 2 3 4))
  (def vp1  (vec [1 2 3 5]))
  (def vp2  (vec [2 3 4 6]))

  (defn plus
    "Add two tuples together componentwise"
    [v1 v2]
    (map + [(rand) (rand) (rand) (rand)] [(rand) (rand) (rand) (rand)]))

  (defn aplus
    "Add two tuples together componentwise"
    ^doubles [^doubles v1 ^doubles v2]
    (double-array 4 [(+ (rand)  (rand))
                     (+ (rand)  (rand))
                     (+ (rand)  (rand))
                     (+ (rand)  (rand))]))

  (let [n 25000000]
    (println "p1" (type p1))
    (println "p2" (type p2))
    (time (dotimes [_ n] (aplus p1 p2)))

    (println "vp1" (type vp1))
    (println "vp2" (type vp2))

    (time (dotimes [_ n] (plus vp1 vp2))))
  )

(defn -
  "Subtract two tuples together componentwise"
  ([t1] (map core-- t1))
  ([t1 t2] (map core-- t1 t2)))

(defn * [a v]
  "Scale a vector v by a factor of a."
  (vector (core-* a (x v)) (core-* a (y v)) (core-* a (z v))))

(defn magnitude [v]
  "Determine the length or magnitude of a 3d or 4d vector"
  (m/length v))

(defn normalize [v]
  "Normalize a non-zero vector."
  (m/normalise v))

(defn dot [v1 v2]
  "Compute the dot product of two vectors."
  (m/dot v1 v2))


(defn cross [v1 v2]
  "Compute the cross product of two vectors."
  ;; Cross product is only defined for 3d vectors. The resulting
  ;; vector is also 3d. We need to reshape it so that there is a 4th
  ;; dimension to indicate a vector.
  (let [v1-3d (m/subvector v1 0 3)
        v2-3d (m/subvector v2 0 3)]
    (m/reshape (m/cross v1-3d v2-3d) [4])))

;; This was really a pain using regular core--. Maybe I need to rethink that.
