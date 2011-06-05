(in-ns 'clj-sentiment.core)

(defn get-features [{:keys [features model-id]}]
  (features model-id))

(defn- apply-distance-metric [metric coll v]
  (reduce (fn [m document]
            (assoc m (:id document) (metric document v))) {} (:docs coll)))

(defprotocol PMetrics
  (pearson-coefficient [this v] "Pearson's correlation coefficient between two variables is defined as the covariance of the two variables divided by the product of their standard deviations. The correlation coefficient takes values from –1 (large, negative correlation) to +1 (large, positive correlation). Note that the data are centered by subtracting the mean, and scaled by dividing by the standard deviation.")
  (pearson-distance [this v] "Effectively, the Pearson distance is computed as dp = 1 - r, where r is the pearson correlation coefficient and lies between 0 (when correlation coefficient is +1, i.e. the two samples are most similar) and 2 (when correlation coefficient is -1)")
  (manhattan-distance [this v] "The distance between two points is the sum of the absolute differences of their coordinates.")
  (cosine-distance [this v] "1 - cosine similarity")
  (cosine-similarity [this v] "Cosine similarity is a measure of similarity between two vectors by measuring the cosine of the angle between them.")
  (jaccard-index [this v] "The Jaccard coefficient measures similarity between sample sets, and is defined as the size of the intersection divided by the size of the union of the sample sets")
  (jaccard-distance [this v] "The Jaccard distance, which measures dissimilarity between sample sets, is complementary to the Jaccard coefficient and is obtained by subtracting the Jaccard coefficient from 1")
  (euclidean-distance [this v] "The Euclidean distance or Euclidean metric is the "ordinary" distance between two points that one would measure with a ruler, and is given by the Pythagorean formula. It takes into account the difference between two samples directly, based on the magnitude of changes in the sample levels. This distance type is usually used for data sets that are suitably normalized.")
  (euclidean-distance-squared [this v] "The Euclidean distance squared. Useful for k-means clustering")
  (chebyshev-distance [this v] "The distance between two vectors is the greatest of their differences along any coordinate dimension."))
 
(extend-type Collection
  PMetrics
  (cosine-distance [this v] (apply-distance-metric cosine-distance this v))
  (cosine-similarity [this v] (apply-distance-metric cosine-similarity this v))
  (pearson-coefficient [this v] (apply-distance-metric pearson-coefficient this v))
  (jaccard-index [this v] (apply-distance-metric jaccard-index this v))
  (jaccard-distance [this v] (apply-distance-metric jaccard-distance this v))
  (euclidean-distance [this v] (apply-distance-metric euclidean-distance this v))
  (euclidean-distance-squared [this v] (apply-distance-metric euclidean-distance-squared this v))
  (manhattan-distance [this v] (apply-distance-metric manhattan-distance this v))
  (chebyshev-distance [this v] (apply-distance-metric chebyshev-distance this v))
  (squared-euclidean-distance [this v] (apply-distance-metric euclidean-distance-squared this v)))

(extend-type Document
  PMetrics
  (cosine-distance [this v] (- 1 (cosine-similarity this v)))

  (cosine-similarity [this v]
    (let [v1 (get-features this)
          v2 (get-features v)]
      (/ (sparse-vector/inner-product v1 v2) (* (sparse-vector/l2-norm v1) (sparse-vector/l2-norm v2)))))
  
  (pearson-coefficient [this v]
    (let [v1 (get-features this)
          v2 (get-features v)
          {v1-mean :mean v1-std-dev :std-dev} (math/basic-stats v1)
          {v2-mean :mean v2-std-dev :std-dev} (math/basic-stats v2)]
      (* (/ v1-mean v1-std-dev) (/ v2-mean v2-std-dev))))
  (jaccard-index [this v] "TODO")
  
  (jaccard-distance [this v] "TODO")
  
  (chebyshev-distance [this v] "TODO")
  
  (manhattan-distance [this v]
    (let [v1 (get-features this)
          v2 (get-features v)
          diff-abs (map contrib-math/abs (sparse-vector/diff v1 v2))]
      (reduce + diff-abs)))
  
  (euclidean-distance [this v]
    (Math/sqrt (euclidean-distance-squared this v)))
  
  (euclidean-distance-squared [this v]
    (let [v1 (get-features this)
          v2 (get-features v)
          diff-squared (map #(* % %) (sparse-vector/diff v1 v2))]
      (reduce + diff-squared))))
