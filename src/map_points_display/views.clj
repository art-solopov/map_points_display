(ns map-points-display.views
  (:require [clojure.string :as s]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5]]
            [map-points-display.config :refer [config]]
            [map-points-display.views.helpers :refer [url-for map-tiles-base-url map-tiles-attribution acknowledgements]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]])
  (:import (java.io StringReader)))

(defn layout
  [{:keys [main extra-scripts layout-class]}]
  (println layout-class)
  (html5 [:head
          [:meta {:name "viewport" :content "width=device-width; initial-scale=1"}]
          [:meta {:charset "UTF-8"}]
          [:title "Map points display"]
          [:link {:rel "stylesheet" :href (url-for "/css/app.css")}]
          ]
         [:body
          [:div {:class (s/join " " (remove nil? ["layout" layout-class])) }
           [:main main]]
          [:footer#main_footer.main-footer
           (map #(vector :p %) acknowledgements)]
          (map #(vector :script {:src (url-for %)}) extra-scripts)]))

(defn- trips-list--item
  [{:keys [name]}]
  (let [href (->> name
                  (str "/trip/")
                  url-for)]
    [:li
     [:a.link {:href href} (h name)]]))

(defn trips-list
  [{:keys [trips]}]
  (let [main (list [:h1 "Your trips"]
                   [:ul#data_tables.data-tables (map trips-list--item trips)])]
    (layout {:main main})))
