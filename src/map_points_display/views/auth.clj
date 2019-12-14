(ns map-points-display.views.auth
  (:require [hiccup.form :refer :all]))

(defn login-form
  [{:keys [username password]}]
  (let [main (form-to {:class "login-form"}
                      [:post "/login"]
                      [:div
                       (label "username" "Username")
                       (text-field "username" username)]
                      [:div
                       (label "password" "Password")
                       (password-field "password" password)]
                      [:div
                       (submit-button "Login")])]
    {:main main}))
