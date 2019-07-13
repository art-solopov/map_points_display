(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.util.response :refer [header]]
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

(defn- wrap-set-ref-header
  [handler]
  (fn [request]
    (let [response (handler request)]
      (header response "Referrer-Policy" "origin-when-cross-origin"))))

(def handler
  (-> routes/app
      wrap-set-ref-header
      (wrap-resource "public")
      wrap-content-type
      wrap-not-modified))
