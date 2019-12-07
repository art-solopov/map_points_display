(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views.trips-list :as v-trips-list]
            [map-points-display.views.show-trip :as v-show-trip]
            [map-points-display.views.show-point :as v-show-point]
            [map-points-display.views :refer [->HiccupView]]
            [map-points-display.data :as data]))

(defroutes app
  (GET "/" []
       (let [trips (data/trips-list)]
         (->HiccupView :trips-list {:trips trips})))
  (GET "/trip/:trip-name" [trip-name]
       (let [groups (data/load-trip-points trip-name)]
         (->HiccupView :show-trip {:groups groups :name trip-name})))
  (GET "/trip-point/:id" [id]
       (if-let [poi (data/load-point (Long/parseLong id))]
         (->HiccupView :show-point poi)
         {:status 404 :body "Not found"}))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
