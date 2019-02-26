(ns map-points-display.views
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]
            [map-points-display.config :refer [config]]))

(html/defsnippet group-item "templates/show.html"
  [:section.group :ul.items :> :li]
  [item]
  [:li] (html/do->
         (html/set-attr :data-lat (:lat item))
         (html/set-attr :data-lon (:lon item))
         (html/set-attr :data-category (:type item)))
  [:.item-name] (html/content (:name item))
  [:.item-address] (html/content (:address item)))

(html/defsnippet group "templates/show.html"
  [:section.group]
  [name items]
  [html/root] (html/set-attr :data-name name)
  [:h2.group-type] (html/content (s/capitalize name))
  [:ul.items] (html/content (map group-item items)))

(html/deftemplate show-file "templates/show.html"
  [ctxt]
  [:#app :h1] (html/content (:message ctxt))
  [:#app :.places] (html/content (map #(apply group %) (:groups ctxt)))
  [:body [:script html/last-of-type]] (html/set-attr :src (:js-url @config)))

(html/defsnippet data-files-item "templates/index.html"
  [:ul#data_files :> :li.data-file]
  [item]
  [:li :> :a] (html/do->
               (html/content (s/capitalize item))
               (html/set-attr :href (str "/data/" item))))

(html/deftemplate index-file "templates/index.html"
  [ctxt]
  [:ul#data_files] (html/content (map data-files-item (:files ctxt))))
