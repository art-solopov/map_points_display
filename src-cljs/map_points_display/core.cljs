(ns map-points-display.core
  (:require [map-points-display.ext.open-layers :refer [ol view]]
            [map-points-display.ext.open-layers.control :as control]
            [map-points-display.layers :as layers]
            [map-points-display.data-points :as dp]
            [map-points-display.ext.open-layers.util :refer [from-lon-lat]]))

(enable-console-print!)

(def dpd (dp/read-data))
(def dpts (map :point dpd))

;; (def test-layer (layers/vec-layer (layers/layer-config {:color "purple"})))

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
                    :layers (conj layers/v-layer-objs layers/base-layer)
                    :view (make-view 11.24 43.77 12)
                    :controls (make-controls)}]
    (new Map (clj->js map-config)))
  )

(defn- set-el-ids [data-points]
  (doseq [{:keys [uuid el]} data-points]
    (set! (.. el -dataset -id) uuid)))

(defn get-app []
  (.getElementById js/document "app"))

(defn -main []
  (set-el-ids dpd)
  (doseq [point dpd]
    (layers/add-point point))
  (init-map "map"))

(-main)
