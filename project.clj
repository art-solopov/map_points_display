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
                 [org.clojure/data.csv "0.1.4"]
                 [environ "1.1.0"]
                 [binaryage/oops "0.7.0"]]
  :main ^:skip-aot map-points-display.core
  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.12.5"]]
  :aliases {"figwheel-dev" ["trampoline" "run" "-m" "figwheel.main"
                            "--" "-b" "dev" "-r"]
            "figwheel-prod" ["trampoline" "run" "-m" "figwheel.main"
                             "--" "-bo" "prod"]}
  :target-path "target/%s"
  :ring {:port 9400 :handler map-points-display.core/handler}
  :profiles {:production {:env {:app-env "production"}}
             :uberjar [:production {:aot :all}]
             :dev {:dependencies [[com.bhauman/rebel-readline-cljs "0.1.4"]]
                   :source-paths ["src-cljs"]
                   :env {:app-env "development"}}})
