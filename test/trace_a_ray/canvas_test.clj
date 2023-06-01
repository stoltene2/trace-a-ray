(ns trace-a-ray.canvas-test
  (:require [clojure.test :refer [deftest is testing]]
            [trace-a-ray.color :as color]
            [trace-a-ray.canvas :as canvas]))

(defn all-blank
  "Determines that every component is mapped to 0."
  [c]
  (every? #(= 0.0 %) (flatten c)))

(deftest saving-a-convas
  (testing "We can default all points on a canvas to black"
    (is (all-blank (canvas/make-canvas 10 10)))))

(deftest set-a-pixel
  (testing "We can save a pixel"
    (let [red (color/color 255 0 0)
          c   (canvas/write-pixel 2 3 red (canvas/make-canvas 10 20))]
      (is (= red (canvas/pixel-at 2 3 c))))))
