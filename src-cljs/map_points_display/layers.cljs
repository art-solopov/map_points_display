(ns map-points-display.layers
  (:require [map-points-display.ext.open-layers.layers :as oll]))

(def base-layer oll/base-layer)

(def layer-colors {"museum" "#8affff"
                    "transport" "#aaff7f"
                    "accomodation" "#e29cff"})

(defn- make-layer [color]
  (oll/vec-layer (oll/layer-config {:color color})))

(defn- tf-make-layer [[name color]]
  [name (make-layer color)])

(def v-layers
  (into {} (map tf-make-layer layer-colors)))

(def v-layer-objs
  (->> v-layers (map second) (map :layer)))

(defn add-point [{:keys [point data]}]
  (let [layer (v-layers (:category data))]
    (oll/add-feature layer point)))
