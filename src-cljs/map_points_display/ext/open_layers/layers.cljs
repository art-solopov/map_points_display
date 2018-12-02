(ns map-points-display.ext.open-layers.layers
  (:require [map-points-display.ext.open-layers :refer [ol]]))

(def ^:private layer (.-layer ol))
(def ^:private Tile (.-Tile layer))
(def ^:private Vector (.-Vector layer))
(def ^:private source-OSM (.. ol -source -OSM))
(def ^:private source-Vector (.. ol -source -Vector))
(def ^:private Style (.. ol -style -Style))
(def ^:private Icon (.. ol -style -Icon))

(def base-layer
  (new Tile (js-obj "source" (source-OSM.))))

(def base-icon-config {:src "/icon.png"
                       :size (array 100 100)
                       :scale 0.3})

(defn icon [config]
  (let [icon-config (->> config (merge base-icon-config) clj->js)]
    (new Icon icon-config)))

(defn layer-config [icon-config]
  (new Style (js-obj
              "image" (icon icon-config))))

(defn vec-layer [features style]
  (let [source (new source-Vector (js-obj "features" features))
        config {:source source :style style}]
    (new Vector (clj->js config))))
