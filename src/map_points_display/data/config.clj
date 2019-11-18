(ns map-points-display.data.config
  (:require [clojure.set :refer [rename-keys]]
            [hikari-cp.core :refer [make-datasource close-datasource]]
            [mount.core :refer [defstate]]
            [map-points-display.config :refer [secrets]]))

(defn db-config []
  (:dbspec @secrets))

(defn pool-config []
  (rename-keys (db-config) {:dbtype :adapter
                            :dbname :database-name
                            :host :server-name
                            :user :username}))

(defonce ds-ref (atom nil))

(defn start
  []
  (let [ds (make-datasource (pool-config))]
    (reset! ds-ref ds)))

(defn stop
  []
  (when-not (nil? @ds-ref)
    (close-datasource @ds-ref)
    (reset! ds-ref nil)))

(defstate data-source :start (start) :stop (stop))
