(defproject map_points_display "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [com.bhauman/figwheel-main "0.1.9"]
                 [ring "1.6.3"]
                 [compojure "1.5.1"]
                 [enlive "1.1.6"]
                 [org.clojure/data.csv "0.1.4"]]
  :main ^:skip-aot map-points-display.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[com.bhauman/rebel-readline-cljs "0.1.4"]]}})
