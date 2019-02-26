(ns map-points-display.ext.leaflet
  (:require [oops.core :refer [oget ocall]]))

(def l (oget js/window "L"))

(def attribution
  "<a href=\"https://wikimediafoundation.org/wiki/Maps_Terms_of_Use\">Wikimedia</a>")

(def url-template
  "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png")

(def ^private tile-layer-options
  (clj->js {:attribution attribution}))

(defn make-map [lat lon zoom]
  (let [the-map (-> l
                (ocall "map" "map")
                (ocall "setView" (array lat lon) zoom))]
    (-> l
        (ocall "tileLayer" url-template tile-layer-options)
        (ocall "addTo" the-map))
    the-map))

