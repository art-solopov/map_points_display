(ns map-points-display.events
  (:require [map-points-display.markers :refer [stroke-color-default
                                                stroke-color-hover]]))

(defn- event-info [event data-points]
  (let [el (.-target event)
        el-id (uuid (.. el -dataset -elid))
        data-pt (->> data-points (filter #(= (:uuid %) el-id)) first)]
    {:el el :el-id el-id :data-pt data-pt}))

(defn make-mouse-events-processor [data-points]
  (fn [ev]
    (let [evinfo (event-info ev data-points)
          {:keys [el el-id data-pt]} evinfo
          {:keys [marker data]} data-pt
          {:keys [category]} data
          style (case (.-type ev)
                  "mouseenter" {:color stroke-color-hover}
                  "mouseleave" {:color stroke-color-default})]
      (.setStyle marker (clj->js style)))
    )
  )
