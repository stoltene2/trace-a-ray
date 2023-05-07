(ns trace-a-ray.examples.projected-circle
  (:require [trace-a-ray.sphere :as sphere]
            [trace-a-ray.tuple :as tuple]
            [trace-a-ray.canvas :as canvas]
            [trace-a-ray.ray :as ray]
            [trace-a-ray.color :as color]
            [trace-a-ray.transformation :as trans]
            [clojure.core.matrix :as m]))


(def ^:private sphere
  "Create the unit sphere in the scene."
  (sphere/make-sphere))

(def ^:private source-point
  "The point at which we are observing the 'scene'.
  It is set back -1.005 units from the origin. This is an empiracle
  number."
  (tuple/point 0 0 -1.005))

(def ^:private rays-from-source-to-wall
  "This is the collection of rays from the source point to each point
  on the wall."
  (let [z 10
        source source-point]
    (for [x (range -250 250) y (range -250 250)]
      (ray/make-ray source (tuple/vector x y z)))))

(defn intersections [rays sphere]
  "Create a list of rays which intersect the sphere"
  (filter (fn [ray] (not (empty? (ray/intersect sphere ray)))) rays))


;; I think that filtering and mapping could be consolidated to one reduce.
;; updating the vector here could be expensive...
(defn intersections-to-points [rays sphere]
  "Get the two dimensional points projected onto the plane at z=10"
  (let [vec-to-point (fn [vec] (assoc vec 3 1.0))] ; instead of
                                                   ; finding
                                                   ; intersections
                                                   ; with plane I'm
                                                   ; cheating here and
                                                   ; just projecting
                                                   ; the vector to a
                                                   ; point by changing
                                                   ; the last 0 to 1.
    (map #(->> % :direction vec-to-point)
         (filter (fn [ray]
                   (not (empty? (ray/intersect sphere ray))))
                 rays))))



;;;============================== Copied again
(defn translate-points-to-center [x y points]
  "Given dimensions x and y of the resulting image translate points
  from origin to image center.
  POINTS are a c"
  (let [find-midpoint (fn [a] (if (even? a)
                                (/ a 2)
                                (/ (dec a) 2)))
        center-x (find-midpoint x)
        center-y (find-midpoint y)]
    (m/transpose (m/mmul (trans/translate center-x center-y 0) (m/transpose points)))))


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
  (let [max-x  500
        max-y  500
        canvas (canvas/canvas max-x max-y)

        points (filter (fn [[x y]] (and (in-interval x 0 (dec max-x))
                                        (in-interval y 0 (dec max-y)))) (map (partial point-to-pixel max-x max-y) ps))
        white  (color/color 255 255 255)]
    (loop [canvas canvas
           points points
           p      (first points)]
      (if (empty? points)
        (canvas/canvas-to-ppm canvas)
        (recur (canvas/write-pixel (first p) (second p) white canvas)
               (rest points)
               ((comp first rest) points))))))



;;;============================== Copied again

(defn intersections-to-ppm []
  "Convert the intersections to a ppm file"
  (make-ppm (translate-points-to-center 500 500 (intersections-to-points rays-from-source-to-wall sphere))))



;; Write file with
(comment
  (spit "/tmp/sphere.ppm" (intersections-to-ppm)))
