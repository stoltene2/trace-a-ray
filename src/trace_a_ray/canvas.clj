(ns trace-a-ray.canvas
  (:require [trace-a-ray.color :as color]))

;; I should use a map here where the key is [x y] so that it is sparse
(defn canvas [x y]
  "Creates an empty canvas where all values are black by default"
  (vec (repeat y (vec (repeat x (color/color 0 0 0))))))

;; Not sure this is used
(defn pixel-at [x y c]
  (nth (nth c y) x))

;; This doesn't need to be public
(defn write-pixel [x y color canv]
  "Create a new canvas where coordinate (x, y) is set to color."
  ;; (assoc map key val)
  (assoc-in canv [y x] color))

(defn- clamp [x] (if (< x 0.0) 0.0 (if (> x 1.0) 1.0 x)))
(defn- scale [x] (Math/round (float (* x 255))))


;; canvas->ppm could exist in a new module called `trace-a-ray.ppm`
;; I can use a transient string or a string builder `with-out-string`
(defn canvas-to-ppm [c]
  "Convert a canvas to a PPM image string"
  (let y (count c)
       max-color-value 255
       row-to-ppm (fn [r] (apply str (interpose " " (map (comp scale clamp) (flatten r)))))
       ppm-rows (map row-to-ppm c)
    (apply str
           (concat (interpose "\n"
                       (concat ["P3"
                                (str x " " y)
                                max-color-value] ppm-rows)) ["\n"]))))
