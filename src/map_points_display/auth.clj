(ns map-points-display.auth
  (:import [java.time Instant Duration]
           [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(def ^:private auth-db (atom {}))
(def session-store (atom {}))
(def sessions-expiration-timer (atom nil))

(def ^:private TOKEN_TIME (Duration/ofDays 1))

(defn- session-expired?
  [{:keys [created-at]}]
  (let [diff (Duration/between created-at (Instant/now))]
    (>= (.compareTo diff TOKEN_TIME) 0)))

(defn expired-sessions
  []
  (->>
   @session-store
   (filter #(session-expired? (val %)))
   keys))

(defn session-expiration-routine
  []
  (println (str (Instant/now)) "Checking for expired sessions")
  (swap! session-store #(apply dissoc % (expired-sessions))))

(defn init-auth
  []
  ;; TODO: replace with an actual authentication database
  (reset! auth-db {"admin" "admin"})
  (reset! sessions-expiration-timer (ScheduledThreadPoolExecutor. 2))
  (.scheduleAtFixedRate @sessions-expiration-timer session-expiration-routine 1 1 TimeUnit/HOURS)
  )

(defn authenticate
  [login password]
  (and (contains? @auth-db login)
       (= (@auth-db login) password)))
