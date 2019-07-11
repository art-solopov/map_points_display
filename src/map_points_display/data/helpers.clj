(ns map-points-display.data.helpers
  (:require [clojure.string :refer [split-lines replace]]
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
       split-lines
       (map #(re-matcher schedule-line-re %))
       (filter #(.find %))
       (map parse-schedule-row)))

(def ^:const mapbox-zoom-level 15.5)
(def ^:const mapbox-img-size [400 380])

;; TODO: look into better string formatting
(def mapbox-map-base-url
  "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/$lon,$lat,$zoom,0,0/$widthx$height?access_token=$token")

(defn map-url
  [{:keys [lat lon]}]
  (let [[w h] mapbox-img-size
        token (:mapbox-api-key (secrets))]
    (-> mapbox-map-base-url
        (replace "$lon" (str lon))
        (replace "$lat" (str lat))
        (replace "$zoom" (str mapbox-zoom-level))
        (replace "$width" (str w))
        (replace "$height" (str h))
        (replace "$token" token))))
