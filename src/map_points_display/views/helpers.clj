(ns map-points-display.views.helpers
  (:require [net.cgrand.enlive-html :as html]
            [environ.core :refer [env]]
            [map-points-display.config :refer [secrets]]))

(def ^:private url-prefix
  (env :url-prefix))

(defn url-for [path]
  (str url-prefix path))

(def acknowledgements
  ["© Artemiy Solopov, 2019"
   "All map data by OpenStreetMaps"
   "Icons are based on icons from <a href=\"https://icomoon.io/\">Icomoon</a>"])

(defmacro template-from-base
  [name & body]
  `(html/deftemplate ~name "templates/_base.html"
     [~(quote ctxt)]
     [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css"))
     [:footer#main_footer :> :p] (html/clone-for [~'ack acknowledgements]
                                                 (html/html-content ~'ack))
     ~@body))

(defn map-tiles-base-url []
  (str "https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}"
       "?access_token="
       (:mapbox-api-key (secrets))))

(def map-tiles-attribution
  "© <a href=\"https://www.mapbox.com/about/maps/\">Mapbox</a> © <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a> <strong><a href=\"https://www.mapbox.com/map-feedback/\" target=\"_blank\">Improve this map</a></strong>")
