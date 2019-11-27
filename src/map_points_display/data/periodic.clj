(ns map-points-display.data.periodic
  (:require [clojure.tools.logging :as log]
            [mount.core :refer [defstate]]
            ;; [map-points-display.data :refer [tables-list update-tables-list update-table-data]]
            )
  (:import [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(defonce executor (atom nil))

(defn update-tables
  []
  ;; (log/info "Updating tables list")
  ;; (update-tables-list)
  ;; (doseq [table (tables-list)]
  ;;   (log/info (str "Updating table " table))
  ;;   (update-table-data table)
  ;;   (Thread/sleep 1000))
  )

(defn init
  []
  (log/debug (str "Initializing executor"))
  (reset! executor (ScheduledThreadPoolExecutor. 3))
  (.scheduleAtFixedRate @executor update-tables 0 30 TimeUnit/SECONDS))

(defn shutdown
  []
  (log/debug ("Shutting down executor"))
  (.shutdownNow @executor)
  (reset! executor nil))

;; (defstate periodic :start (init) :stop (shutdown))
