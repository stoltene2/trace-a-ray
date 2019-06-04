(ns trace-a-ray.ray
  (:require [trace-a-ray.tuple :as t]))

(defrecord ray [point direction])

(defn position [ray time]
  "Determine the position of the ray at time t"

  (t/+ (.point ray) (t/* time (.direction ray))))
