(ns map-points-display.core
  (:require [map-points-display.data-points :as dp]
            [map-points-display.ext.leaflet :as leaflet]))

(enable-console-print!)

(def dpd (dp/read-data))

(defn -main []
  (-> js/document (.getElementById "map") (.-innerHTML) (set! ""))
  (let [ds (map :data dpd)
        data-count (count ds)
        ; TODO: replace with transducers?
        center-lat (->> ds (map :lat) (map #(/ % data-count)) (reduce +))
        center-lon (->> ds (map :lon) (map #(/ % data-count)) (reduce +))
        the-map (leaflet/make-map center-lat center-lon 13)]
    (doseq [point dpd]
      (.addTo (:marker point) the-map))))

(-main)
