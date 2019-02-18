(ns map-points-display.data-points)

(defn- read-datum [el]
  {:name (-> el (.querySelector ".item-name") .-innerText)
   :lat (js/Number (.. el -dataset -lat))
   :lon (js/Number (.. el -dataset -lon))
   :category (.. el -dataset -category)})

(defn- get-els []
  (array-seq (.querySelectorAll js/document ".group .items li")))

(defn- update-els [el uuid]
  (set! (.. el -dataset -id) uuid))

(defn read-data []
  (let [els (get-els)
        el-data (map read-datum els)
        el-pts (repeat nil)] ; TODO: replace with actual points definition
    (map #(array-map :el %1 :data %2 :point %3 :uuid %4)
         els el-data el-pts (repeatedly random-uuid))))

