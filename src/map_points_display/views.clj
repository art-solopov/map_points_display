(ns map-points-display.views
  (:require [clojure.string :as s]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to javascript-tag unordered-list image]]
            [map-points-display.config :refer [config]]
            [map-points-display.views.helpers :refer [map-tiles-base-url map-tiles-attribution acknowledgements normalize-timestr]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]])
  (:import (java.io StringReader)))

(defn layout
  [{:keys [main layout-class extra-head extra-body]}]
  (html5 [:head
          [:meta {:name "viewport" :content "width=device-width; initial-scale=1"}]
          [:meta {:charset "UTF-8"}]
          [:title "Map points display"]
          [:link {:rel "stylesheet" :href "/css/app.css"}]
          extra-head
          ]
         [:body
          [:div {:class (s/join " " (remove nil? ["layout" layout-class])) }
           main]
          [:footer#main_footer.main-footer
           (map #(vector :p %) acknowledgements)]
          extra-body]))

(defn- trips-list--item
  [{:keys [name]}]
  (let [href (str "/trip/" name)]
    [:li
     [:a.link {:href href} (h name)]]))

(defn trips-list
  [{:keys [trips]}]
  (let [main [:main (list [:h1 "Your trips"]
                          [:ul#data_tables.data-tables (map trips-list--item trips)])]]
    (layout {:main main})))

(defn- show-trip--item
  [{:keys [lat lon type id name address]}]
  [:li {:data-lat lat :data-lon lon :data-category type :id id}
   [:strong.item-name name]
   [:div
    [:div.item-address address]
    [:div
     (link-to {:class "pan-link"} "#" "Center map")
     "&nbsp;"
     (link-to {:class "more-link"} (str "/trip-point/" id) "More")]]])

(defn- show-trip--group
  [type items]
  [:section.group
   [:h2.group-type (s/capitalize type)]
   [:ul {:class "items"} (map show-trip--item items)]])

(def show-trip--extra-head
  (list [:link {:rel "stylesheet" :href "https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
                :integrity "sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
                :crossorigin true}]
        [:script {:src "https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
                  :integrity "sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
                  :crossorigin true}]))

(defn show-trip
  [{:keys [name groups]}]
  (let [main (list [:div#map.map {:data-map-url (map-tiles-base-url) :data-map-attribution map-tiles-attribution}
                    [:div#extra_attribution.extra-attribution]]
                   [:div#app.app
                    (link-to {:class "link"} "/" "Back")
                    [:h1 name]
                    [:article.places
                     (map #(apply show-trip--group %) groups)]])]
    (layout {:main main :layout-class "layout--show"
             :extra-head show-trip--extra-head
             :extra-body [:script {:src (:js-url @config)}]})))

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

(defn show-point
  [{:keys [lat lon type id name address notes schedule] :as point}]
  (let [main [:main
              (show-point--map-image point)
              [:header
               [:h1 name]
               [:p#address address]]
              (when-not (s/blank? notes) [:section (show-point--notes notes)])
              (when-not (s/blank? schedule) [:table.schedule (map show-point--schedule-row (parse-schedule schedule))])]]
    (layout {:main main})))
