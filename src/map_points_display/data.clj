(ns map-points-display.data
  (:require [clojure.string :as s]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [clj-http.client :as http]
            [taoensso.carmine :as car :refer [wcar]]
            [map-points-display.config :refer [config secrets]]
            [map-points-display.utils :refer [path-join]]))

(defmacro wcar* [& body]
  `(wcar (:redis (config)) ~@body))

(defn tables-list
  []
  (:tables-list (config)))

(defn- table-key
  [table]
  (str "map-points:table:" table))

(defn- meta-key
  [table]
  (str "map-points:_meta:" table))

(defn read-table
  [table]
  (wcar* (car/get (table-key table))))

(defn load-data
  [table-name]
  (let [raw-data (read-table table-name)]
    (group-by :type raw-data)))

(defn- table-request-headers
  [table-name]
  (let [{api-key :airtable-api-key} (secrets)
        hdr {"Authorization" (str "Bearer " api-key)}]
    (if-let [{etag :etag} (wcar* (car/get (meta-key table-name)))]
      (merge hdr {"If-None-Match" etag})
      hdr)))

(defn- postprocess-data
  [data]
  (let [records (data "records")]
    (->> records
         (map #(get % "fields"))
         (map keywordize-keys))))

(defn fetch-table-data
  [table-name]
  (let [{adb :airtable-database} (secrets)
        url (str "https://api.airtable.com/v0/" adb "/" table-name)
        resp (http/get url {:headers (table-request-headers table-name)
                            :cookie-policy :standard
                            :throw-exceptions false})]
    (case (:status resp)
      200 (do
            (println "OK, data retrieved")
            ;; TODO: replace with clj-http's cheshire integration
            {:data (-> resp :body json/read-str postprocess-data)
             :meta {:etag (-> resp :headers :etag)}})
      304 (println "Not modified")
      404 (println "Not found")
      (range 400 600) (println "Error"))))

(defn update-table-data
  [table]
  (when-let [{:keys [data meta]} (fetch-table-data table)]
    (wcar* (car/set (table-key table) data)
           (car/set (meta-key table) meta))))
