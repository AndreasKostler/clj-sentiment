(ns clj-sentiment.tutorials.tutorial2
  (:require [clj-sentiment.utils.file-utils :as file-utils])
  (:require [clj-sentiment.utils.sparse-vector :as sparse-vec])
  (:require [clj-sentiment.reading.default :as reader])
  (:require [clj-sentiment.parsing.preprocessing :as prep])
  (:require [stout.porter-stemmer :as porter])
  (:require [clj-sentiment.tokeniser.default-tokeniser :as tokeniser])
  (:require [clj-sentiment.feature-models :as feature-models])
  (:require [clj-sentiment.weight-models :as weights])
  (:require [clj-sentiment.metrics.metrics :as metrics])
  (:require [clj-sentiment.core :as core])
  (:import [clj-sentiment.core Document Collection]))

;; The default tokeniser splits a string by whitespace
;; characters. wrap-default-dir-reader tells the tokeniser to use the
;; plain-text dir reader (ref readers). Before being passed to the
;; tokeniser, the string returned by the reader undergoes a
;; series of transformations to remove unwanted noise. 

(def tut2-tokenise
  (-> #'tokeniser/default-tokeniser
      reader/wrap-default-dir-reader
      (prep/add-prep prep/to-lower-case)
      (prep/add-prep prep/strip-non-word-characters)
      (prep/add-prep prep/merge-multiple-ws)))

(def tokenised-files (tut2-tokenise {:path "src/clj_sentiment/tutorials/tutorial2/corpus"}))

;; Once we have a list of tokenised files we can create the
;; collection after removing stop words and stemming. 
(def create-collection
  (-> #'core/create-collection
      (feature-models/wrap feature-models/bow)  
      (prep/add-prep prep/remove-default-stopwords)
      (prep/add-prep porter/porter-stemmer)))

(def tut2-coll (create-collection {:feature-model feature-models/bow :file-tokens tokenised-files :id "tut2-collection"}))

;; Calculate the weights based on the tf-idf weight model
(def docs (weights/weigh-terms tut2-coll weights/tf-idfp 0))

(defn create-query [s & args]
  (let [query-vec (zipmap args (repeat 1))]
    (Document. s {:bow query-vec} {} (count args) :bow)))

;; Run the query against all docments in our collection based on the
;; given similarity metric
;;(def query #{"blub" "bla" "bleu"})

(def q (create-query "query" "semant" "latent" "index"))


;;(core/compare tut2-coll query cosine-similarity)






