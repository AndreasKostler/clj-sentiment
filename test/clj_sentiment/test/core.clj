(ns clj-sentiment.test.core
  (:use [clj-sentiment.core] :reload)
  (:use [clojure.test]))

(def doc1 (Document. "d1" {:bow {1 0 2 3 3 4 4 5}} {} 4 :bow))
(def doc2 (Document. "d2" {:bow {1 7 2 6 3 3 4 -1}} {} 4 :bow))
(def coll (Collection. {} "c" 4 1 (list doc1)))
