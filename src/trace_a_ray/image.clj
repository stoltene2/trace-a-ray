(ns trace-a-ray.image
  (:import java.awt.Color
           java.awt.image.BufferedImage))

(defn make-buffered-image
  "Create a Java BufferedImage from a canvas of points"
  [canvas]
  (let [width (:x canvas)
        height (:y canvas)
        points (:points canvas)
        bi (BufferedImage. width height BufferedImage/TYPE_INT_RGB)
        g (.createGraphics bi)]
    (.setBackground g (Color/black))
    (.clearRect g 0 0 width height)
    (doseq [[[x y] [r g b]] points]
      (.setRGB bi x y (.getRGB (Color. (int r) (int g) (int b)))))
    bi))

(comment
  (import javax.imageio.ImageIO)
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (ImageIO/write bi "png" "/tmp/some/out/file.png"))
