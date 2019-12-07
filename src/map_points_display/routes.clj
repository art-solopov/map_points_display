(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views :as views]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [trips (data/trips-list)]
         (views/trips-list {:trips trips})))
  (GET "/trip/:trip-name" [trip-name]
       (let [groups (data/load-trip-points trip-name)]
         (views/show-trip {:groups groups :name trip-name})))
  (GET "/trip-point/:id" [id]
       (if-let [poi (data/load-point (Long/parseLong id))]
         (views/show-point poi)
         {:status 404 :body "Not found"}))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
