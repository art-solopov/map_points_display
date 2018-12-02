(ns map-points-display.ext.open-layers)

(def ol (.-ol js/window))
(def ^:private View (.-View ol))

(defn view [config]
  "TODO: maybe try to auto convert the center?"
  (new View (clj->js config)))
