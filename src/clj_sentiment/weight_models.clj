(in-ns 'clj-sentiment.core)

(defprotocol PWeighTerms
  (weigh-terms [this weight-model coll] "Weighs the terms according to a weight model"))

(extend-type Document
  PWeighTerms
  (weigh-terms [this weight-model coll] (weight-model this coll)))

(extend-type Collection
  PWeighTerms
  (weigh-terms [this weight-model _] (map #(weigh-terms % weight-model this) (:docs this))))

(defn idf [d n]
  (/ d n))

(defn idfl [d n]
  (math/log 10 (/ d n)))

(defn idfp [d n]
  (math/log 10 (/ (- d n) n)))

(defn tf-idf-model [idf-fn]
  (fn [{:keys [features model-id] :as document} {:keys [inv-idx d]}]
    (let [bow (features model-id)
          weighted-terms (reduce (fn [wm [term tf]]
                                   (let [n (:n (inv-idx term))
                                         idf (idf-fn d n)]
                                     (assoc wm term (* tf idf)))) {} bow)]
      (assoc document :features {:tf-idfp weighted-terms} :model-id :tf-idf))))

(def tf-idf (tf-idf-model idf))
(def tf-idfl (tf-idf-model idfl))
(def tf-idfp (tf-idf-model idfp))

(defn weights [doc model-id]
  (:weights (:features (doc model-id))))
