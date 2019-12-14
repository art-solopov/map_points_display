(ns map-points-display.data.points
  (:require [jdbc.core :as jdbc]
            [map-points-display.data.config :refer [data-source]]))

(def types
  ["museum" "sight" "transport" "accomodation" "entertainment"
   "food" "other"])

(defn create
  [_user trip-id params]
  (let [name (params "name")
        address (params "address")
        lat (Double/parseDouble (params "lat"))
        lon (Double/parseDouble (params "lon"))
        type (params "type")
        notes (params "notes")
        schedule (params "schedule")]
    (with-open [conn (jdbc/connection data-source)]
      (jdbc/execute conn
                    [(str "INSERT INTO points "
                          "(trip_id, name, address, coordinates, type, notes, schedule) "
                          "VALUES "
                          "(?, ?, ?, point(?, ?), ?, ?, ?)")
                     trip-id name address lat lon type notes schedule])))
  )
