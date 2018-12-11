(ns map-points-display.utils
  (:import java.nio.file.Paths))

(defn path-join [p & ps]
  (.toString (Paths/get p (into-array ps))))

