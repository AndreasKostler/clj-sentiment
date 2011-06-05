(ns clj-sentiment.classifier
  (require [clj-sentiment.core :as core]))

;; Should be multimethod
(defn create-classifier [coll]
  (reduce (fn [classifier d] (train classifier d)) (NaiveBayesClassifier. {} {}) coll))

(defn tag-document [d c]
  "Tag a document d as belonging to category c"
  (assoc-in d [:tags :category] c))

(defprotocol PTrain
  (train [this d] "Takes a classified document d to train a classifier."))

(defprotocol PClassifier
  (feature-probability [this f c] "Probability that the feature f appears in category c")
  (classify [this d] "Takes a document d and returns a classification")
  (feature-probability [this f c] "Conditional probability Pr(f|c)")
  (weighted-probality [this f c] prb-fn wght ap) "Weigh probabilities according to a weight wght starting with an assumed probability of ap. This makes desensitises the classifiers in early stages of training or for sparse training data.")

(defrecord NaiveBayesClassifier [fc cc]
  PTrain
  (train [this {:keys [features tags model-id]}]
    (let [features (keys (features model-id))
          c (:category tags)
          cc (update-in this [:cc c] (fnil inc 0))]
      (reduce (fn [m feat] (update-in m [:ct feat c] (fnil inc 0))) cc features)))
  PClassifier
  (feature-probability [this f c] (let [doc-in-category (get-in this [:cc c] 0)]
                                    (if (zero? doc-in-category) 0
                                        (/ (get-in this [:fc f c]) doc-in-category))))
  (weighted-probability [this f c p-fn w ap] )
  (classify [this d]))


