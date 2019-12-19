(ns map-points-display.views.points
  (:require [clojure.string :as s]
            [hiccup.element :refer [image link-to]]
            [hiccup.form :refer [form-to] :as form]
            [map-points-display.data.points :as data]
            [map-points-display.views.helpers :refer [map-tiles-base-url map-tiles-attribution acknowledgements normalize-timestr]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]]
            [map-points-display.config :refer [config]]))

(defn- show-point--map-image
  [point]
  [:aside.map-image-container
   (image {:id "map_image" :class "map_image"} (map-url point :big))])

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

(defn show
  [{:keys [lat lon type id name address notes schedule] :as point}]
  (let [main [:main
              (show-point--map-image point)
              [:header
               [:h1 name]
               [:p#address address]]
              (when-not (s/blank? notes) [:section (show-point--notes notes)])
              (when-not (s/blank? schedule) [:table.schedule (map show-point--schedule-row (parse-schedule schedule))])]]
    {:main main}))

(defn form
  [{:keys [fields method url trip]}]
  (let [form (form-to [method url]
                      [:div
                       (form/label "name" "Name")
                       (form/text-field {:required true} "name" (fields "name"))]
                      [:div
                       (form/label "address" "Address")
                       (form/text-area "address" (fields "address"))]
                      [:div
                       [:div [:button#btn_geocode {:type "button"
                                                   :data-link "/api/geocode"
                                                   :data-trip-id (:id trip)}
                              "Geocode"]]
                       [:div#geocode_results.flex-shelf.geocode-results]
                       ]
                      [:div
                       (form/label "lat" "Latitude")
                       (form/text-field {:required true :pattern "\\d+\\.?\\d*"} "lat" (fields "lat"))]
                      [:div
                       (form/label "lon" "Longitude")
                       (form/text-field {:required true :pattern "\\d+\\.?\\d*"} "lon" (fields "lon"))]
                      [:div
                       (form/label "type" "Type")
                       (form/drop-down {:required true} "type" data/types (fields "type"))]
                      [:div
                       (form/label "notes" "Notes")
                       (form/text-area "notes" (fields "notes"))]
                      [:div
                       (form/label "schedule" "Schedule")
                       (form/text-area "schedule" (fields "schedule"))]
                      [:div
                       (form/submit-button "Save")])
        gc-template [:template#geocode_result_template
                     [:div.geocode-result
                      [:div
                       [:button {:type "button" :class "gc-save"} "Save"]]
                      [:div.name ""]
                      [:div.address ""]
                       (image {:class "preview"} (map-url {:lat "$lat" :lon "$lon"} :small))
                      ]]]
    {:main (list [:div (link-to {:class "link"} (str "/trip/" (:name trip)) "Back")]
                 form)
     :extra-body (list gc-template
                       [:script {:src "/js/geocode.js"}])}))
