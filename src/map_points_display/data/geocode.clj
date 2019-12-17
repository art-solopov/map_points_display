(ns map-points-display.data.geocode
  (:require [clojure.string :refer [split]]
            [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [map-points-display.data.trips :refer [get-country-code]]))

(def uri "https://nominatim.openstreetmap.org/search")

(defn- merge-with-name-address
  [data]
  (let [[name address] (split (:display_name data) #", " 2)]
    (merge data {:name name :address address})))

(defn- process-place
  [place-data]
  (-> place-data
      (select-keys ["display_name" "lat" "lon" "place_id"])
      keywordize-keys
      merge-with-name-address))

(defn geocode [trip-id search]
  (let [country-code (get-country-code trip-id)
        response (http/get uri
                           {:query-params {:q search :countrycodes country-code
                                           :format "json"}
                            :user-agent "Mapptsbot/0.9"})
        {:keys [status body error]} @response]
    (log/info "Received Nominatim response status" status)
    (if error
      (log/error error)
      (let [data (json/parse-string body)]
        (map process-place data)))))
