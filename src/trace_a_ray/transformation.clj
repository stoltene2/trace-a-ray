(ns trace-a-ray.transformation
  (:require [clojure.core.matrix :as matrix]))

(defn translate [x y z]
  "Create a matrix transformation which will move all points by x, y,
  and z."
  [[1 0 0 x]
   [0 1 0 y]
   [0 0 1 z]
   [0 0 0 1]])


(defn scale [x y z]
  "Create a matrix transformation which will scale all points and
  vectors by x, y, and z."
  [[x 0 0 0]
   [0 y 0 0]
   [0 0 z 0]
   [0 0 0 1]])


(defn rotate-x [rad]
  "Rotate point around x-axis by rad radians."
  [[1 0              0                  0]
   [0 (Math/cos rad) (- (Math/sin rad)) 0]
   [0 (Math/sin rad) (Math/cos rad)     0]
   [0 0              0                  1]])

(defn rotate-y [rad]
  "Rotate point around y-axis by rad radians."
  [[(Math/cos rad)     0   (Math/sin rad)     0]
   [0                  1   0                  0]
   [(- (Math/sin rad)) 0   (Math/cos rad)     0]
   [0                  0   0                  1]])

(defn rotate-z [rad]
  "Rotate point around z-axis by rad radians."
  [[(Math/cos rad) (- (Math/sin rad)) 0 0]
   [(Math/sin rad) (Math/cos rad)     0 0]
   [0              0                  1 0]
   [0              0                  0 1]])
