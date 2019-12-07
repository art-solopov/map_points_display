(ns map-points-display.views.trips-list
  (:require [hiccup.core :refer [h]]
            [map-points-display.views.helpers ]))

(defn- trips-list--item
  [{:keys [name]}]
  (let [href (str "/trip/" name)]
    [:li
     [:a.link {:href href} (h name)]]))

(defn render
  [{:keys [trips]}]
  (let [main [:main (list [:h1 "Your trips"]
                          [:ul#data_tables.data-tables (map trips-list--item trips)])]]
    {:main main}))
