(ns trace-a-ray.ray-test
  (:require [trace-a-ray.ray :as ray]
            [trace-a-ray.tuple :as tuple]
            [trace-a-ray.sphere :as sphere]
            [trace-a-ray.transformation :as trans]
            [trace-a-ray.helpers :refer :all]
            [clojure.test :refer :all]))

(deftest creating-a-ray
  (testing "Construction of rays"
    (let [p (tuple/point 1 1 1)
          dir (tuple/vector 2 3 4)
          r (ray/make-ray p dir)]

      (is (= (.point r) p)
          "Fetch the point component of a ray")

      (is (= (.direction r) dir)
          "Fetch the direction component of a ray"))))

(deftest determining-ray-position
  (testing "Determining the position of a ray at time t"
    (let [p (tuple/point 2 3 4)
          dir (tuple/vector 1 0 0)
          r (ray/make-ray p dir)]

      (is (tuple= (ray/position r 0) (tuple/point 2 3 4))
          "Time 0")

      (is (tuple= (ray/position r 1) (tuple/point 3 3 4))
          "Time 1")

      (is (tuple= (ray/position r -1) (tuple/point 1 3 4))
          "Time -1")

      (is (tuple= (ray/position r 2.5) (tuple/point 4.5 3.0 4.0))
          "Time 2.5"))))

(deftest ray-sphere-intersection
  (testing "When a ray intersects a sphere in two points."
    (let [s   (sphere/make-sphere)
          p   (tuple/point 0 0 -5)
          dir (tuple/vector 0 0 1)
          r   (ray/make-ray p dir)]
      (is (= [4.0 6.0] (ray/intersect s r)))))

  (testing "When a ray intersects a sphere tangentially"
    (let [s   (sphere/make-sphere)
          p   (tuple/point 0 1 -5)
          dir (tuple/vector 0 0 1)
          r   (ray/make-ray p dir)]
      (is (= [5.0 5.0] (ray/intersect s r)))))

  (testing "When a ray misses a sphere."
    (let [s   (sphere/make-sphere)
          p   (tuple/point 0 2 -5)
          dir (tuple/vector 0 0 1)
          r   (ray/make-ray p dir)]
      (is (= [] (ray/intersect s r)))))

  (testing "When a ray has the point in the interior of a sphere."
    (let [s   (sphere/make-sphere)
          p   (tuple/point 0 0 0)
          dir (tuple/vector 0 0 1)
          r   (ray/make-ray p dir)]
      (is (= [-1.0 1.0] (ray/intersect s r)))))

  (testing "When a ray is in front of a sphere and points are in increasing order."
    (let [s   (sphere/make-sphere)
          p   (tuple/point 0 0 5)
          dir (tuple/vector 0 0 1)
          r   (ray/make-ray p dir)]
      (is (= [-6.0 -4.0] (ray/intersect s r))))))


(deftest determining-hits-and-intersections-in-the-world
  (testing "When all intersections have positive time values."
    (let [s      (sphere/make-sphere)
          world  (->> (ray/make-world)
                      (ray/intersection 1 s)
                      (ray/intersection 2 s))
          inters (ray/intersections s world)]

      (is (= 1 (ray/hit inters))
          "The lowest t-value should be returned.")))

  (testing "When some intersections are negative valued."
    (let [s      (sphere/make-sphere)
          world  (->> (ray/make-world)
                      (ray/intersection -1 s)
                      (ray/intersection 1 s))
          inters (ray/intersections s world)]

      (is (= 1 (ray/hit inters))
          "The lowest non-negative t should be returned")))

  (testing "When all intersections have negative time values."
    (let [s      (sphere/make-sphere)
          world  (->> (ray/make-world)
                      (ray/intersection -2 s)
                      (ray/intersection -1 s))
          inters (ray/intersections s world)]

      (is (= nil (ray/hit inters))
          "There should be no intersections")))

  (testing "When all intersections have positive time values."
    (let [s      (sphere/make-sphere)
          world  (->> (ray/make-world)
                      (ray/intersection 5 s)
                      (ray/intersection 7 s)
                      (ray/intersection -3 s)
                      (ray/intersection 2 s))
          inters (ray/intersections s world)]
      (is (= 2 (ray/hit inters))))))


(deftest translating-a-ray
  (testing "Translating a ray to object space."
    (let [r (ray/make-ray (tuple/point 1 2 3) (tuple/vector 0 1 0))
          M (trans/translate 3 4 5)
          r2 (ray/transform r M)]

      (is (tuple= (tuple/point 4 6 8) (.point r2))
          "The point of origin for the ray is scaled.")

      (is (tuple= (tuple/vector 0 1 0) (.direction r2))
          "The direction is not changed since we transformed.")))

  (testing "Scaling a ray to object space."
    (let [r (ray/make-ray (tuple/point 1 2 3) (tuple/vector 0 1 0))
          M (trans/scale 2 3 4)
          r2 (ray/transform r M)]

      (is (tuple= (tuple/point 2 6 12) (.point r2))
          "When scaling the point of origin for the ray is scaled
          too.")

      (is (tuple= (tuple/vector 0 3 0) (.direction r2))))))
