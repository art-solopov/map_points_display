(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]
            [map-points-display.routes :as routes]
            [map-points-display.config :refer [config]]
            [environ.core :refer [env]])
  (:gen-class))

(def sessions-store (atom {}))

(def handler
  (-> routes/app
      (wrap-session {:store (memory-store sessions-store)})
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified))
