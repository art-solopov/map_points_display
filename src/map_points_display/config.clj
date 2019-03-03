(ns map-points-display.config
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]]))

(defn- read-config []
  (let [name (env :app-env)
        full-name (str "config/" name ".edn")]
    (-> full-name io/resource slurp edn/read-string)))

(def config (delay (read-config)))
