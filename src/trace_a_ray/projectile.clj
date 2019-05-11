(ns trace-a-ray.projectile
  (:require [trace-a-ray.tuple :as t]))


(defn projectile [position velocity]
  "Represent a projectile.
POSITION is a point and VELOCITY is a vector."
  [position velocity])

(defn y-pos [[point _]]
  "Extract y coordinate from the point part of the projectile"
  (t/y point))

(defn tick [env [proj-pos proj-vel :as projectile]]
  (let [position (t/+ proj-pos proj-vel)
        velocity (t/+ (t/+ proj-vel (:gravity env))
                      (:wind env))]
    [position velocity]))


(defn run-it []
  "Execute tick repeatedly until projectile hits the ground (-y value).
The environment has an effect from gravity and wind."
  (let [env    {:gravity (t/vector 0 -0.1 0) :wind (t/vector -0.1, 0, 0)}
        p-init [(t/point 0 1 0) (t/normalize (t/vector 1 3 0))]]
    (loop [tick-count            0
           p p-init]
      (if (< (y-pos p) 0)
        [tick-count (first p)]
        (recur (inc tick-count) (tick env p))))))
