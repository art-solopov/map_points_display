(ns map-points-display.data.fetch
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [map-points-display.config :refer [secrets]]))

(def ^:const base-url "https://api.airtable.com/v0/")

(defn- airtable-db []
  (:airtable-database @secrets))

(defn- airtable-url
  [path]
  (str base-url (airtable-db) path))

(defn- auth-headers []
  (let [{api-key :airtable-api-key} @secrets]
    {"Authorization" (str "Bearer " api-key)}))

(defn- fetch-headers
  [etag]
  (let [{api-key :airtable-api-key} @secrets
        headers {"Authorization" (str "Bearer " api-key)
                 "If-None-Match" etag}]
    (into {} (filter val headers))))

(defn fetch-airtable-data
  [path {query-params :query-params etag :etag}]
  (let [url (airtable-url path)
        response (http/get url {:query-params query-params
                                :headers (fetch-headers etag)
                                :cookie-policy :standard
                                :throw-exceptions false
                                :multi-param-style :array
                                :as :json})]
    (case (:status response)
      200 (do
            (log/info "OK, data retrieved")
            {:data (-> response :body)
             :meta {:etag (-> response :headers :etag)}})
      304 (log/info "Not modified")
      404 (log/warn "Not found")
      422 (log/error (str "Something happened\n" (:body response)))
      (log/warn (str "Unhandled status " (:status response))))))
