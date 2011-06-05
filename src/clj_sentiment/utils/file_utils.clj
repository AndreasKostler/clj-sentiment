(ns clj-sentiment.utils.file-utils
  (:require [clojure.java.io :as io])
  (:require [clojure.contrib.duck-streams :as duck-streams]))

(defn- is-dir? [f]
  "Is f a directory?"
  (when f (.isDirectory f)))

(defn- is-file? [f]
  "is f an oridinary file?"
  (when f (.isFile f)))

(defn- get-files-in-path [p]
  "Get a list of all oridinary files in path p"
  (let [f (io/as-file p)]
    (when f (filter is-file? (seq (.listFiles f))))))

(defn- filter-by-extensions [l exts]
  "Filer a list of files l by extension exts"
 (if (not (empty? exts))
   (filter #(exts (last (.split (.getAbsolutePath %) "\\."))) l)
   l))


(defn get-file-list [p & exts]
  "Get a list of oridinary files in path p filtered by extension exts."
  (filter-by-extensions (get-files-in-path p) (set exts)))
