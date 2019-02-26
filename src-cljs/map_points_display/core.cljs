(ns map-points-display.core
  (:require [map-points-display.data-points :as dp]
            [map-points-display.ext.leaflet :as leaflet]
            [map-points-display.events :refer
             [make-mouse-events-processor make-click-processor]]))

(enable-console-print!)

(def the-map (atom nil))
(def data-points (atom nil))

(defn avg-coord [data-points coord]
  (let [sum (->> data-points (map coord) (reduce +))
        ct (count data-points)]
    (/ sum ct)))

(defn -main []
  (reset! data-points (dp/read-data))
  (-> js/document (.getElementById "map") (.-innerHTML) (set! ""))
  (let [ds (map :data @data-points)
        center-lat (avg-coord ds :lat)
        center-lon (avg-coord ds :lon)
        entlv-proc (make-mouse-events-processor @data-points)
        click-proc (make-click-processor the-map)]
    (reset! the-map (leaflet/make-map center-lat center-lon 13))
    (doseq [point @data-points]
      (.addTo (:marker point) @the-map)
      (let [el (:el point)]
        (set! (.. el -dataset -elid) (:uuid point))
        (.addEventListener el "mouseenter" entlv-proc)
        (.addEventListener el "mouseleave" entlv-proc)
        (.addEventListener (.querySelector el "a") "click" click-proc)))))

(-main)
