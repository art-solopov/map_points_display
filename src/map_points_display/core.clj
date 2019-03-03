(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [map-points-display.routes :as routes]
            [map-points-display.config :refer [config]]
            [environ.core :refer [env]])
  (:gen-class))

(def ^:private handler-base
  (-> routes/app
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified))

(def handler
  (if (:serve-target @config)
    (wrap-file handler-base "./target/public")
    handler-base))
