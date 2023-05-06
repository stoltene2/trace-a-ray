(ns trace-a-ray.sphere
  (:require [clojure.core.matrix :as m]))

(defrecord Sphere [transform inverse-transform])

(defn make-sphere [& {:keys [transform] :or {transform (m/identity-matrix 4)}}]
  "Create a sphere record. If a transform is not supplied the identity matrix is used by default."
  (->Sphere transform (m/inverse transform)))
