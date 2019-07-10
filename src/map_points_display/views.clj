(ns map-points-display.views
  (:require [clojure.string :as s]
            [environ.core :refer [env]]
            [net.cgrand.enlive-html :as html]
            [map-points-display.config :refer [config]])
  (:import (java.io StringReader)))

(def ^:private url-prefix
  (env :url-prefix))

(defn- url-for [path]
  (str url-prefix path))

;; Showing data table

(html/defsnippet group-item "templates/show.html"
  [:section.group :ul.items :> [:li html/first-child]]
  [table-id item]
  [:li] (html/do->
         (html/set-attr :data-lat (:lat item))
         (html/set-attr :data-lon (:lon item))
         (html/set-attr :data-category (:type item))
         (html/set-attr :id (:id item)))
  [:.item-name] (html/content (:name item))
  [:.item-address] (html/content (:address item))
  [:a.more-link] (html/set-attr :href (str "/data/" table-id "/" (:id item))))

(html/defsnippet group "templates/show.html"
  [:section.group]
  [table-id name items]
  [html/root] (html/set-attr :data-name name)
  [:h2.group-type] (html/content (s/capitalize name))
  [:ul.items] (html/content (map #(group-item table-id %) items)))

(html/defsnippet show-header "templates/show.html"
  [:head #{[[:link (html/but html/first-of-type)]] [:script]}]
  [])

(html/defsnippet show-content "templates/show.html"
  #{[:#map] [:#app]}
  [ctxt]
  [:#app :h1] (html/content (:message ctxt))
  [:#app :.places] (html/content (map #(apply group (:table-id ctxt) %) (:groups ctxt))))

(html/deftemplate show-table "templates/_base.html"
  [ctxt]
  [:head] (html/append (show-header))
  [:.layout] (html/content (show-content ctxt))
  [:body] (html/append (html/html [:script {:src (url-for (:js-url (config)))}]))
  [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css")))

;; Data tables list

(html/defsnippet data-tables-item "templates/index.html"
  [:ul#data_tables :> [:li.data-table html/first-child]]
  [item]
  [:li :> :a] (html/do->
               (html/content item)
               (html/set-attr :href (->> item (str "/data/") url-for)))
  [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css")))

(html/defsnippet data-tables-list "templates/index.html"
  [:.layout :> :main]
  [ctxt]
  [:ul#data_tables] (html/content (map data-tables-item (:tables ctxt))))

(html/deftemplate index-table "templates/_base.html"
  [ctxt]
  [:.layout] (html/content (data-tables-list ctxt))
  [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css")))

;; PoI show

(html/defsnippet poi-show-content "templates/show-poi.html"
  [:.layout :> :main]
  [ctxt]
  [:h1] (html/content (:name ctxt))
  [:#address] (html/content (:address ctxt))
  [:#notes] (if-let [notes (:notes ctxt)]
             (html/content notes)
             (html/substitute "")))

(html/deftemplate poi-show "templates/_base.html"
  [ctxt]
  [:.layout] (html/content (poi-show-content ctxt))
  [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css")))
