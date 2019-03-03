(ns map-points-display.markers
  (:require [oops.core :refer [ocall]]
            [map-points-display.ext.leaflet :refer [l]]))

(def stroke-color-default "black")
(def stroke-color-hover "#be005f")

(def layer-colors {"museum" "#8affff"
                   "transport" "#aaff7f"
                   "accomodation" "#e29cff"
                   "sight" "#419eee"
                   "food" "#bc7d00"
                   "shop" "#8b8bcf"
                   "other" "#d1d1d1"})

(def ^private base-opts
  {:radius 8.5
   :fill true
   :fillOpacity 0.85
   :color stroke-color-default
   :weight 2})

(defn make-marker [{:keys [lat lon name category]}]
  (let [marker-conf (merge base-opts {:fillColor (layer-colors category)})
        marker (ocall l "circleMarker" (array lat lon) (clj->js marker-conf))]
    (ocall marker "bindPopup" (str name " [" category "]"))
    marker))
