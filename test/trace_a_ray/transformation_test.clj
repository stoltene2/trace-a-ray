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
