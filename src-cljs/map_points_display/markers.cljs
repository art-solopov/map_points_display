(ns map-points-display.markers
  (:require [map-points-display.ext.leaflet :refer [l]]))

(def stroke-color-default "black")
(def stroke-color-hover "goldenrod")

(def layer-colors {"museum" "#8affff"
                   "transport" "#aaff7f"
                   "accomodation" "#e29cff"
                   "sight" "#419eee"
                   "other" "#d1d1d1"})

(def ^private base-opts
  {:radius 7
   :fill true
   :fillOpacity 0.85
   :color stroke-color-default
   :weight 2})

(defn make-marker [{:keys [lat lon name category]}]
  (let [marker-conf (merge base-opts {:fillColor (layer-colors category)})
        marker (.circleMarker l (array lat lon) (clj->js marker-conf))]
    (.bindPopup marker (str name " [" category "]"))
    marker))
