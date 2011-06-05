(defproject clj-sentiment "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/clojure "1.2.1"]
                 [clj-http "0.1.1"]
                 [clj-time "0.3.0"]
                 [commons-lang "2.6"]
                 [clj-json "0.3.2"]
                 [org.clojars.sids/htmlcleaner "2.1"]
                 [stout "0.1.0"]]
  :dev-dependencies [[swank-clojure "1.3.1"]
                     [marginalia "0.5.1"]]
  :main clj-sentiment.core
  :jvm-opts ["-server" "-Xmx256m"])
