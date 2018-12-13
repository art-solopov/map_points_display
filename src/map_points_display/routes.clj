(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [files (data/list-data-files)]
         (views/index-file {:files files})))
  (GET "/data/:data-file" [data-file]
       (let [groups (data/load-data (str data-file ".csv"))]
         (views/show-file {:groups groups :message (s/capitalize data-file)})))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
