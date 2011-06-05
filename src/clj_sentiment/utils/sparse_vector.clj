(ns clj-sentiment.utils.sparse-vector
  (:require [clojure.contrib.math :as core-math]))

;; Todo create protocol and extend clojure.lang.PersistentHashSet

(defn sparse-vec
  "Creates a map representing a sparse vector from a collection of key value pairs or a collection each for keys and values."
  ([a] (into {} (filter (fn [[_ v]] (not (zero? v))) a)))
  ([a b] (sparse-vec (zipmap a b))))

(defn sum [a b]
  "Component wise sum of a and b"
  (let [[a b] (if (> (count a) (count b)) [b a] [a b])]
    (map (fn [[k v]] (+ v (get b k 0))) a)))

(defn diff [a b]
  "Component wise difference of a and b"
  (map (fn [[k v]] (- v (get b k 0))) a))

(defn inner-product [a b]
  "Calculates the inner product (dot product) of two sparse vectors."
  (let [[a b] (if (> (count a) (count b)) [b a] [a b])]
    (reduce + (map (fn [[k v]] (* v (get b k 0))) a))))

(defn l2-norm [a]
  "Calculates the Euclidean (L2) norm of a sparse vector."
  (Math/sqrt (reduce + (map (fn [[_ v]] (* v v)) a))))

(defn l1-norm [a]
  "Calculates the Manhattan (L1) norm of a sparse vector."
  (reduce + (map (fn [[_ v]] (core-math/abs v)) a)))

(defn unit-vec [a]
  "Takes a sparse vector and returns a sparse vector with an
Eucliden (L2) norm of 1."
  (let [l-inv (/ 1 (l2-norm a))]
    (reduce (fn [m [k v]] (assoc m k  (* l-inv v))) {}  a)))

(defn update [a k v]
  "Update the kth element in a with v. If v is zero, remove [k v] to maintain
the sparseness criterion."
  (if (zero? val) (dissoc a k)
      (assoc a k v)))
