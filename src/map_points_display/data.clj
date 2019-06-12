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

(def redis-config
  (:redis @config))

(defmacro wcar* [& body]
  `(wcar redis-config ~@body))

(def tables-list
  (:tables-list @config))

(defn- table-key
  [table]
  (str "map-points:table:" table))

(defn read-table
  [table]
  (wcar* (car/get (table-key table))))

(defn load-data
  [table-name]
  (let [raw-data (read-table table-name)]
    (group-by :type raw-data)))

(defn- postprocess-data
  [data]
  (let [records (data "records")]
    (->> records
         (map #(get % "fields"))
         (map keywordize-keys))))

(defn fetch-table-data
  [table-name]
  (let [{adb :airtable-database api-key :airtable-api-key} @secrets
        url (str "https://api.airtable.com/v0/" adb "/" table-name)
        resp (http/get url {:headers {"Authorization" (str "Bearer " api-key)}
                            :cookie-policy :standard})]
    (if (= (:status resp) 200)
      (-> resp :body json/read-str postprocess-data))))

(defn update-table-data
  [table]
  (when-let [data (fetch-table-data table)]
    (wcar* (car/set (table-key table) data))))
