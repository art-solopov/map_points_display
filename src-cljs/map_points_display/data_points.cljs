(ns map-points-display.data-points
  (:require [map-points-display.ext.open-layers.geom :refer [points]]))

(defn- read-datum [el]
  {:name (.-innerText el)
   :lat (js/Number (.. el -dataset -lat))
   :lon (js/Number (.. el -dataset -lon))
   :category (.. el -dataset -category)})

(defn- get-els []
  (array-seq (.querySelectorAll js/document ".group .items li")))

(defn- update-els [el uuid]
  (set! (.. el -dataset -id) uuid))

(defn read-data []
  (let [els (get-els)
        el-data (map read-datum els)
        el-pts (points el-data)]
    (map #(array-map :el %1 :data %2 :point %3 :uuid %4)
         els el-data el-pts (repeatedly random-uuid))))

