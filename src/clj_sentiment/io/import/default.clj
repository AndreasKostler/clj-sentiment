(ns clj-sentiment.reading.default
  (:require [clj-sentiment.utils.file-utils :as file-utils])
  (:require [clojure.java.io :as io]))

(defn read-file [f & {:keys [prep-fns] :or {:prep-fns nil}}]
  "Reads a single file."
  (let [content (slurp f)]
    {:id (.getName f) :text (reduce (fn [res proc] (proc res)) content prep-fns)}))

(defn read-dir [dir & {:keys [prep-fns]}]
  "Reads all files in dir"
  (map (fn [f]
         (let [content (slurp f)]
           (read-file f :prep-fns prep-fns)))
       (file-utils/get-file-list dir)))

(defn wrap-default-reader [reader]
  (fn [tokeniser]
    (fn [opts]
      (tokeniser (assoc opts :reader reader)))))

(def wrap-default-file-reader (wrap-default-reader read-file))
(def wrap-default-dir-reader (wrap-default-reader read-dir))
