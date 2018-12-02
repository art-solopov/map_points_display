(ns map-points-display.ext.open-layers.geom
  (:require [map-points-display.ext.open-layers :refer [ol]]
            [map-points-display.ext.open-layers.util :refer [from-lon-lat]]))

(def ^:private Feature (.-Feature ol))
(def ^:private Point (.. ol -geom -Point))

(defn point [{:keys [lat lon name]}]
  (let [pd {:geometry (new Point (from-lon-lat lat lon))
            :name name}]
    (new Feature (clj->js pd))))

(defn points [pts]
  (->> pts (map point) (apply array)))
