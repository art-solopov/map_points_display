(ns map-points-display.data.helpers
  (:require [clojure.string :as s]
            [map-points-display.config :refer [secrets]]))

(def schedule-line-re #"(?<dowfrom>\w+)(?:-(?<dowto>\w+))?\s+(?<timefrom>\d+(?::\d+)?)-(?<timeto>\d+(?::\d+)?)")

(defn- parse-schedule-row
  [row-match]
  (reduce (fn [m key]
            (merge m {(keyword key) (.group row-match key)}))
   {} ["dowfrom" "dowto" "timefrom" "timeto"]))

(defn parse-schedule
  [schedule]
  (->> schedule
       s/split-lines
       (map #(re-matcher schedule-line-re %))
       (filter #(.find %))
       (map parse-schedule-row)))

(def ^:const mapbox-zoom-level 15.5)
(def ^:const mapbox-img-size [400 380])

;; TODO: look into better string formatting
(def mapbox-static-map-base-url
  "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/$lon,$lat,$zoom,0,0/$widthx$height?access_token=$token")

(defn map-url
  [{:keys [lat lon]}]
  (let [[w h] mapbox-img-size
        token (:mapbox-api-key (secrets))]
    (-> mapbox-static-map-base-url
        (s/replace "$lon" (str lon))
        (s/replace "$lat" (str lat))
        (s/replace "$zoom" (str mapbox-zoom-level))
        (s/replace "$width" (str w))
        (s/replace "$height" (str h))
        (s/replace "$token" token))))
