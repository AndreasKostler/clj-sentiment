(ns clj-sentiment.reading.html
    (:import [org.htmlcleaner HtmlCleaner]
             [org.apache.commons.lang StringEscapeUtils])
    (:require [clj-http.client :as client]))

(defn parse-page
  "Given the HTML source of a web page, parses it and returns the :title
   and the tag-stripped :content of the page. Does not do any encoding
   detection, it is expected that this has already been done."
  [page-src]
  (try
    (when page-src
      (let [cleaner (new HtmlCleaner)]
       (doto (.getProperties cleaner) ;; set HtmlCleaner properties
         (.setOmitComments true)
         (.setPruneTags "script,style"))
       (when-let [node (.clean cleaner page-src)]
         {:title   (when-let [title (.findElementByName node "title", true)]
                     (-> title
                         (.getText)
                         (str)
                         (StringEscapeUtils/unescapeHtml)))
          :content (-> node
                       (.getText)
                       (str)
                       (StringEscapeUtils/unescapeHtml))})))
    (catch Exception e
      (log/error "Error when parsing" e))))
