(ns map-points-display.elements
  (:require [map-points-display.layers :as layers]))

(defn mouse-event-processor [data-points]
  (fn [ev]
    (let [el (.-target ev)
          el-uuid (.. el -dataset -id)
          data-pt (->> data-points (filter #(= (:uuid %) (uuid el-uuid))) first)
          {:keys [point data]} data-pt
          {:keys [category]} data
          style (case (.-type ev)
                  "mouseenter" (layers/v-layer-hover-styles category)
                  "mouseleave" nil)]
      (.setStyle point style))
    )
  )
