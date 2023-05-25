(ns trace-a-ray.image
  (:import java.awt.Color
           java.io.File
           java.awt.image.BufferedImage
           javax.imageio.ImageIO))

(defn write-image
  "Write a test image. This creates a sample image that is 100x100. Has
  a black background and draws one red square on the image. I'm
  intending that this can take a canvas as an input and output file
  that can be consumed in clerk.

  I need to benchmark this image creation with the ppm creation."
  ;; https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/awt/image/BufferedImage.html
  [_canvas]
  (let [block 5
        width 100
        height 100
        bi (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics bi)
        out-file (File. "/tmp/out.png")]
    (.setBackground g (Color. 0 0 0))
    (.clearRect g 0 0 width height)
    (.setColor g (Color. 200 0 0))
    (.fillRect g 0 0 block block)
    (ImageIO/write bi "png" out-file)))

(defn make-ppm [_canvas])
