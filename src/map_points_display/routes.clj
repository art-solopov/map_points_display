(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as rsp]
            [ring.util.request :as req]
            [clojure.tools.logging :as log]
            [ring.util.response :refer [resource-response]]
            [cheshire.core :as json]
            [map-points-display.views :refer [hiccup-view]]
            [map-points-display.data :as data]
            [map-points-display.data.users :as users]
            [map-points-display.data.trips :as trips]
            [map-points-display.data.points :as points]))

(def ^:private default-not-found
  (-> {:status 404 :body "Not found"} (rsp/content-type "text/plain")))

(defn trip-routes
  [user trip]
  (routes
   (GET "/" []
        (let [groups (data/load-trip-points (:id trip))]
          (hiccup-view :show-trip {:groups groups :name (:name trip)})))
   (GET "/add-point" request
        (hiccup-view :point-form {:fields {} :method :post :url (:uri request)
                                  :trip trip}))
   (POST "/add-point" request
         (do
           (points/create user (:id trip) (:form-params request))
           (rsp/redirect (str "/trip/" (:name trip)))))))

(defroutes api
  (POST "/geocode" {:keys [json-body]}
        (-> {:status 200 :body (json/generate-string {:input json-body})}
            (rsp/content-type "application/json"))
        )
  )

(defn user-required-wrap
  [handler]
  (fn [{:keys [user] :as request}]
    (if-not (nil? user)
      (handler request)
      (-> {:status 403 :body "Forbidden"} (rsp/content-type "text/plain")))))

(defn wrap-parse-json
  [handler]
  (fn [request]
    (if (and (:body request)
             (= (req/content-type request) "application/json"))
      (handler (assoc request :json-body (-> request req/body-string json/parse-string)))
      (handler request))))

(defn user-protected
  [user]
  (routes
   (context "/trip/:trip-name" [trip-name]
            (if-let [trip (data/load-trip trip-name)]
              (trip-routes user trip)
              default-not-found))
   (context "/api" [] (wrap-routes api wrap-parse-json))
   (GET "/new-trip" []
        (hiccup-view :trip-form {:fields {} :method :post :url "/new-trip"}))
   (POST "/new-trip" {:keys [form-params]}
         ;; TODO add data validation
         (do
           (trips/create user form-params)
           (rsp/redirect "/")))
   (GET "/trip-point/:id" [id]
        (if-let [poi (data/load-point (Long/parseLong id))]
          (hiccup-view :show-point poi)
          default-not-found))))

(defroutes app
  (context "/" {:keys [user]} (wrap-routes (user-protected [user]) user-required-wrap))
  (GET "/" {:keys [user]}
       (if-not (nil? user)
         ;; TODO: add fetching by user when private/public is used
         (let [trips (data/trips-list)]
           (hiccup-view :trips-list {:trips trips}))
         (hiccup-view :welcome {})))
  (GET "/login" []
       (hiccup-view :login-form {}))
  (POST "/login" {:keys [form-params]}
        (let [username (form-params "username")
              password (form-params "password")]
          (if-let [user (users/authenticate username password)]
            (-> (rsp/redirect "/")
                (assoc :session {"user-id" (:id user)}))))
        )
  (route/not-found "Not found"))
