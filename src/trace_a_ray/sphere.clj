(ns trace-a-ray.sphere
  (:require [clojure.core.matrix :as m]))

(defrecord Sphere [transform inverse-transform])

(defn make-sphere
  "Create a sphere record. If a transform is not supplied the identity matrix is used by default."
  [& {:keys [transform] :or {transform (m/identity-matrix 4)}}]
  (->Sphere transform (m/inverse transform)))
