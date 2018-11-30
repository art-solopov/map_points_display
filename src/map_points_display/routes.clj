(ns map-points-display.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [map-points-display.views :as views]))

(defroutes app
  (GET "/" [] (views/home {:message "Map app"})))
