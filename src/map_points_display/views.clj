(ns map-points-display.views
  (:require [clojure.string :as s]
            [hiccup.core :refer [h]]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]
            [compojure.response :refer [render Renderable]]
            [map-points-display.views.helpers :refer [map-tiles-base-url map-tiles-attribution acknowledgements normalize-timestr]]
            [map-points-display.views.trips-list :as v-trips-list]
            [map-points-display.views.show-trip :as v-show-trip]
            [map-points-display.views.show-point :as v-show-point]
            [map-points-display.views.welcome :as v-welcome]
            [map-points-display.views.auth :as v-auth])
  (:import (java.io StringReader)))

(def views-by-name {:trips-list v-trips-list/render
                    :show-trip v-show-trip/render
                    :show-point v-show-point/render
                    :welcome v-welcome/render
                    :login-form v-auth/login-form})

(defn- navbar
  [{:keys [user]}]
  [:nav.navbar
   [:span.navbar-brand "Map points display"]
   [:div.navbar-right
    (if (nil? user)
      (link-to "/login" "Login")
      (list [:span (:username user)]
            (link-to "/logout" "Logout")))
    ]
   ])

(defn layout
  [{:keys [main layout-class extra-head extra-body]} request]
  (html5 [:head
          [:meta {:name "viewport" :content "width=device-width; initial-scale=1"}]
          [:meta {:charset "UTF-8"}]
          [:title "Map points display"]
          [:link {:rel "stylesheet" :href "/css/app.css"}]
          extra-head
          ]
         [:body
          (navbar request)
          [:div {:class (s/join " " (remove nil? ["layout" layout-class])) }
           main]
          [:footer#main_footer.main-footer
           (map #(vector :p %) acknowledgements)]
          extra-body]))

(defrecord HiccupView
    [^clojure.lang.Keyword view-name view-data]
  Renderable
  (render [this request]
    (let [view-fn (views-by-name (:view-name this))
          view-fragments (view-fn view-data)
          html (layout view-fragments request)]
      (render html request))))
