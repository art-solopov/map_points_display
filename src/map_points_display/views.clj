(ns map-points-display.views
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [map-points-display.config :refer [config]]
            [map-points-display.views.helpers :refer [template-from-base url-for map-tiles-base-url map-tiles-attribution]]
            [map-points-display.data.helpers :refer [parse-schedule map-url]])
  (:import (java.io StringReader)))

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
  [:#map] (html/do->
           (html/set-attr :data-map-url (map-tiles-base-url))
           (html/set-attr :data-map-attribution map-tiles-attribution))
  [:#app :h1] (html/content (:message ctxt))
  [:#app :.places] (html/content (map #(apply group (:table-id ctxt) %) (:groups ctxt))))

(template-from-base
 show-table
 [:head] (html/append (show-header))
 [:.layout] (html/do->
             (html/add-class "layout--show")
             (html/content (show-content ctxt)))
 [:body] (html/append (html/html [:script {:src (url-for (:js-url (config)))}])))

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

(template-from-base
 index-table
 [:.layout] (html/content (data-tables-list ctxt)))

;; PoI show

(html/defsnippet poi-schedule-wday "templates/show-poi.html"
  [[:.schedule-row html/first-child] :> [:span html/first-of-type]]
  [wday]
  [html/root] (let [wday-short (->> wday
                                    (take 3)
                                    (apply str))]
                (html/do->
                 (html/set-attr :class "")
                 (html/add-class (s/lower-case wday-short))
                 (html/content wday-short))))

(defn- normalize-timestr
  [timestr]
  (if (s/includes? timestr ":")
    timestr
    (str timestr ":00")))

(html/defsnippet poi-schedule-row "templates/show-poi.html"
  [[:.schedule-row html/first-child]]
  [{:keys [dowfrom dowto timefrom timeto]}]
  [:.wdays] (html/do->
             (html/content "")
             (html/append (poi-schedule-wday dowfrom))
             (if dowto
               (html/do->
                (html/append "–")
                (html/append (poi-schedule-wday dowto)))
               identity))
  [:.times] (html/content (str (normalize-timestr timefrom) "–" (normalize-timestr timeto))))

(html/defsnippet poi-schedule "templates/show-poi.html"
  [:#schedule]
  [schedule]
  [html/root] (html/content (map poi-schedule-row schedule)))

(html/defsnippet poi-show-content "templates/show-poi.html"
  [:.layout :> :main]
  [ctxt]
  [:h1] (html/content (:name ctxt))
  [:#address] (html/content (:address ctxt))
  [:.notes-p] (if-let [notes (:notes ctxt)]
                (let [note-lines (->> notes
                                      s/split-lines
                                      (filter (complement s/blank?)))]
                  (html/clone-for [n note-lines]
                                  (html/content n)))
                (html/substitute ""))
  [:#schedule] (if-let [schedule (:schedule ctxt)]
                 (html/substitute (->> schedule parse-schedule poi-schedule))
                 (html/substitute "")))

(template-from-base
 poi-show
 [:.layout] (html/content (poi-show-content ctxt))
 [:.map-image-container :> :img#map_image] (html/set-attr :src (map-url ctxt))
 [:.map-image-container :> :img#map_marker] (html/set-attr :src (str "/icons/" (:type ctxt) ".png")))
