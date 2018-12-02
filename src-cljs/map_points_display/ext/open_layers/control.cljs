(ns map-points-display.ext.open-layers.control
  (:require [map-points-display.ext.open-layers :refer [ol]]
            [map-points-display.ext.open-layers.util :refer [create-string-xy]]))

(def ^:private control (.-control ol))

(def defaults (.-defaults control))
(defn mouse-position []
  (let [MousePosition (.-MousePosition control)]
    (new MousePosition
         (js-obj
          "coordinateFormat" (create-string-xy 4)
          "projection" "EPSG:4326"))))

