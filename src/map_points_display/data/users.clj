(ns map-points-display.data.users
  (:require [crypto.password.bcrypt :as password]
            [clojure.tools.logging :as log]
            [jdbc.core :as jdbc]
            [map-points-display.data.config :refer [data-source]]))

(defn load-by-id
  [id]
  (with-open [conn (jdbc/connection data-source)]
    (-> conn
        (jdbc/fetch ["SELECT * FROM users WHERE id = ?" id])
        first)))

(defn load-by-username
  [username]
  (with-open [conn (jdbc/connection data-source)]
    (-> conn
        (jdbc/fetch ["SELECT * FROM users WHERE username = ?" username])
        first)))

(defn authenticate
  [username password]
  (when-let [user (load-by-username username)]
    (if (password/check password (:password_digest user))
      user
      nil)))
