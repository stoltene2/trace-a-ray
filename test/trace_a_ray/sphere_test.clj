(ns trace-a-ray.sphere-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.transformation :as trans]
            [trace-a-ray.sphere :as sphere]
            [trace-a-ray.ray :as r]
            [trace-a-ray.tuple :as t]
            [trace-a-ray.helpers :refer :all]))


(deftest sphere-transformations
  (testing "A sphere can have an attached transformation"
    (let [T (trans/translate 2 3 4)
          s (sphere/make-sphere :transform T)]

      (is (matrix= T (.transform s))))))

(deftest sphere-intersections
  (testing "Intersecting a scaled sphere with a ray"
    (let [r (r/make-ray (t/point 0 0 -5) (t/vector 0 0 1))
          T (trans/scale 2 2 2)
          s (sphere/make-sphere :transform T)]
      (is (tuple= [3 7] (r/intersect s r)))))

  (testing "Intersecting a translated sphere with a ray"
    (let [r (r/make-ray (t/point 0 0 -5) (t/vector 0 0 1))
          T (trans/translate 5 0 0)
          s (sphere/make-sphere :transform T)]
      (is (= [] (r/intersect s r))))))

(deftest sphere-normal-vectors
  (testing "Computing normal vectors on unit-sphere"
    (let [s (sphere/make-sphere)
          sqrt3-3 (/ (Math/sqrt 3.0) 3.0)]
      (is (tuple= (t/vector 1 0 0) (sphere/normal s (t/point 1 0 0))))
      (is (tuple= (t/vector 0 1 0) (sphere/normal s (t/point 0 1 0))))
      (is (tuple= (t/vector 0 0 1) (sphere/normal s (t/point 0 0 1))))
      (is (tuple= (t/vector sqrt3-3 sqrt3-3 sqrt3-3) (sphere/normal s (t/point sqrt3-3 sqrt3-3 sqrt3-3))))))

  (testing "Normal is a normalized vector"
    (let [s (sphere/make-sphere)
          sqrt3-3 (/ (Math/sqrt 3.0) 3.0)]
      (is (tuple= (t/normalize (t/vector sqrt3-3 sqrt3-3 sqrt3-3))
                  (sphere/normal s (t/point sqrt3-3 sqrt3-3 sqrt3-3)))))))
