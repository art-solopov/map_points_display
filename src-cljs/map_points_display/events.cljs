(ns map-points-display.events
  (:require [map-points-display.markers :refer [stroke-color-default
                                                stroke-color-hover]]))

(defn- event-info [element data-points]
  (let [el-id (uuid (.. element -dataset -elid))
        data-pt (->> data-points (filter #(= (:uuid %) el-id)) first)]
    {:el-id el-id :data-pt data-pt}))

(defn make-mouse-events-processor [data-points]
  (fn [ev]
    (let [el (.-target ev)
          evinfo (event-info el data-points)
          {:keys [el-id data-pt]} evinfo
          {:keys [marker data]} data-pt
          {:keys [category]} data
          style (case (.-type ev)
                  "mouseenter" {:color stroke-color-hover}
                  "mouseleave" {:color stroke-color-default})]
      (.setStyle marker (clj->js style)))
    )
  )

(defn make-click-processor [map-atom]
  (fn [ev]
    (let [t (.-target ev)
          el (.-parentElement t)
          lat (js/Number (.. el -dataset -lat))
          lon (js/Number (.. el -dataset -lon))]
      (.preventDefault ev)
      (.panTo @map-atom (array lat lon)))))
