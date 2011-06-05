(ns clj-sentiment.reading.google-reader
  (:require [clj-time.core :as t])
  (:require [clj-http.client :as client])
  (:require [clj-json.core :as j]))

(def *google-auth-token* (atom {:last-updated-at (t/date-time 1980) :token ""}))

(defn expired? [auth-token & {:keys [mins] :or {mins 60}}]
  (> (t/in-minutes (t/interval (:last-updated-at auth-token) (t/now))) mins))

(defn update-auth-token [auth-token token]
  (-> auth-token (assoc :last-updated-at (t/now))
      (assoc :token (token "Auth"))))

(defn wrap-google-auth [client]
  (fn [{:keys [login passwd service auth-url] :as req}]
    (when (and (and login passwd service)
             (expired? @*google-auth-token*))
      (let [req-url (:url req)
            response
            (:body
             (client (-> req (dissoc :account-details)
                         (dissoc :service)
                         (assoc :url (or auth-url "https://www.google.com/accounts/ClientLogin"))
                         (assoc :method :post)
                         (assoc :query-params
                           {"Email" login
                            "Passwd" passwd
                            "service" service
                            "accountType" "GOOGLE"}))))
            token
            (apply hash-map (flatten (map #(seq (.split % "="))
                                          (seq (.split response "\\s+")))))]
        (assoc req :url req-url)
        (swap! *google-auth-token* update-auth-token token)))
    (client (-> req
                (dissoc :login)
                (dissoc :passwd)
                (dissoc :service)
                (dissoc :auth-url)
                (assoc :method :get)
                (assoc-in [:headers "Authorization"]
                          (str "GoogleLogin auth=" (:token @*google-auth-token*)))))))

(defn wrap-google-reader-feed [client]
  (fn [{:keys [feed-url] :as req}]
    (when feed-url
      (let [response (:body (client
                             (-> req
                                 (dissoc :feed-url)
                                 (assoc :url (str (:url req) feed-url)))))]
        (map #(:content (parse-page %)) (map #(get-in % ["summary" "content"])
                         ((j/parse-string response) "items")))))))

(def import (-> #'client/request
                wrap-google-auth
                wrap-google-reader-feed))
