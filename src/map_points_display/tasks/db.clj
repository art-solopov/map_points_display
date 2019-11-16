(ns map-points-display.tasks.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [map-points-display.data.config :refer [db-config]]))

(defn load-config []
  {:datastore (jdbc/sql-database (db-config))
   :migrations (jdbc/load-resources "migrations")})

;; TODO: add versions

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))
