(ns map-points-display.views.show-trip
  (:require [clojure.string :as s]
            [hiccup.element :refer [link-to]]
            [map-points-display.views :refer [layout]]
            [map-points-display.views.helpers :refer [map-tiles-base-url map-tiles-attribution]]
            [map-points-display.config :refer [config]]))

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

(defn render
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
