(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views.trips-list :as v-trips-list]
            [map-points-display.views.show-trip :as v-show-trip]
            [map-points-display.views.show-point :as v-show-point]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [trips (data/trips-list)]
         (v-trips-list/render {:trips trips})))
  (GET "/trip/:trip-name" [trip-name]
       (let [groups (data/load-trip-points trip-name)]
         (v-show-trip/render {:groups groups :name trip-name})))
  (GET "/trip-point/:id" [id]
       (if-let [poi (data/load-point (Long/parseLong id))]
         (v-show-point/render poi)
         {:status 404 :body "Not found"}))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
