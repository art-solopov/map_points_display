(defproject map_points_display "0.1.0"
  :description "A simple web app that displays predefined POI on the map"
  :url "https://github.com/art-solopov/map_points_display"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring "1.6.3"]
                 [compojure "1.5.1"]
                 [enlive "1.1.6"]
                 [org.clojure/data.csv "0.1.4"]
                 [environ "1.1.0"]]
  :main ^:skip-aot map-points-display.core
  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.12.5"]]
  :target-path "target/%s"
  :ring {:port 9400 :handler map-points-display.core/handler}
  :profiles {:production {:env {:app-env "production"}}
             :uberjar [:production {:aot :all}]
             :dev {:env {:app-env "development"}}})
