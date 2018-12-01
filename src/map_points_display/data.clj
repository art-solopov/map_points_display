(ns map-points-display.data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn- read-rows [file-path]
  (with-open [reader (io/reader file-path)]
    (into [] (csv/read-csv reader))))

(def default-data "data.csv")

(defn load-data [file-path]
  (let [rows (read-rows file-path)
        header (map keyword (first rows))
        data (rest rows)]
    (map #(zipmap header %) data)))
