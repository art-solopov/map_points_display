(ns map-points-display.data.trips
  (:require [jdbc.core :as jdbc]
            [map-points-display.data.config :refer [data-source]]))

(defn create
  [user params]
  (let [author-id (:id user)
        trip-name (params "name")
        country-code (params "country_code")]
    (with-open [conn (jdbc/connection data-source)]
      (jdbc/execute conn
                    ["INSERT INTO trips (author_id, name, country_code) VALUES (?, ?, ?)"
                     author-id trip-name country-code]))))

(defn get-country-code
  [id]
  (with-open [conn (jdbc/connection data-source)]
    (-> conn
        (jdbc/fetch ["SELECT country_code FROM trips WHERE id = ?" id])
        first)))
