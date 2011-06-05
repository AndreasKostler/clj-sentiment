(ns clj-sentiment.core
  (:require [clj-sentiment.utils.math :as math])
  (:require [clojure.contrib.math :as contrib-math])
  (:require [clj-sentiment.utils.sparse-vector :as sparse-vector]))

(defrecord Document [id features tags length model-id])

(defn create-document [& {:keys [id features tags length model-id] :or {id "" features {} tags {} length 0 model-id nil}}]
  (Document. id features tags length model-id))

(defn apply-feature-model [model {:keys [id text tags]}]
  (let [features (model text)
        model-id (first (keys features))]
    (Document. id features tags (count (features model-id)) model-id)))



(defrecord Collection [inv-idx id avgdl d docs])

(defn update-inv-idx [inv-idx terms id]
  (reduce (fn [m term]
            (-> m
                (update-in [term :doc-ids] #(conj (set  %) id))
                (update-in [term :n] (fnil inc 0)))) inv-idx terms))

(defn preprocess-token [prep-fns {:keys [text] :as token}]
  (if prep-fns
    (assoc token :text (reduce (fn [res proc] (filter #(not (nil? %)) (map proc res))) text prep-fns))
    token))

(defn create-collection [{:keys [feature-model file-tokens prep-fns id]}]
  "Creates a collection from a list of documents"
  (let [d (count file-tokens)
        file-tokens (map (partial preprocess-token prep-fns) file-tokens)
        documents (map (partial apply-feature-model feature-model) file-tokens)
        coll (reduce (fn [{:keys [inv-idx avgdl docs] :as coll}
                         {:keys [id features length model-id] :as doc}]
                       (let [terms (keys (features model-id))]
                         (assoc coll
                           :avgdl (+ avgdl (/ length d))
                           :inv-idx (update-inv-idx inv-idx terms id))))
                     (Collection. {} id 0 0 [])
                     documents)]
    (assoc coll :d d :docs documents)))


(load "weight_models")
(load "feature_models")
(load "metrics")

;; preprocessing
;; (-> result
;;     to-lower-case
;;     strip-stopwords
;;     strip-punctuation
;;     strip-html-tags
;;     (strip-short-tokens 3)
;;     strip-non-alpha
;;     strip-multiple-ws - probably not needed)

;; tokenising
;; (-> result
;;     (.split "\\s+"))


