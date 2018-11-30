(ns map-points-display.core)

(enable-console-print!)

(def open-layers (.-ol js/window))

(defn make-view [lat lon zoom]
  (let [proj (.-proj open-layers)
        View (.-View open-layers)
        view-conf {:center (new (.-fromLonLat proj) (array lat lon))
                   :zoom zoom}]
    (View. (clj->js view-conf))))

(defn make-controls []
  (let [defaults (->> open-layers .-control .-defaults)
        MousePosition (->> open-layers .-control .-MousePosition)
        createStringXY (->> open-layers .-coordinate .-createStringXY)
        mouse-pos-control (new MousePosition
                               (js-obj
                                "coordinateFormat" (createStringXY 4)
                                "projection" "EPSG:4326"))]
    (.extend (defaults) (array mouse-pos-control))))

(defn init-map [el-id]
  (let [mapel (.getElementById js/document el-id)]
    (set! (.-innerHTML mapel) ""))

  (let [Map (.-Map open-layers)
        Tile (->> open-layers .-layer .-Tile)
        source-OSM (->> open-layers .-source .-OSM)
        map-config {:target el-id
                    :layers (array (Tile. (js-obj "source" (source-OSM.)))) 
                    :view (make-view 11.24 43.77 10)
                    :controls (make-controls)}]
    (new Map (clj->js map-config)))
  )

(defn get-app []
  (.getElementById js/document "app"))

(defn -main []
  (init-map "map"))


(-main)
