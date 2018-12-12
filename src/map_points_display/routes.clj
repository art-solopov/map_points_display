(ns map-points-display.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [groups (data/load-data data/default-data)]
         (views/home {:groups groups :message "Map app"})))
  (GET "/:data-file" [data-file]
       (let [groups (data/load-data (str data-file ".csv"))]
         (views/home {:groups groups :message (str "Map app: " data-file)})))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
