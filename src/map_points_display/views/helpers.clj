(ns map-points-display.views.helpers
  (:require [net.cgrand.enlive-html :as html]
            [environ.core :refer [env]]))

(def ^:private url-prefix
  (env :url-prefix))

(defn url-for [path]
  (str url-prefix path))

(def acknowledgements
  ["© Artemiy Solopov, 2019"
   "All map data by OpenStreetMaps"])

(defmacro template-from-base
  [name & body]
  `(html/deftemplate ~name "templates/_base.html"
     [~(quote ctxt)]
     [:head [:link (html/attr= :rel "stylesheet") html/first-of-type]] (html/set-attr :href (url-for "/css/app.css"))
     [:footer#main_footer :> :p] (html/clone-for [~'ack acknowledgements]
                                                 (html/content ~'ack))
     ~@body))

