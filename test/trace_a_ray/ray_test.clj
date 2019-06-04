(ns trace-a-ray.ray-test
  (:require [trace-a-ray.ray :as ray]
            [trace-a-ray.tuple :as tuple]
            [clojure.test :refer :all]))

(deftest creating-a-ray
  (testing "Construction of rays"
    (let [p (tuple/point 1 1 1)
          dir (tuple/vector 2 3 4)
          r (ray/->ray p dir)]

      (is (= (.point r) p)
          "Fetch the point component of a ray")

      (is (= (.direction r) dir)
          "Fetch the direction component of a ray"))))

(deftest determining-ray-position
  (testing "Determining the position of a ray at time t"
    (let [p (tuple/point 2 3 4)
          dir (tuple/vector 1 0 0)
          r (ray/->ray p dir)]

      (is (= (ray/position r 0) (tuple/point 2 3 4))
          "Time 0")

      (is (= (ray/position r 1) (tuple/point 3 3 4))
          "Time 1")

      (is (= (ray/position r -1) (tuple/point 1 3 4))
          "Time -1")

      (is (= (ray/position r 2.5) (tuple/point 4.5 3.0 4.0))
          "Time 2.5"))))

(defn sphere []
  "Create a unit-sphere."
  [])

(deftest ray-sphere-intersection
  (testing "When a ray intersects a sphere in two points."
    (let [s (sphere)
          p (tuple/point 0 0 -5)
          dir (tuple/vector 0 0 1)
          r (ray/->ray p dir)]
      (is (= [4.0 6.0] (ray/intersect s r)))))

  (testing "When a ray intersects a sphere tangentially"
    (let [s (sphere)
          p (tuple/point 0 1 -5)
          dir (tuple/vector 0 0 1)
          r (ray/->ray p dir)]
      (is (= [5.0 5.0] (ray/intersect s r)))))

  (testing "When a ray misses a sphere."
    (let [s (sphere)
          p (tuple/point 0 2 -5)
          dir (tuple/vector 0 0 1)
          r (ray/->ray p dir)]
      (is (= [] (ray/intersect s r)))))

  (testing "When a ray has the point in the interior of a sphere."
    (let [s (sphere)
          p (tuple/point 0 0 0)
          dir (tuple/vector 0 0 1)
          r (ray/->ray p dir)]
      (is (= [-1.0 1.0] (ray/intersect s r)))))

  (testing "When a ray is in front of a sphere and points are in increasing order."
    (let [s (sphere)
          p (tuple/point 0 0 5)
          dir (tuple/vector 0 0 1)
          r (ray/->ray p dir)]
      (is (= [-6.0 -4.0] (ray/intersect s r))))))
