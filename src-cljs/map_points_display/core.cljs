(ns map-points-display.core
  (:require [map-points-display.ext.open-layers :refer [ol ol-view]]
            [map-points-display.ext.open-layers.util :refer [from-lon-lat]]))

(enable-console-print!)

(defn make-view [lat lon zoom]
  (let [view-conf {:center (from-lon-lat lat lon)
                   :zoom zoom}]
    (ol-view view-conf)))

(defn make-controls []
  (let [control (.. ol -control)
        defaults (.-defaults control)
        MousePosition (.-MousePosition control)
        createStringXY (.. ol -coordinate -createStringXY)
        mouse-pos-control (new MousePosition
                               (js-obj
                                "coordinateFormat" (createStringXY 4)
                                "projection" "EPSG:4326"))]
    (.extend (defaults) (array mouse-pos-control))))

(defn init-map [el-id]
  (let [mapel (.getElementById js/document el-id)]
    (set! (.-innerHTML mapel) ""))

  (let [Map (.-Map ol)
        Tile (.. ol -layer -Tile)
        source-OSM (.. ol -source -OSM)
        map-config {:target el-id
                    :layers (array (Tile. (js-obj "source" (source-OSM.)))) 
                    :view (make-view 11.24 43.77 10)
                    :controls (make-controls)}]
    (new Map (clj->js map-config)))
  )

(defn get-app []
  (.getElementById js/document "app"))

(defn -main []
  (init-map "map"))


(-main)
