(ns clj-sentiment.tutorials.tutorial1
  (:import [clj-sentiment.core Collection Document])
  (:use [clj-sentiment.weight-models])
  (:use [clj-sentiment.metrics.metrics]))

(def collection (Collection.
                 {"gift" {:n 300000 :docs #{"doc1" "doc2" "other"}}
                  "card" {:n 400000 :docs #{"doc1" "doc2" "other"}}}
                 "test-collection"
                 -1
                 100000000))

(def doc1 (Document. "doc1" {:bow {"gift" 2 "card" 3}} {}))
(def doc2 (Document. "doc2" {:bow {"gift" 1 "card" 6}} {}))
(def query (Document. "query" {:bow {"gift" 1 "card" 1}} {}))

(def doc1-w (tf-idfp doc1 collection))
(def doc2-w (tf-idfp doc2 collection))
(def query-w (tf-idfp query collection))

(cosine-distance (weights doc1-w) (weights query-w))
(cosine-distance (weights doc2-w) (weights query-w))

