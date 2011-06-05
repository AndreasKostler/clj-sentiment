(in-ns 'clj-sentiment.core)

(defn wrap [coll-fn model]
  "Wraps a feature given feature model."
  (fn [opts]
    (coll-fn (assoc opts :feature-model model))))

(defn bow [l]
  "The bag of words feature model groups features and their term frequencies."
  (hash-map :bow (reduce (fn [{tf term :as m} term]
                     (assoc m term (inc (or tf 0)))) {} l)))
