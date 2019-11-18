(ns map-points-display.core
  (:require [environ.core :refer [env]]
            [mount.core :as mount]
            [mount-up.core :as mu]
            [map-points-display.http-server]
            [map-points-display.data.config]
            [map-points-display.data.periodic :as data-p])
  (:gen-class))

(mu/on-upndown :info mu/log :before)

(defn -main
  [& args]
  (-> (Runtime/getRuntime)
      (.addShutdownHook (Thread. mount/stop)))
  (mount/start))
