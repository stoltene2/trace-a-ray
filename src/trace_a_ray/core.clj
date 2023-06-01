(ns trace-a-ray.core
  (:require [trace-a-ray.examples.projected-circle :as example])
  (:gen-class))

(defn -main
  "I print out a projected circle"
  [& _args]
  (println "Generating /tmp/sphere.png")
  (dotimes [_ 1]
    (time (example/intersections-to-image))))
