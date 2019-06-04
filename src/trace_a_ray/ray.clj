(ns trace-a-ray.ray
  (:require [trace-a-ray.tuple :as t]))

(defrecord ray [point direction])

(defn position [ray time]
  "Determine the position of the ray at time t"

  (t/+ (.point ray) (t/* time (.direction ray))))


(defn intersect [sphere ray]
  "Given a unit-sphere and a ray, determine if there are any
  intersections between the two."

  (let [world-origin (t/point 0 0 0)
        ray-dir (.direction ray)
        sphere->ray-v (t/- (.point ray) world-origin)

        a (t/dot ray-dir ray-dir)
        b (* 2 (t/dot ray-dir sphere->ray-v))
        c (dec (t/dot sphere->ray-v sphere->ray-v))

        discriminate (- (* b b) (* 4 a c))

        has-roots? (>= discriminate 0)]

    (if has-roots?

      (let [discriminiate-sqrt (Math/sqrt discriminate)
            two-a (* 2 a)
            t1 (/ (- (- b) discriminiate-sqrt) two-a)
            t2 (/ (+ (- b) discriminiate-sqrt) two-a)]
        (if (< t1 t2) [t1 t2] [t2 t1]))

      [])))
