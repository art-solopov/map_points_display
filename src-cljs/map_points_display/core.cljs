(ns map-points-display.core
  (:require [map-points-display.data-points :as dp]
            [map-points-display.ext.leaflet :as leaflet]))

(enable-console-print!)

(def dpd (dp/read-data))
(def dpts (map :point dpd))

(defn -main []
  (leaflet/make-map 43.7 11.25 13))

(-main)
