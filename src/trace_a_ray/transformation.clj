(ns trace-a-ray.transformation
  (:require [clojure.core.matrix :as matrix]))

;; TODO: Where do I set this?
;;(set-current-implementation :vectorz)

(defn translate [x y z]
  "Create a matrix transformation which will move all points by x, y,
  and z."
  [[1 0 0 x]
   [0 1 0 y]
   [0 0 1 z]
   [0 0 0 1]])
