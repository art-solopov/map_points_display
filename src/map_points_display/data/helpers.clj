(ns map-points-display.data.helpers
  (:require [clojure.string :as s]
            [map-points-display.config :refer [secrets]]))

(def schedule-line-re #"(?<dowfrom>\w+)(?:-(?<dowto>\w+))?\s+(?<timefrom>\d+(?::\d+)?)-(?<timeto>\d+(?::\d+)?)")

(defn- parse-schedule-row
  [^java.util.regex.Matcher row-match]
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
(def ^:const mapbox-img-sizes
  {:big [400 380]
   :small [300 300]})

;; TODO: look into better string formatting
(def mapbox-static-map-base-url
  "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/pin-l($lon,$lat)/$lon,$lat,$zoom,0,0/$widthx$height?access_token=$token")

(defn map-url
  [{:keys [lat lon]} img-size]
  (let [[w h] (mapbox-img-sizes img-size)
        token (:mapbox-api-key @secrets)]
    (-> mapbox-static-map-base-url
        (s/replace "$lon" (str lon))
        (s/replace "$lat" (str lat))
        (s/replace "$zoom" (str mapbox-zoom-level))
        (s/replace "$width" (str w))
        (s/replace "$height" (str h))
        (s/replace "$token" token))))
