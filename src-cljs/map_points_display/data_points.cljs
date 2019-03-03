(ns map-points-display.data-points
  (:require [oops.core :refer [oget]]
            [map-points-display.markers :refer [make-marker]]))

(defn- read-datum [el]
  {:name (-> el (.querySelector ".item-name") .-innerText)
   :lat (js/Number (oget el "dataset.lat"))
   :lon (js/Number (oget el "dataset.lon"))
   :category (oget el "dataset.category")})

(defn- get-els []
  (array-seq (.querySelectorAll js/document ".group .items li")))

(defn read-data []
  (let [els (get-els)
        el-data (map read-datum els)
        el-markers (map make-marker el-data)]
    (map #(array-map :el %1 :data %2 :marker %3 :uuid %4)
         els el-data el-markers (repeatedly random-uuid))))
