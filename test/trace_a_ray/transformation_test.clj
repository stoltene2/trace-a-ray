(ns trace-a-ray.transformation-test
  (:require [clojure.test :refer :all]
            [trace-a-ray.transformation :as transform]
            [trace-a-ray.tuple :as tuple]
            [clojure.core.matrix :as m]))

(def EPSILON 0.0000001)

(defn matrix= [m1 m2]
  "Compares each component of a  to verify it is within
  EPSILON."
  (let [components (map vector (flatten m1) (flatten m2))
        within-epsilon (fn [[x y]] (< (Math/abs (- x y)) EPSILON))]
    (every? identity (map within-epsilon components))))


(deftest translate-a-point
  (testing "When we want to translate a point"
    (let [trans (transform/translate 5 -3 2)
          p (tuple/point -3 4 5)]
      (is (matrix= (m/mmul trans p) (tuple/point 2 1 7)) "Points are moved by translation")))

  (testing "When we want to translate a point back"
    (let [trans (transform/translate 5 -3 2)
          inverse-trans (m/inverse trans)
          initial-p (tuple/point -3 4 5)
          moved-p (m/mmul trans initial-p)
          expected-p (tuple/point -3 4 5)]
      (is (matrix= (m/mmul inverse-trans moved-p) expected-p) "Points are moved by translation")))

  (testing "When translating a vector it is unaffected by the transform operation."
    (let [v (tuple/vector -3 4 5)
          trans (transform/translate 5 -3 2)]
      (is (matrix= (m/mmul trans v) v)))))

(deftest scaling
  (testing "When scaling a point"
    (let [trans (transform/scale 2 3 4)
          p (tuple/point -4 6 8)]
      (is (matrix= (m/mmul trans p) (tuple/point -8 18 32)) "Components are multiplied")))

  (testing "When shrinking a point we can use the inverse"
    (let [inverse-trans (m/inverse (transform/scale 2 3 4))
          p (tuple/point -4 6 8)]
      (is (matrix= (m/mmul inverse-trans p) (tuple/point -2 2 2)) "Components are shrunk")))

  (testing "When reflecting a point we can use negative values"
    (let [trans (m/inverse (transform/scale -1 1 1))
          p (tuple/point -4 6 8)]
      (is (matrix= (m/mmul trans p) (tuple/point 4 6 8)) "x-component flips")))

  (testing "When scaling a vector"
    (let [trans (transform/scale 2 3 4)
          v (tuple/vector -4 6 8)]
      (is (matrix= (m/mmul trans v) (tuple/vector -8 18 32)) "Vectors are scaled too"))))
