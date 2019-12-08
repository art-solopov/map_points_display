(ns map-points-display.http-server
  (:require [mount.core :refer [defstate]]
            [org.httpkit.server :refer [run-server]]
            [ring.util.response :refer [header]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.session.cookie :as session]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.logger :as logger]
            [clojure.tools.logging :as log]
            [map-points-display.config :refer [config secrets]]
            [map-points-display.data.users :as users]
            [map-points-display.routes :as routes]))

(defonce -server (atom nil))

(defn- wrap-set-ref-header
  [handler]
  (fn [request]
    (let [response (handler request)]
      (header response "Referrer-Policy" "origin-when-cross-origin"))))

(defn- get-session-user
  [session]
  (if-let [user-id (session "user-id")]
    (users/load-by-id user-id)))

(defn- wrap-user
  [handler]
  (fn [{:keys [session] :as request}]
    (handler (->> session
                  get-session-user
                  (assoc request :user)))))

(def handler
  (-> routes/app
      wrap-user
      wrap-params
      (wrap-session {:store (session/cookie-store {:key (:session-key @secrets)})})
      wrap-set-ref-header
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified
      logger/wrap-with-logger))

(defn start-server
  []
  (let [port (:port @config)]
    (log/info "Starting server on port" port)
    (reset! -server (run-server handler {:port port}))))

(defn stop-server
  []
  (when-not (nil? -server)
    (log/info "Stopping server")
    (@-server :timeout 100)
    (reset! -server nil)))

(defstate server
  :start (start-server)
  :stop (stop-server))
