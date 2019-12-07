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

