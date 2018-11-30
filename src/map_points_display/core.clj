(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.reload :refer [wrap-reload]]
            [map-points-display.routes :as routes])
  (:gen-class))

(def app
  (-> routes/app
      (wrap-resource "public")
      (wrap-file "./target/public")
      wrap-reload))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (rj/run-jetty app {:port 9400})
  )
