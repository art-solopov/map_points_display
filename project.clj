(defproject map_points_display "0.9.0"
  :description "A simple web app that displays predefined POI on the map"
  :url "https://github.com/art-solopov/map_points_display"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [mount "0.1.16"]
                 [tolitius/mount-up "0.1.2"]
                 [ring "1.6.3"]
                 [http-kit "2.3.0"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [funcool/clojure.jdbc "0.9.0"]
                 [org.postgresql/postgresql "42.2.8"]
                 [hikari-cp "2.9.0"]
                 [ragtime "0.8.0"]
                 [environ "1.1.0"]
                 [ring-logger "1.0.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.slf4j/slf4j-simple "1.7.29"]]
  :main ^:skip-aot map-points-display.core
  :plugins [[lein-environ "1.1.0"]]
  :aliases {"runserver" ["trampoline" "run"]
            "db:migrate" ["run" "-m" "map-points-display.tasks.db/migrate"]
            "db:rollback" ["run" "-m" "map-points-display.tasks.db/rollback"]}
  :target-path "target/%s"
  :profiles {:production {:env {:app-env "production"}}
             :uberjar [:production {:aot :all}]
             :dev {:env {:app-env "development"}}
             :repl {:jvm-opts ["-Dorg.slf4j.simpleLogger.defaultLogLevel=error"]}})
