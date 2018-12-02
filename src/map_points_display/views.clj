(ns map-points-display.views
  (:require [clojure.string :as s]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet group-item "templates/index.html"
  [:section.group :ul.items :> :li]
  [item]
  [:li] (html/do->
         (html/content (:name item))
         (html/set-attr :data-lat (:lat item))
         (html/set-attr :data-lon (:lon item))
         (html/set-attr :data-category (:type item))))

(html/defsnippet group "templates/index.html"
  [:section.group]
  [name items]
  [html/root] (html/set-attr :data-name name)
  [:h2.group-type] (html/content (s/capitalize name))
  [:ul.items] (html/content (map group-item items)))

(html/deftemplate home "templates/index.html"
  [ctxt]
  [:#app :h1] (html/content (:message ctxt))
  [:#app :.places] (html/content (map #(apply group %) (:groups ctxt))))
