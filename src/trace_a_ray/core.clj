(ns trace-a-ray.core
  (:require [trace-a-ray.examples.projected-circle :as example])
  (:gen-class))

(defn -main
  "I print out a projected circle"
  [& args]
  (do
    (println "Generating /tmp/sphere.ppm")
    (time (spit "/tmp/sphere.ppm" (example/intersections-to-ppm)))))
