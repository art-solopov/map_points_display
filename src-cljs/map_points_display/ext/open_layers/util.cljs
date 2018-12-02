(ns map-points-display.ext.open-layers.util
  (:require [map-points-display.ext.open-layers :refer [ol]]))

(def proj (.-proj ol))
(def ^:private fromLonLat (.-fromLonLat proj))

(defn from-lon-lat [lat lon]
  (new fromLonLat (array lat lon)))

(def ^:private createStringXY (.. ol -coordinate -createStringXY))

(defn create-string-xy [fractionDigits]
  (createStringXY fractionDigits))
