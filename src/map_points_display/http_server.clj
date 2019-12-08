(ns map-points-display.http-server
  (:require [mount.core :refer [defstate]]
            [org.httpkit.server :refer [run-server]]
            [ring.util.response :refer [header]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.logger :as logger]
            [clojure.tools.logging :as log]
            [map-points-display.config :refer [config]]
            [map-points-display.data :refer [load-user]]
            [map-points-display.routes :as routes]))

(defonce -server (atom nil))

(defn- wrap-set-ref-header
  [handler]
  (fn [request]
    (let [response (handler request)]
      (header response "Referrer-Policy" "origin-when-cross-origin"))))

(defn- wrap-user
  [handler]
  (fn [request]
    (handler (assoc request :user (load-user 1)))))

(def handler
  (-> routes/app
      wrap-user
      wrap-session
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
