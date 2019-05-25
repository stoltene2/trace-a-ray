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

      (is (matrix= (m/mmul trans p) (tuple/point 2 1 7))
          "Points are moved by translation")))

  (testing "When we want to translate a point back"
    (let [trans (transform/translate 5 -3 2)
          inverse-trans (m/inverse trans)
          initial-p (tuple/point -3 4 5)
          moved-p (m/mmul trans initial-p)
          expected-p (tuple/point -3 4 5)]

      (is (matrix= (m/mmul inverse-trans moved-p) expected-p)
          "Points are moved by translation")))

  (testing "When translating a vector it is unaffected by the transform operation."
    (let [v (tuple/vector -3 4 5)
          trans (transform/translate 5 -3 2)]
      (is (matrix= (m/mmul trans v) v)))))

(deftest scaling
  (testing "When scaling a point"
    (let [trans (transform/scale 2 3 4)
          p (tuple/point -4 6 8)]

      (is (matrix= (m/mmul trans p) (tuple/point -8 18 32))
          "Components are multiplied")))

  (testing "When shrinking a point we can use the inverse"
    (let [inverse-trans (m/inverse (transform/scale 2 3 4))
          p (tuple/point -4 6 8)]

      (is (matrix= (m/mmul inverse-trans p) (tuple/point -2 2 2))
          "Components are shrunk")))

  (testing "When reflecting a point we can use negative values"
    (let [trans (m/inverse (transform/scale -1 1 1))
          p (tuple/point -4 6 8)]

      (is (matrix= (m/mmul trans p) (tuple/point 4 6 8))
          "x-component flips")))

  (testing "When scaling a vector"
    (let [trans (transform/scale 2 3 4)
          v (tuple/vector -4 6 8)]

      (is (matrix= (m/mmul trans v) (tuple/vector -8 18 32))
          "Vectors are scaled too"))))



;;
;;                  |+y
;;                  |
;;                  |
;;                  |
;;                  |
;;                  |
;;                  |
;;                  |
;;                  o------------- +z
;;                   \
;;                    \
;;                     \
;;                      \
;;                       \
;;                        \
;;                          +x
;;
;;

(deftest rotating
  (testing "When rotating around the x-axis"
    (let [original-p (tuple/point 0 1 0)
          sqrt2-div-2 (/ (Math/sqrt 2) 2)
          expected (tuple/point 0.0 sqrt2-div-2 sqrt2-div-2)
          rot-45-deg (transform/rotate-x (/ Math/PI 4))
          rot-90-deg (transform/rotate-x (/ Math/PI 2))]

      (is (matrix= expected (m/mmul rot-45-deg original-p))
          "Rotate by 45-deg")

      (is (matrix= original-p (m/mmul (m/inverse rot-45-deg) expected))
          "Rotate back by 45-deg")

      (is (matrix= (tuple/point 0 0 1) (m/mmul rot-90-deg original-p))
          "Rotate by 90-deg, y falls onto z.")))

  (testing "When rotating around the y-axis"
    (let [original-p (tuple/point 0 0 1)
          sqrt2-div-2 (/ (Math/sqrt 2) 2)
          expected (tuple/point sqrt2-div-2 0.0 sqrt2-div-2)
          rot-45-deg (transform/rotate-y (/ Math/PI 4))
          rot-90-deg (transform/rotate-y (/ Math/PI 2))]

      (is (matrix= expected (m/mmul rot-45-deg original-p))
          "Rotate 45 degrees from x to z.")

      (is (matrix= original-p (m/mmul (m/inverse rot-45-deg) expected))
          "Rotate 45 degrees back from z to x.")

      (is (matrix= (tuple/point 1 0 0) (m/mmul rot-90-deg original-p))
          "Rotate by 90-deg, z falls to x")))

  (testing "When rotating around the z-axis"
    (let [original-p (tuple/point 0 1 0)
          sqrt2-div-2 (/ (Math/sqrt 2) 2)
          expected (tuple/point (- sqrt2-div-2) sqrt2-div-2 0.0)
          rot-45-deg (transform/rotate-z (/ Math/PI 4))
          rot-90-deg (transform/rotate-z (/ Math/PI 2))]

      (is (matrix= expected (m/mmul rot-45-deg original-p))
          "Rotate 45 degrees from y onto -x.")

      (is (matrix= original-p (m/mmul (m/inverse rot-45-deg) expected))
          "Rotate 45 degrees from -x towards y.")

      (is (matrix= (tuple/point -1 0 0) (m/mmul rot-90-deg original-p))
          "Rotate by 90-deg, y falls to -x because of right hand rule."))))
