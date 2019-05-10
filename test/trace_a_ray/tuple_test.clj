
(ns trace-a-ray.tuple-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.tuple :as t]
            [clojure.test.check :as tc]))

(def EPSILON 0.0000001)

(defn tuple-equal? [t1 t2]
  "Compares each component of a tuple to verify it is within
  EPSILON."
  (let [components (map vector t1 t2)
        within-epsilon (fn [[x y]] (< (Math/abs (- x y)) EPSILON))]
    (every? identity (map within-epsilon components))))

(deftest tuple-as-point
  (testing "When tuple is a point"
    (let [p [4.3 -4.2 3.1 1.0]]
      (is (t/point? p) "Should be a point")
      (is (not (t/vector? p)) "Should not be a vector")
      (is (= (t/x p) 4.3))
      (is (= (t/y p) -4.2))
      (is (= (t/z p) 3.1))
      (is (= (t/w p) 1.0)))))

(deftest tuple-as-vector
  (testing "When tuple is a vector"
    (let [p [4.3, -4.2, 3.1, 0.0]]
      (is (not (t/point? p)) "Should not be a point")
      (is (t/vector? p) "Should be a vector")
      (is (t/x p) 4.3)
      (is (t/y p) -4.2)
      (is (t/z p) 3.1)
      (is (t/w p) 0.0))))

(deftest make-a-point
  (testing "When we create a point"
    (is (= (t/point 1 2 3) [1 2 3 1.0]))))


(deftest make-a-vec
  (testing "When we create a point"
    (is (= (t/vector 1 2 3) [1 2 3 0.0]))))


(deftest add-tuples
  (testing "When adding tuples together"
    (is
     (tuple-equal? (t/+ [3 -2 5 1] [-2 3 1 0]) [1, 1, 6, 1]))))

(deftest subtracting-tuples
  (testing "When adding tuples together"
    (let [p1 (t/point   3  2  1)
          p2 (t/point   5  6  7)
          v  (t/vector -2 -4 -6)]
      (is (tuple-equal? (t/- p1 p2) v))
      (is (t/vector? (t/- p1 p2)) "Subtracting points yields a vector")))

  (testing "When subtracting a vector from a point"
    (let [p1 (t/point  3  2  1)
          v1 (t/vector 5  6  7)
          p  (t/point -2 -4 -6)]
      (is (= (t/- p1 v1) p) "Point minus vector is point")
      (is (t/point? (t/- p1 v1)) "Point minus vector is point")))

  (testing "When subtracting two vectors we get another vector"
    (let [v1 (t/vector  3  2  1)
          v2 (t/vector 5  6  7)
          v  (t/vector -2 -4 -6)]
      (is (= (t/- v1 v2) v) "Point minus vector is point")
      (is (t/vector? (t/- v1 v2)) "Point minus vector is point"))))

(deftest negating
  (testing "When negating a vector"
    (let [zero (t/vector 0 0 0)
          v    (t/vector 1 -2 3)]
      (is (= (t/- v) (t/- zero v))))))

(deftest scaling
  (testing "When multiplying a vector by a scalar"
    (is (= (t/* 2 (t/vector 1 1 1)) (t/vector 2 2 2)))
    (is (= (t/* 0.5 (t/vector 1 1 1)) (t/vector 0.5 0.5 0.5)))
    (is (= (t/* 0.5 (t/vector 2 3 4)) (t/vector 1.0 1.5 2.0)))))


(deftest magnitude
  (testing "When determining the magnitude of a vector"
    (is (= (t/magnitude (t/vector 1 0 0)) 1.0))
    (is (= (t/magnitude (t/vector 0 1 0)) 1.0))
    (is (= (t/magnitude (t/vector 0 0 1)) 1.0))
    (is (= (t/magnitude (t/vector 1 2 3)) (Math/sqrt 14)))))


(deftest normalizing
  (testing "When finding the normal of a vector"
    (let [sqrt-14 (Math/sqrt 14)]
      (is (= (t/normalize (t/vector 1 0 0)) (t/vector 1.0 0.0 0.0)))

      (is (tuple-equal?
           (t/normalize (t/vector 1 2 3))
           (t/vector (/ 1 sqrt-14) (/ 2 sqrt-14) (/ 3 sqrt-14))))

      (is (= (t/magnitude (t/normalize (t/vector 1 2 3))) 1.0)
          "The magnitude of a normalized vector is 1.0"))))


(deftest dot-product
  (testing "When finding the dot product of two vectors"
    (is (= (t/dot (t/vector 1 2 3) (t/vector 2 3 4)) 20.0))

    (is (= (t/dot (t/normalize (t/vector 1 2 3))
                  (t/normalize (t/vector 1 2 3)))
           1.0)
        "Normalized vectors are identical")

    (is (= (t/dot (t/normalize (t/vector 1 2 3))
                  (t/normalize (t/- (t/vector 1 2 3))))
           -1.0)
        "Normalized opposite vectors are negative")

    (is (= (t/dot (t/vector 1 0 0) (t/vector 0 1 0)) 0.0)
        "Vectors are perpendicular")

    (is (= (t/dot (t/vector 1 0 0) (t/vector -1 0 0)) -1.0)
        "Vectors are opposite")))


;; Clojure dislike: Horrible error messages
(deftest cross-product
  (testing "When computing the cross product"
    (let [a (t/vector 1 2 3)
          b (t/vector 2 3 4)]

      (is (= (t/cross (t/vector 1 2 3) (t/vector 2 3 4))
             (t/vector -1 2 -1)))

      (is (= (t/cross b a)
             (t/vector 1 -2 1))))

    (is (= (t/cross (t/vector 1 0 0) (t/vector 0 1 0))
           (t/vector 0 0 1)))))
