(ns map-points-display.data.geocode
  (:require [clojure.string :refer [split]]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [map-points-display.data.trips :refer [get-country-code]]))

(def uri "https://nominatim.openstreetmap.org/search")

(defn- merge-with-name-address
  [{:keys [namedetails display_name] :as data}]
  (if-let [name (:name namedetails)]
    (let [address (-> display_name (split #", " 2) last)]
      (merge data {:name name :address address}))
    (merge data {:name display_name :address display_name})))

(defn- process-place
  [place-data]
  (-> place-data
      keywordize-keys
      (select-keys [:display_name :lat :lon :place_id :namedetails])
      merge-with-name-address))

(defn geocode [trip-id search]
  (let [country-code (get-country-code trip-id)
        response (http/get uri
                           {:query-params {:q search :countrycodes country-code
                                           :namedetails 1
                                           :format "json"}
                            :user-agent "Mapptsbot/0.9"})
        {:keys [status body error]} @response]
    (log/info "Received Nominatim response status" status)
    (if error
      (log/error error)
      (let [data (json/parse-string body)]
        (map process-place data)))))
