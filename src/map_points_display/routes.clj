(ns map-points-display.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [all-d (data/load-data data/default-data)
             groups (group-by :type all-d)]
         (views/home {:groups groups :message "Map app"}))))
