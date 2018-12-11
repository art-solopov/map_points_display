(ns map-points-display.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [groups (data/load-data data/default-data)]
         (views/home {:groups groups :message "Map app"})))
  (GET "/:data-file" [data-file]
       (let [groups (data/load-data (str data-file ".csv"))]
         (views/home {:groups groups :message (str "Map app: " data-file)})))
  (route/not-found "Not found"))
