(ns trace-a-ray.ray
  (:require [clojure.core.matrix :as m]
            [trace-a-ray.tuple :as t]
            [trace-a-ray.sphere])

  ;; Since this is a java class you need to lowercase the dashes
  ;; https://danielcompton.net/requiring-records-clojure-clojurescript
  (:import [trace_a_ray.sphere Sphere]))

(defrecord Ray [point direction])

(defn make-ray
  "Create a ray record with its origin point and direction"
  [point direction]
  (->Ray point direction))

(defn position
  "Determine the position of the ray at time t"
  [^Ray ray time]
  (t/+ (.point ray) (t/* time (.direction ray))))

(defn transform
  "Transform a ray using transform to convert a ray to object space
  and back."

  [^Ray ray transform]
  (let [p (m/mmul transform (.point ray))
        dir (m/mmul transform (.direction ray))]
    (make-ray p dir)))

;; Not sure how to typehint sphere here
(defn intersect
  "Given a sphere and a ray, determine if there are any
  intersections between the two."

  [^Sphere sphere ^Ray ray]
  (let [;; ray transformed to be relative to the sphere
        obj-ray ^Ray (transform ray (.inverse-transform sphere))
        ray-dir (.direction obj-ray)
        sphere->ray-v (t/- (.point obj-ray) t/world-origin)

        a (double (t/dot ray-dir ray-dir))
        b (* 2 (double (t/dot ray-dir sphere->ray-v)))
        c (dec (double (t/dot sphere->ray-v sphere->ray-v)))

        discriminate (double (- (* b b) (* 4 a c)))

        has-roots? (>= discriminate 0)]

    (if has-roots?

      (let [discriminiate-sqrt (Math/sqrt discriminate)
            two-a (double (* 2 a))
            neg-b (double (- b))
            t1 (double (/ (double (- neg-b discriminiate-sqrt)) two-a))
            t2 (double (/ (double (+ neg-b discriminiate-sqrt)) two-a))]
        (if (< t1 t2) [t1 t2] [t2 t1]))

      [])))

(defn make-world
  "Makes an empty world to track object intersections"
  []
  {})

(defn intersection
  "Records an intersection at time, t, for the object, obj inside of
  the world."

  [t obj world]
  (let [sorted-insert (fn [old n] (sort (conj old n)))]
    (update world obj sorted-insert t)))

(defn intersections
  "Return all intersections of an object in the world."
  [obj world]
  (world obj))

(defn hit
  "Returns the first non-negative intersection for a collection of
  intersections. Otherwise nil is returned."
  [is]
  (->> is
       (filter pos?)
       (first)))

(comment

  (require '[trace-a-ray.ray :as ray] :reload)
  #_{:clj-kondo/ignore [:duplicate-require]}
  (require '[trace-a-ray.sphere :as sphere] :reload)
  #_{:clj-kondo/ignore [:duplicate-require]}
  (require '[trace-a-ray.ray :as ray] :reload)
  #_{:clj-kondo/ignore [:duplicate-require]}
  (require '[trace-a-ray.tuple :as tuple] :reload)
  (time (dotimes [_ 100000]
          (let [s   (sphere/make-sphere)
                p   (tuple/point 0 0 -5)
                dir (tuple/vector 0 0 1)
                r   (ray/make-ray p dir)]
            (ray/intersect s r)))))
