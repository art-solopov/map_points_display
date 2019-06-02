(ns map-points-display.auth
  (:import [java.time Instant]
           [java.util.concurrent ScheduledThreadPoolExecutor TimeUnit]))

(def ^:private auth-db (atom {}))
(def session-db (atom {}))
(def sessions-expiration-timer (atom nil))

(def ^:private TOKEN_TIME (* 3600 18))

;; (defn expired-sessions
;;   []
;;   (->>
;;    @session-db
;;    (filter #(.isBefore (:exp-time (val %)) (Instant/now)))
;;    keys))

;; (defn session-expiration-routine
;;   []
;;   (swap! session-db #(apply dissoc % (expired-sessions))))

(defn init-auth
  []
  ;; TODO: replace with an actual authentication database
  (reset! auth-db {"admin" "admin"})
  (reset! sessions-expiration-timer (ScheduledThreadPoolExecutor. 2))
  ;; (.scheduleAtFixedRate @sessions-expiration-timer session-expiration-routine 1 1 TimeUnit/HOURS)
  )

(defn authenticate
  [login password]
  (and (contains? @auth-db login)
       (= (@auth-db login) password)))
