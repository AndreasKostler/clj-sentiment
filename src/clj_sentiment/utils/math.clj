(ns clj-sentiment.utils.math)

(defn log [b x]
  "Log of x to the base b."
  (/ (java.lang.Math/log x) (java.lang.Math/log b)))


(defn median [x]
  "Median of a sample set x"
        (let [x (sort x)
             c (count x)
             median (split-at (/ c 2) x)]
             (if (zero? (mod c 2))
                 (/ (+ (last (first median)) (first (second median))) 2)
                 (last (first median)))))

(defn mean [x n]
  "Mean of sample set x."
  (/ (reduce + x) n))

(defn std-dev [x n]
  "Standard deviation of sample set x"
  (let [m (mean x n)]
    (- (reduce + (map #(* % %) x))
       (+ m m))))

(defn basic-stats [x]
  "Returns the basic statistics mean and std-dev for sample set x in one go for performance reasons."
          (let [[sum sum-sq n] (reduce (fn [[sum sum-sq n] el]
                                           [(+ sum el) (+ sum-sq (* el el)) (inc n)])
                                       [0 0 0]
                                       x)
               mean (/ sum n)]
          {:mean mean :std-dev (Math/sqrt (- (/ sum-sq n) (* mean mean)))}))
