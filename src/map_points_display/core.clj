(ns map-points-display.core
  (:require [ring.adapter.jetty :as rj])
  (:gen-class))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello world!"})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (rj/run-jetty handler {:port 9400})
  )
