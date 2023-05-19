(ns trace-a-ray.examples.projected-circle
  (:require [trace-a-ray.sphere :as sphere]
            [trace-a-ray.tuple :as tuple]
            [trace-a-ray.canvas :as canvas]
            [trace-a-ray.ray :as ray]
            [trace-a-ray.color :as color]
            [trace-a-ray.transformation :as trans]
            [clojure.core.matrix :as m]
            [clojure.core.reducers :as r]))

(def ^:private sphere
  "Create the unit sphere in the scene."
  (sphere/make-sphere))

(def ^:private source-point
  "The point at which we are observing the 'scene'.
  It is set back -1.005 units from the origin. This is an empiracle
  number."
  (tuple/point 0 0 -1.005))

(defn source-coordinates
  "Given X and Y then return a lazy sequence of pairs [X Y] from
  [(-X, X), (-Y, Y)]"
  [x y]
  (into [] (for [x (range (- x) x)
                 y (range (- y) y)]
             [x y])))

#_{:clj-kondo/ignore [:unused-private-var]}
(def ^:private rays-from-source-to-wall
  "This is the collection of rays from the source point to each point
  on the wall."
  (let [z 10
        source source-point]
    (map #(ray/make-ray source (tuple/vector (first %) (second %) z)) (source-coordinates 250 250))))

(defn intersections-to-points-reducer
  "Get the two dimensional points projected onto the plane at z=10.

This function relies on Clojure's parallel reducers. It requires the
the source, coords, is reducible. It starts with a pair of
coordinates, constructs rays, filters for intersections, converts to
point and collects the results into a vector."
  [sphere]
  (let [vec-to-point (fn [vec] (assoc vec 3 1.0))
        source source-point
        z 10
        coords (source-coordinates 250 250)]
    (->> coords
         (r/map #(ray/make-ray source (tuple/vector (first %) (second %) z)))
         (r/filter #(seq (ray/intersect sphere %)))
         (r/map #(->> % :direction vec-to-point))
         (r/foldcat)
         (into []))))

;;;============================== Copied again
(defn translate-points-to-center
  "Given dimensions x and y of the resulting image translate points
  from origin to image center.
  POINTS are a c"

  [x y points]
  (let [find-midpoint (fn [a] (if (even? a)
                                (/ a 2)
                                (/ (dec a) 2)))
        center-x (find-midpoint x)
        center-y (find-midpoint y)]
    (m/transpose (m/mmul (trans/translate center-x center-y 0) (m/transpose points)))))

(defn in-interval
  "Returns true if lower <= n <= upper"
  [n lower upper]
  (and (>= n lower) (<= n upper)))

(defn point-to-pixel
  "Take an n-dimensional point and plot the x and y components in a rectangle bounded by
    0 <= pt_x < max-x and
    0 <= pt_y < max-y

The coordinates will be rounded to integer values."
  [_max-x max-y pt]
  (let [x (float (first pt))
        y (float (second pt))]

    [(Math/round x) (Math/round (- (dec max-y) y))]))

(defn make-ppm [ps]
  (let [max-x  500
        max-y  500
        canvas (canvas/make-canvas max-x max-y)

        points (->> ps (map #(point-to-pixel max-x max-y %))
                    (filter (fn [[x y]] (and (in-interval x 0 (dec max-x))
                                             (in-interval y 0 (dec max-y))))))
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

(defn intersections-to-ppm
  "This is the main entry point here"
  []
  (make-ppm (translate-points-to-center 500 500 (intersections-to-points-reducer sphere))))

;; Write file with
(comment
  (spit "/tmp/sphere.ppm" (intersections-to-ppm)))

(comment
  ;; Profiling
  (require '[clj-async-profiler.core :as prof])

  (prof/profile #_{:clj-kondo/ignore [:unresolved-namespace]}
   (user/timeit (intersections-to-ppm)))
  (prof/serve-ui 8080))
