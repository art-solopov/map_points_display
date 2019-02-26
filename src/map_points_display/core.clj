(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [map-points-display.routes :as routes]
            [map-points-display.config :refer [config]]
            [environ.core :refer [env]])
  (:gen-class))

(defn- init-app []
  (if (= "development" (env :app-env))
    (wrap-reload #'routes/app)
    routes/app))

(defn- wrap-dev-files? []
  (if (:serve-target @config)
    (fn [app] (wrap-file app "./target/public"))
    identity))

(defn make-app []
  (-> (init-app)
      (wrap-resource "public")
      wrap-content-type
      ((wrap-dev-files?))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (env :app-env))
  (let [app (make-app)]
    (rj/run-jetty app (:jetty-config @config))))
