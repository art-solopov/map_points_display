(ns map-points-display.views
  (:require [clojure.string :as s]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to javascript-tag unordered-list]]
            [map-points-display.config :refer [config]]
            [map-points-display.views.helpers :refer [url-for map-tiles-base-url map-tiles-attribution acknowledgements]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]])
  (:import (java.io StringReader)))

(defn layout
  [{:keys [main layout-class extra-head extra-body]}]
  (html5 [:head
          [:meta {:name "viewport" :content "width=device-width; initial-scale=1"}]
          [:meta {:charset "UTF-8"}]
          [:title "Map points display"]
          [:link {:rel "stylesheet" :href (url-for "/css/app.css")}]
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
  (let [href (->> name
                  (str "/trip/")
                  url-for)]
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
             :extra-body [:script {:src (url-for (:js-url @config))}]})))
