(ns trace-a-ray.canvas-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.color :as color]
            [trace-a-ray.canvas :as canvas]
            [clojure.string :as str]))

(defn all-blank [c]
  "Determines that every component is mapped to 0."
  (every? #(= 0.0 %) (flatten c)))

(deftest saving-a-convas
  (testing "We can default all points on a canvas to black"
    (is (all-blank (canvas/make-canvas 10 10)))))

(deftest set-a-pixel
  (testing "We can save a pixel"
    (let [red (color/color 255 0 0)
          c   (canvas/write-pixel 2 3 red (canvas/make-canvas 10 20))]
      (is (= (canvas/pixel-at 2 3 c) red)))))

(deftest canvas-to-ppm
  (testing "Convert a canvas to ppm"
    (let [c1  (color/color 1 0 0)
          c2  (color/color 0 0.5 0)
          c3  (color/color -0.5 0 1)
          can (->>
               (canvas/make-canvas 5 3)
               (canvas/write-pixel 0 0 c1)
               (canvas/write-pixel 2 1 c2)
               (canvas/write-pixel 4 2 c3))]
      (is (= (str/join "\n"
              ["P3"
              "5 3"
              "255"
              "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0"
              "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0"
               "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255"
               ""])
             (canvas/canvas-to-ppm can))))))
