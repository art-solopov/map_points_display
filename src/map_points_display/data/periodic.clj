(ns map-points-display.data.periodic
  (:require [map-points-display.data :refer [tables-list update-table-data]])
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(def executor (atom nil))

(defn update-tables
  []
  (doseq [table tables-list]
    (println "Updating table" table)
    (update-table-data table)
    (Thread/sleep 1000)))

(defn init
  []
  (println "Initializing executor")
  (reset! executor (ScheduledThreadPoolExecutor. 3))
  (.scheduleAtFixedRate @executor update-tables 0 30 TimeUnit/SECONDS))

(defn shutdown
  []
  (println "Shutting down executor")
  (.shutdownNow @executor)
  (reset! executor nil))
