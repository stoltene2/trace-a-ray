(ns trace-a-ray.ray
  (:require [clojure.core.matrix :as m]
            [trace-a-ray.tuple :as t]))

(defrecord Ray [point direction])

(defn make-ray [point direction]
  "Create a ray record with its origin point and direction"
  (->Ray point direction))

(defn position [ray time]
  "Determine the position of the ray at time t"

  (t/+ (.point ray) (t/* time (.direction ray))))


(defn transform [ray transform]
  "Transform a ray using transform to convert a ray to object space
  and back."
  (let [p (m/mmul transform (.point ray))
        dir (m/mmul transform (.direction ray))]
    (make-ray p dir)))


(defn intersect [sphere ray]
  "Given a sphere and a ray, determine if there are any
  intersections between the two."

  (let [;; ray transformed to be relative to the sphere
        obj-ray (transform ray (m/inverse (.transform sphere)))
        world-origin (t/point 0 0 0)
        ray-dir (.direction obj-ray)
        sphere->ray-v (t/- (.point obj-ray) world-origin)

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


(defn make-world []
  "Makes an empty world to track object intersections"
  {})

(defn intersection [t obj world]
  "Records an intersection at time, t, for the object, obj inside of
  the world."
  (let [sorted-insert (fn [old n] (sort (conj old n)))]
    (update world obj sorted-insert t)))

(defn intersections [obj world]
  "Return all intersections of an object in the world."
  (world obj))

(defn hit [is]
  "Returns the first non-negative intersection for a collection of
  intersections. Otherwise nil is returned."
  (->> is
       (filter pos?)
       (first)))
