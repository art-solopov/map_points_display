(ns map-points-display.core
  (:require [map-points-display.ext.open-layers :refer [ol view]]
            [map-points-display.ext.open-layers.control :as control]
            [map-points-display.layers :as layers]
            [map-points-display.data-points :as dp]
            [map-points-display.elements :refer [on-el-mouse-enter on-el-mouse-leave]]
            [map-points-display.ext.open-layers.util :refer [from-lon-lat]]))

(enable-console-print!)

(def dpd (dp/read-data))
(def dpts (map :point dpd))

(defn make-view [lat lon zoom]
  (let [view-conf {:center (from-lon-lat lat lon)
                   :zoom zoom}]
    (view view-conf)))

(defn make-controls []
  (.extend (control/defaults) (array (control/mouse-position))))

(defn init-map [el-id center-lat center-lon]
  (let [mapel (.getElementById js/document el-id)]
    (set! (.-innerHTML mapel) ""))

  (let [Map (.-Map ol)
        map-config {:target el-id
                    :layers (conj layers/v-layer-objs layers/base-layer)
                    :view (make-view center-lat center-lon 12)
                    :controls (make-controls)}]
    (new Map (clj->js map-config)))
  )

(defn- set-el-ids [data-points]
  (doseq [{:keys [uuid el]} data-points]
    (set! (.. el -dataset -id) uuid)))

(defn- add-event-listeners [data-points]
  (doseq [{:keys [el point data]} data-points]
    (.addEventListener el "mouseenter" (on-el-mouse-enter point (:category data)))
    (.addEventListener el "mouseleave" (on-el-mouse-leave point (:category data)))))

(defn get-app []
  (.getElementById js/document "app"))

(defn -main []
  (set-el-ids dpd)
  (add-event-listeners dpd)
  (doseq [point dpd]
    (layers/add-point point))
  (let [d (map :data dpd)
        data-count (count d)
        center-lat (->> d (map :lat) (map #(/ % data-count)) (reduce +))
        center-lon (->> d (map :lon) (map #(/ % data-count)) (reduce +))]
    (init-map "map" center-lat center-lon)))

(-main)
