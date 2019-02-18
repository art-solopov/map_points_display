(ns map-points-display.ext.leaflet)

(def l (.. js/window -L))

(def attribution
  "<a href=\"https://wikimediafoundation.org/wiki/Maps_Terms_of_Use\">Wikimedia</a>")

(def url-template
  "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png")

(def ^private tile-layer-options
  (clj->js {:attribution attribution}))

(defn make-map [lat lon zoom]
  (let [the-map (-> l
                (.map "map")
                (.setView (array lat lon) zoom))]
    (-> l
        (.tileLayer url-template tile-layer-options)
        (.addTo the-map))
    the-map))

