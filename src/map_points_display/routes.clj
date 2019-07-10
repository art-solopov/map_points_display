(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [tables (data/tables-list)]
         (views/index-table {:tables tables})))
  (GET "/data/:data-table" [data-table]
       (let [groups (data/load-data data-table)]
         (views/show-table {:groups groups :message data-table :table-id data-table})))
  (GET "/data/:data-table/:id" [data-table id]
       (str "Data table " data-table " id " id))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
