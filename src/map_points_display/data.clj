(ns map-points-display.data
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [environ.core :refer [env]]
            [taoensso.carmine :as car :refer [wcar]]
            [map-points-display.config :refer [config]]
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
