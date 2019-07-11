(ns trace-a-ray.sphere
  (:require [clojure.core.matrix :as m]
            [trace-a-ray.tuple :as tuple]))

(defrecord Sphere [transform])

(defn make-sphere [& {:keys [transform] :or {transform (m/identity-matrix 4)}}]
  "Create a sphere record. If a transform is not supplied the identity matrix is used by default."
  (->Sphere transform))

(defn normal [sphere point]
  "Compute the normal vector of a sphere at a given point on the sphere."
  (tuple/- point (tuple/point 0 0 0)))
