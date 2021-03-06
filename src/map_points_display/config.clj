(ns map-points-display.config
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]]))

(defn- read-config []
  (let [name (env :app-env)
        full-name (str "config/" name ".edn")
        base-config (-> full-name io/resource slurp edn/read-string)]
    (if-let [extra-config-path (:extra-config base-config)]
      (merge base-config (-> extra-config-path slurp edn/read-string))
      base-config)))

(def config (delay (read-config)))

(defn- secrets-file [] (get @config :secrets-file "./secrets.edn"))
(defn- read-secrets []
  (-> (secrets-file) slurp edn/read-string))
(def secrets (delay (read-secrets)))
