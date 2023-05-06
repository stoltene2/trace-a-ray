(ns trace-a-ray.color-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.color :as c]
            [trace-a-ray.helpers :refer :all]))

(deftest color-components
  (testing "When getting the components of a color"
    (let [r 1.0
          g 2.0
          b 3.0
          c (c/color r g b)]
      (is (= (c/red c) r))
      (is (= (c/green c) g))
      (is (= (c/blue c) b)))))


(deftest color-operations-for-add-and-subtract
  (testing "When adding, subtracting colors"
    (let [c1 (c/color 0.9 0.6 0.75)
          c2 (c/color 0.7 0.1 0.25)]
      (is (= (c/+ c1 c2) (c/color 1.6 0.7 1.0)))
      (is (tuple= (c/- c1 c2) (c/color 0.2 0.5 0.5))))))


(deftest color-multiplication-by-scalar
  (testing "When scaling a color vector by a constant"
    (is (tuple= (c/mul 2 (c/color 0.2 0.3 0.4))
           (c/color 0.4 0.6 0.8)))))


(deftest color-multiplication-by-another-color
  (testing "When multiplying a color vector by another color"
    (let [c1 (c/color 1 0.2 0.4)
          c2 (c/color 0.9 1 0.1)]
      (is (tuple= (c/mul c1 c2) (c/color 0.9 0.2 0.04))))))
