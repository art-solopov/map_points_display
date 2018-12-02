(ns map-points-display.core
  (:require [map-points-display.ext.open-layers :refer [ol view]]
            [map-points-display.ext.open-layers.control :as control]
            [map-points-display.ext.open-layers.layers :as layers]
            [map-points-display.ext.open-layers.util :refer [from-lon-lat]]))

(enable-console-print!)

(defn make-view [lat lon zoom]
  (let [view-conf {:center (from-lon-lat lat lon)
                   :zoom zoom}]
    (view view-conf)))

(defn make-controls []
  (.extend (control/defaults) (array (control/mouse-position))))

(defn init-map [el-id]
  (let [mapel (.getElementById js/document el-id)]
    (set! (.-innerHTML mapel) ""))

  (let [Map (.-Map ol)
        map-config {:target el-id
                    :layers [layers/base-layer]
                    :view (make-view 11.24 43.77 10)
                    :controls (make-controls)}]
    (new Map (clj->js map-config)))
  )

(defn get-app []
  (.getElementById js/document "app"))

(defn -main []
  (init-map "map"))


(-main)
