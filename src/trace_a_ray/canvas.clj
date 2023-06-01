(ns trace-a-ray.canvas)

(defn make-canvas
  "Make a 1d vector canvas"
  [x y]
  {:x x
   :y y
   :points {}})

(defn pixel-at [x y canv]
  (let [points (:points canv)]
    (get points [x y])))

(defn write-pixel
  "Create a new canvas where coordinate (x, y) is set to color."
  [x y color canv]
  (let [points (:points canv)]
    (merge canv {:points (assoc points [x y] color)})))
