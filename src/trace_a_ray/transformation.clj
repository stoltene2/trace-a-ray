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


(defn shearing [x_y x_z  ;Shear x in proportion to y and z
                y_x y_z  ;Shear y in proportion to x and z
                z_x z_y] ;Shear z in proportion to x and y
  "Create a transformation which scales components in proportion to
  others."
  [[  1   x_y  x_z  0]
   [ y_x   1   y_z  0]
   [ z_x  z_y   1   0]
   [  0    0    0   1]])
