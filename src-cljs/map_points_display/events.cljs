(ns map-points-display.events
  (:require [oops.core :refer [oget ocall]]
            [map-points-display.markers :refer [stroke-color-default
                                                stroke-color-hover]]))

(defn- event-info [element data-points]
  (let [el-id (uuid (oget element "dataset.elid"))
        data-pt (->> data-points (filter #(= (:uuid %) el-id)) first)]
    {:el-id el-id :data-pt data-pt}))

(defn make-mouse-events-processor [data-points]
  (fn [ev]
    (let [el (oget ev "target")
          evinfo (event-info el data-points)
          {:keys [el-id data-pt]} evinfo
          {:keys [marker data]} data-pt
          {:keys [category]} data
          style (case (oget ev "type")
                  "mouseenter" {:color stroke-color-hover}
                  "mouseleave" {:color stroke-color-default})]
      (ocall marker "setStyle" (clj->js style)))
    )
  )

(defn make-click-processor [map-atom]
  (fn [ev]
    (let [t (oget ev "target")
          el (oget t "parentElement")
          lat (js/Number (oget el "dataset.lat"))
          lon (js/Number (oget el "dataset.lon"))]
      (ocall ev "preventDefault")
      (ocall @map-atom "panTo" (array lat lon)))))
