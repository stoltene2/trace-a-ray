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


(deftest shearing
  (let [x-by-y (transform/shearing 1 0 0 0 0 0)
        x-by-z (transform/shearing 0 1 0 0 0 0)
        y-by-x (transform/shearing 0 0 1 0 0 0)
        y-by-z (transform/shearing 0 0 0 1 0 0)
        z-by-x (transform/shearing 0 0 0 0 1 0)
        z-by-y (transform/shearing 0 0 0 0 0 1)
        p (tuple/point 2 3 4)]

    (testing "Moving components in proportion to each other"
      (is (matrix= (tuple/point 5 3 4) (m/mmul x-by-y p))
          "Multiple of y added back to x")

      (is (matrix= (tuple/point 6 3 4) (m/mmul x-by-z p))
          "Multiple of z added back to x")

      (is (matrix= (tuple/point 2 5 4) (m/mmul y-by-x p))
          "Multiple of x added back to y")

      (is (matrix= (tuple/point 2 7 4) (m/mmul y-by-z p))
          "Multiple of z added back to y")

      (is (matrix= (tuple/point 2 3 6) (m/mmul z-by-x p))
          "Multiple of x added back to z")

      (is (matrix= (tuple/point 2 3 7) (m/mmul z-by-y p))
          "Multiple of y added back to z"))))


(deftest composing-transformations
  (let [p (tuple/point 1 0 1)
        A (transform/rotate-x (/ (Math/PI) 2))
        B (transform/scale 5 5 5)
        C (transform/translate 10 5 7)]

    (testing "Transformations can be applied in sequence."
      (let [p_A (m/mmul A p)
            p_BA (m/mmul B p_A)
            p_CBA (m/mmul C p_BA)]

        (is (matrix= (tuple/point 1 -1 0) p_A)
            "A multiplied by p")

        (is (matrix= (tuple/point 5 -5 0) p_BA)
            "B multiplied by p_A")

        (is (matrix= (tuple/point 15 0 7) p_CBA)
            "C multiplied by p_BA")))

    (testing "Transformations can be composed and applied."
      (is (matrix= (tuple/point 15 0 7) (m/mmul C B A p))))))
