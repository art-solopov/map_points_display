(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as rsp]
            [clojure.tools.logging :as log]
            [ring.util.response :refer [resource-response]]
            [map-points-display.views :refer [->HiccupView]]
            [map-points-display.data :as data]))

(def ^:private default-not-found
  (-> {:status 404 :body "Not found"} (rsp/content-type "text/plain")))

(defn user-protected
  [user]
  (routes
   (GET "/trip/:trip-name" [trip-name]
        (if-let [trip (data/load-trip trip-name)]
          (let [groups (data/load-trip-points (:id trip))]
            (->HiccupView :show-trip {:groups groups :name trip-name}))
          default-not-found))
   (GET "/trip-point/:id" [id]
        (if-let [poi (data/load-point (Long/parseLong id))]
          (->HiccupView :show-point poi)
          default-not-found))))

(defn user-required-wrap
  [handler]
  (fn [{:keys [user] :as request}]
    (if-not (nil? user)
      (handler request)
      (-> {:status 403 :body "Forbidden"} (rsp/content-type "text/plain")))))

(defroutes app
  (context "/" {:keys [user]} (wrap-routes (user-protected [user]) user-required-wrap))
  (GET "/" {:keys [user]}
       (if-not (nil? user)
         ;; TODO: add fetching by user when private/public is used
         (let [trips (data/trips-list)]
           (->HiccupView :trips-list {:trips trips}))
         (->HiccupView :welcome {})))
  (route/not-found "Not found"))
