(ns map-points-display.views.helpers
  (:require [clojure.string :as s]
            [environ.core :refer [env]]
            [map-points-display.config :refer [secrets]]))

(def acknowledgements
  ["© Artemiy Solopov, 2019"
   "All map data by OpenStreetMaps"
   "Icons are based on icons from <a href=\"https://icomoon.io/\">Icomoon</a>"])

(defn map-tiles-base-url []
  (str "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/256/{z}/{x}/{y}"
       "?access_token="
       (:mapbox-api-key @secrets)))

(defn normalize-timestr
  [timestr]
  (if (s/includes? timestr ":")
    timestr
    (str timestr ":00")))

(def map-tiles-attribution
  "© <a href=\"https://www.mapbox.com/about/maps/\">Mapbox</a> © <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a> <strong><a href=\"https://www.mapbox.com/map-feedback/\" target=\"_blank\">Improve this map</a></strong>")
