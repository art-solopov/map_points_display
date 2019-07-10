(ns map-points-display.data.helpers
  (:require [clojure.string :refer [split-lines]]))

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
