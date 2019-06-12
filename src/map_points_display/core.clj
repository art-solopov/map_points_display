(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [map-points-display.routes :as routes]
            [map-points-display.data.periodic :as data-p]
            [environ.core :refer [env]])
  (:gen-class))

(defn init
  []
  (data-p/init))

(defn destroy
  []
  (data-p/shutdown))

(def handler
  (-> routes/app
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified))
