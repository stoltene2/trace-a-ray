(ns trace-a-ray.examples.projectile
  (:require [trace-a-ray.tuple :as t]
            [trace-a-ray.canvas :as canvas]
            [trace-a-ray.color :as color]))

(defn y-pos
  "Extract y coordinate from the point part of the projectile"
  [[point _]]
  (t/y point))

(defn tick [env [proj-pos proj-vel]]
  (let [position (t/+ proj-pos proj-vel)
        velocity (t/+ (t/+ proj-vel (:gravity env))
                      (:wind env))]
    [position velocity]))

(defn run-it
  "Execute tick repeatedly until projectile hits the ground (-y value).
The environment has an effect from gravity and wind."

  []
  (let [env    {:gravity (t/vector 0 -0.01 0) :wind (t/vector -0.004, 0, 0)}
        arbitrary-scale 2.1
        starting-point (t/point 0 0 0)
        starting-velocity (t/* arbitrary-scale (t/normalize (t/vector 20 30 0)))
        p-init [starting-point starting-velocity]]
    (loop [tick-count 0
           p p-init
           points []]
      (if (< (y-pos p) 0)
        [tick-count (first p) points]
        (recur (inc tick-count) (tick env p) (conj points (first p)))))))

(defn in-interval
  "Returns true if lower <= n <= upper"
  [n lower upper]
  (and (>= n lower) (<= n upper)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn make-ppm

  "This is the magic that makes it go"
  []
  (let [max-x 200
        max-y 200
        canvas (canvas/make-canvas max-x max-y)
        point-to-pixel (fn [x]
                         [(Math/round (float (first x)))
                          (Math/round (float (- (dec max-y) (second x))))])

        points (filter (fn [[x y]] (and (in-interval x 0 (dec max-x))
                                        (in-interval y 0 (dec max-y)))) (map point-to-pixel (nth (run-it) 2)))
        red (color/color 255 0 0)]
    (loop [canvas canvas
           points (rest points)
           p (first points)]
      (if (empty? points)
        (canvas/canvas-to-ppm canvas)
        (recur (canvas/write-pixel (first p) (second p) red canvas)
               (rest points)
               ((comp first rest) points))))))
