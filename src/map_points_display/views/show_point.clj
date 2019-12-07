(ns map-points-display.views.show-point
  (:require [clojure.string :as s]
            [hiccup.element :refer [image]]
            [map-points-display.views.helpers :refer [map-tiles-base-url map-tiles-attribution acknowledgements normalize-timestr]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]]
            [map-points-display.views :refer [layout]]
            ))

(defn- show-point--map-image
  [point]
  [:aside.map-image-container
   (image {:id "map_image" :class "map_image"} (map-url point))
   (image {:id "map_marker" :class "marker"} (str "/icons/" (:type point) ".png"))])

(defn- show-point--notes
  [notes]
  (->> notes s/split-lines
       (filter (complement s/blank?))
       (map #(vector :p.notes-p %))))

(defn- show-point--schedule-wday
  [wday]
  (let [wday-short (->> wday
                        (take 3)
                        (apply str))]
    [:span {:class (s/lower-case wday-short)} wday]))

(defn- show-point--schedule-row
  [{:keys [dowfrom dowto timefrom timeto]}]
  [:tr
   [:td
    (show-point--schedule-wday dowfrom)
    (when-not (nil? dowto)
      (list "–" (show-point--schedule-wday dowto)))]
   [:td
    (normalize-timestr timefrom)
    "–"
    (normalize-timestr timeto)
    ]])

(defn render
  [{:keys [lat lon type id name address notes schedule] :as point}]
  (let [main [:main
              (show-point--map-image point)
              [:header
               [:h1 name]
               [:p#address address]]
              (when-not (s/blank? notes) [:section (show-point--notes notes)])
              (when-not (s/blank? schedule) [:table.schedule (map show-point--schedule-row (parse-schedule schedule))])]]
    (layout {:main main})))
