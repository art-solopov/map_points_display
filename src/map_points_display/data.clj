(ns map-points-display.data
  (:require [clojure.data.csv :as csv]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [map-points-display.utils :refer [path-join]]))

(defn- read-rows [file-path]
  (with-open [reader (io/reader file-path)]
    (into [] (csv/read-csv reader))))

(def default-data "data.csv")
(def data-path "./data")

(defn list-data-files []
  (->> data-path
       io/file
       file-seq
       (filter #(.isFile %))
       (map #(.getName %))
       (map #(s/replace % #".csv$" ""))))

(defn load-data [file-name]
  (let [file-path (path-join data-path file-name)
        rows (read-rows file-path)
        header (map keyword (first rows))
        data (rest rows)]
    (->> data (map #(zipmap header %)) (group-by :type))))
