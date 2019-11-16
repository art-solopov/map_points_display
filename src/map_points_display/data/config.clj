(ns map-points-display.data.config
  (:require [map-points-display.config :refer [secrets]]))

(defn db-config []
  (:dbspec @secrets))
