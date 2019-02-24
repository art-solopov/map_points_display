(ns map-points-display.core
  (:require [map-points-display.data-points :as dp]
            [map-points-display.ext.leaflet :as leaflet]
            [map-points-display.events :refer [make-mouse-events-processor]]))

(enable-console-print!)

(def the-map (atom nil))
(def data-points (atom nil))

(defn -main []
  (reset! data-points (dp/read-data))
  (-> js/document (.getElementById "map") (.-innerHTML) (set! ""))
  (let [ds (map :data @data-points)
        data-count (count ds)
        ; TODO: replace with transducers?
        center-lat (->> ds (map :lat) (map #(/ % data-count)) (reduce +))
        center-lon (->> ds (map :lon) (map #(/ % data-count)) (reduce +))
        entlv-proc (make-mouse-events-processor @data-points)]
    (reset! the-map (leaflet/make-map center-lat center-lon 13))
    (doseq [point @data-points]
      (.addTo (:marker point) @the-map)
      (let [el (:el point)]
        (set! (.. el -dataset -elid) (:uuid point))
        (.addEventListener el "mouseenter" entlv-proc)
        (.addEventListener el "mouseleave" entlv-proc)))))

(-main)
