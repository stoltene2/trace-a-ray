(ns trace-a-ray.canvas
  (:require [trace-a-ray.color :as color]
            [clojure.string :as str]))

;; I should use a map here where the key is [x y] so that it is sparse
(defn make-canvas [x y]
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


(defn canvas-to-ppm
  "Convert a canvas to a PPM image string. Initially, I was creating
  this string using `(apply str ...)`, which uses a StringBuilder, but
  it just isn't as clean or easy to read. The approach below captures
  println output ad puts them in a string."

  [canvas]

  (let [x               (count (first canvas))
        y               (count canvas)
        max-color-value 255
        clamp-and-scale (comp scale clamp)
        header   (fn [] (printf "P3\n%s %s\n%s\n" x y max-color-value))
        row->ppm (fn [r] (println (str/join " " (->> r flatten (map clamp-and-scale)))))
        ppm-rows (fn [] (doseq [row canvas]
                          (row->ppm row)))]

    (with-out-str (do
                    (header)
                    (ppm-rows)))))
