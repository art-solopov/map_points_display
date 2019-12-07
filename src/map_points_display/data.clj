(ns map-points-display.data
  (:require [clojure.string :as s]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [jdbc.core :as jdbc]
            [environ.core :refer [env]]
            [map-points-display.config :refer [config secrets]]
            [map-points-display.utils :refer [path-join]]
            [map-points-display.data.config :refer [data-source]]))

(defn trips-list
  []
  (with-open [conn (jdbc/connection data-source)]
    (jdbc/fetch conn "SELECT * FROM trips")))

(defn- postprocess-point
  [point]
  (let [{:keys [coordinates]} point]
    (-> point
        (dissoc :coordinates)
        (merge {:lat (.x coordinates) :lon (.y coordinates)}))))

(defn load-trip-points
  [table-name]
  (with-open [conn (jdbc/connection data-source)]
    (let [q (str "SELECT points.* FROM points\n"
                 "JOIN trips t on points.trip_id = t.id\n"
                 "WHERE t.name = ?")
          points (jdbc/fetch conn [q table-name])]
      (->> points
           (map postprocess-point)
           (group-by :type)))))

(defn load-point
  [id]
  (with-open [conn (jdbc/connection data-source)]
    (-> conn
        (jdbc/fetch ["SELECT * FROM points WHERE id = ?" id])
        first
        postprocess-point)))

