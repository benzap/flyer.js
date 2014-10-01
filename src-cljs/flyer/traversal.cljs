(ns flyer.traversal
  "includes the necessary functions for accumulating all of the
  iframes, frames, and windows that are currently active."
  (:require [flyer.window :as w]
            [flyer.utils :as utils]
            [flyer.storage :as storage]))

(defn list-frame-windows
  "returns a list of all of the frames that the provided window has"
  [window]
  (let [framelist (or (-> window .-frames) [])
        length (.-length framelist)]
    (loop [i 0
           list []]
      (if (< i length)
        (recur (inc i)
               (conj list (aget framelist i)))
        list))))

(defn list-external-windows
  "returns a list of all external windows linked to the current
  window" 
  []
  (vec (storage/get-window-refs)))

(defn generate-broadcast-list
  "generates a list of windows that we wish to send the message to"
  ([current-window]
     (let [current-frame-list 
           (list-frame-windows current-window)
           map-reduce-fn
           (comp (partial reduce concat)
                 (partial map generate-broadcast-list))]
               (conj (map-reduce-fn current-frame-list)
                     current-window)))
  ([]
     (let [map-reduce-fn
           (comp (partial reduce concat)
                 (partial map generate-broadcast-list))
           external-windows (list-external-windows)]
       (concat
        (generate-broadcast-list (utils/get-main-parent))
        (map-reduce-fn external-windows)))))
