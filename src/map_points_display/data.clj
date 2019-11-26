(ns map-points-display.data
  (:require [clojure.string :as s]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [jdbc.core :as jdbc]
            [environ.core :refer [env]]
            [clj-http.client :as http]
            [taoensso.carmine :as car :refer [wcar]]
            [map-points-display.config :refer [config secrets]]
            [map-points-display.data.fetch :refer [fetch-airtable-data]]
            [map-points-display.utils :refer [path-join]]
            [map-points-display.data.config :refer [data-source]]))

(defmacro wcar* [& body]
  `(wcar (@config :redis) ~@body))

;; TODO: update interface

;; Table list

(defn tables-list
  []
  (with-open [conn (jdbc/connection data-source)]
    (jdbc/fetch conn "SELECT * FROM trips")))

;; Tables

(defn load-data
  [table-name]
  (with-open [conn (jdbc/connection data-source)]
    (let [q (str "SELECT points.* FROM points\n"
                 "JOIN trips t on points.trip_id = t.id\n"
                 "WHERE t.name = ?")]
      (jdbc/fetch conn [q table-name]))))

(defn load-poi
  [table-name id]
  (with-open [conn (jdbc/connection data-source)]
    (-> conn
        (jdbc/fetch ["SELECT * FROM points WHERE id = ?" id])
        first)))

