(ns map-points-display.routes
  (:require [clojure.string :as s]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response redirect]]
            [map-points-display.views :as views]
            [map-points-display.data :as data]
            [map-points-display.auth :refer [authenticate]])
  (:import [java.time Instant]))

(defn- wrap-auth-redirect [handler]
  (fn [request]
    (let [session (:session request)]
      (if (empty? session)
        (redirect "/login")
        (handler request)))))

(defroutes -with-login-routes
  (GET "/data/:data-file/edit" [data-file] (list "<html><body><h1>Data file " data-file " edit</h1></body></html>")))

(def with-login-routes (wrap-auth-redirect -with-login-routes))

(defroutes app
  (GET "/" [:as {s :session}]
       (let [files (data/list-data-files)]
         {:body (views/index-file {:files files})
          :headers {"Content-Type" "text/html"}}))
  (GET "/data/:data-file" [data-file]
       (let [groups (data/load-data (str data-file ".csv"))]
         (views/show-file {:groups groups :message (s/capitalize data-file)})))
  (GET "/login" []
       (views/login-form {}))
  (POST "/login" [:as {form-params :form-params}]
        (let [login (form-params "login")
              password (form-params "password")]
          (if (authenticate login password)
            (-> (redirect "/")
                (assoc-in [:session :authenticated?] true)
                (assoc-in [:session :created-at] (Instant/now))
              )
            (views/login-form {:login login}))))
  (ANY "*" r (with-login-routes r))
  (GET "/templates/:filename" [filename]
       (resource-response filename {:root "templates"}))
  (route/not-found "Not found"))
