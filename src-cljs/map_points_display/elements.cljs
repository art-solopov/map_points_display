(ns map-points-display.elements
  (:require [map-points-display.layers :as layers]))

(defn on-el-mouse-enter [point category]
  (fn [t]
    (println t)
    (.setStyle point (layers/v-layer-hover-styles category))))

(defn on-el-mouse-leave [point category]
  (fn [t]
    (println t)
    (.setStyle point nil)))
