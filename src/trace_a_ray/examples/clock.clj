(ns trace-a-ray.examples.clock
  (:require [trace-a-ray.transformation :as trans]
            [trace-a-ray.tuple :as tuple]
            [clojure.core.matrix :as m]
            [trace-a-ray.canvas :as canvas]
            [trace-a-ray.color :as color]))


(def ^:private twelve-o-clock
  "The top-most point on a clock. Positioned 30 units above the origin."
  (let [p (tuple/point 0 0 0)]
    (m/mmul (trans/translate 0 30 0) p)))

(def ^:private angles-for-hours
  "These are angles in radians for each hour in a clock.
  For example,
    [0*PI/6 1*PI/6 2*PI/6 ... 11*PI/6]"
  (map * (range 12) (repeat (/ Math/PI 6.0))))


(def ^:private clock-points
  "A collection of points representing the hours of a clock centered
  around the origin."
  (let [transforms (map trans/rotate-z angles-for-hours)
        points (repeat twelve-o-clock)]
    (map m/mmul transforms points)))

(def ^:private spiral-points
  "A collection of points around a clock where each point is scaled by
  its angle. This produces a crude spiral."
  (let [scale-transforms (map #(trans/scale (/ % 3) (/ % 3) 0) angles-for-hours)
        rotate-transforms (map trans/rotate-z angles-for-hours)
        points (repeat twelve-o-clock)]
    (map m/mmul scale-transforms rotate-transforms points)))

(defn translate-points-to-center [x y points]
  "Given dimensions x and y of the resulting image translate points
  from origin to image center.
  POINTS are a c"
  (let [find-midpoint (fn [a] (if (even? a)
                                  (dec (/ a 2))
                                  (dec (/ (dec a) 2))))
        center-x (find-midpoint x)
        center-y (find-midpoint y)]
    (m/transpose (m/mmul (trans/translate center-x center-y 0) (m/transpose points)))))

;; Points needed for a canvas plot
(def canvas-points (translate-points-to-center 100 100 clock-points))

(def canvas-points-spiral (translate-points-to-center 100 100 spiral-points))


;; This is largely copied from projectile.clj

(defn in-interval [n lower upper]
  "Returns true if lower <= n <= upper"
  (and (>= n lower) (<= n upper)))

(defn point-to-pixel [max-x max-y pt]
  "Take an n-dimensional point and plot the x and y components in a rectangle bounded by
    0 <= pt_x < max-x and
    0 <= pt_y < max-y

The coordinates will be rounded to integer values."

  (let [x (float (first pt))
        y (float (second pt))]

       [(Math/round x) (Math/round (- (dec max-y) y))]))

(defn make-ppm [ps]
  (let [max-x 100
        max-y 100
        canvas (canvas/make-canvas max-x max-y)

        points (filter (fn [[x y]] (and (in-interval x 0 (dec max-x))
                                        (in-interval y 0 (dec max-y)))) (map (partial point-to-pixel max-x max-y) ps))
        white (color/color 255 255 255)]
    (loop [canvas canvas
           points points
           p (first points)]
      (if (empty? points)
        (canvas/canvas-to-ppm canvas)
        (recur (canvas/write-pixel (first p) (second p) white canvas)
               (rest points)
               ((comp first rest) points))))))
