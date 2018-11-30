(ns map-points-display.views
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate home "templates/index.html"
  [ctxt]
  [:#app :h1] (html/content (:message ctxt)))
