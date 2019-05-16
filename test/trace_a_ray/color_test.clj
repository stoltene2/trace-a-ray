(ns trace-a-ray.color-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.color :as c]))

;;; Duplicated from tuple
(def EPSILON 0.0000001)

(defn tuple-equal? [t1 t2]
  "Compares each component of a tuple to verify it is within
  EPSILON."
  (let [components (map vector t1 t2)
        within-epsilon (fn [[x y]] (< (Math/abs (- x y)) EPSILON))]
    (every? identity (map within-epsilon components))))

;;;;;;;;;;;; END duplication


(deftest color-components
  (testing "When getting the components of a color"
    (let [r 1
          g 2
          b 3
          c (c/color r g b)]
      (is (= (c/red c) r))
      (is (= (c/green c) g))
      (is (= (c/blue c) b)))))


(deftest color-operations-for-add-and-subtract
  (testing "When adding, subtracting colors"
    (let [c1 (c/color 0.9 0.6 0.75)
          c2 (c/color 0.7 0.1 0.25)]
      (is (= (c/+ c1 c2) (c/color 1.6 0.7 1.0)))
      (is (tuple-equal? (c/- c1 c2) (c/color 0.2 0.5 0.5))))))


(deftest color-multiplication-by-scalar
  (testing "When scaling a color vector by a constant"
    (is (tuple-equal? (c/mul 2 (c/color 0.2 0.3 0.4))
           (c/color 0.4 0.6 0.8)))))


(deftest color-multiplication-by-another-color
  (testing "When multiplying a color vector by another color"
    (let [c1 (c/color 1 0.2 0.4)
          c2 (c/color 0.9 1 0.1)]
      (is (tuple-equal? (c/mul c1 c2) (c/color 0.9 0.2 0.04))))))
