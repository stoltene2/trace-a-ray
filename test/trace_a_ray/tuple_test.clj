
(ns trace-a-ray.tuple-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.tuple :as t]))

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
      (is (not (t/vec? p)) "Should not be a vector")
      (is (= (t/x p) 4.3))
      (is (= (t/y p) -4.2))
      (is (= (t/z p) 3.1))
      (is (= (t/w p) 1.0)))))

(deftest tuple-as-vector
  (testing "When tuple is a vector"
    (let [p [4.3, -4.2, 3.1, 0.0]]
      (is (not (t/point? p)) "Should not be a point")
      (is (t/vec? p) "Should be a vector")
      (is (t/x p) 4.3)
      (is (t/y p) -4.2)
      (is (t/z p) 3.1)
      (is (t/w p) 0.0))))

(deftest make-a-point
  (testing "When we create a point"
    (is (= (t/mk-point 1 2 3) [1 2 3 1.0]))))


(deftest make-a-vec
  (testing "When we create a point"
    (is (= (t/mk-vec 1 2 3) [1 2 3 0.0]))))


(deftest add-tuples
  (testing "When adding tuples together"
    (is
     (tuple-equal? (t/add [3 -2 5 1] [-2 3 1 0]) [1, 1, 6, 1]))))


(deftest sub-tuples
  (testing "When adding tuples together"
    (let [p1 (t/mk-point 3  2  1)
          p2 (t/mk-point 5  6  7)
          v  (t/mk-vec  -2 -4 -6)]
      (is (tuple-equal? (t/sub p1 p2) v))
      (is (t/vec? (t/sub p1 p2)) "Subtracting points yields a vector"))))
