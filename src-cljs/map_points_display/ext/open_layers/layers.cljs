(ns map-points-display.ext.open-layers.layers
  (:require [map-points-display.ext.open-layers :refer [ol]]))

(def ^:private layer (.-layer ol))
(def ^:private Tile (.-Tile layer))
(def ^:private source-OSM (.. ol -source -OSM))

(def base-layer
  (new Tile (js-obj "source" (source-OSM.))))

