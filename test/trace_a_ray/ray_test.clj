(ns traca-a-ray.ray-test
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
