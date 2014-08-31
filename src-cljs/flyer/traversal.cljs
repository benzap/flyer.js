(ns flyer.traversal
  "includes the necessary functions for accumulating all of the
  iframes, frames, and windows that are currently active."
  (:require [flyer.window :as w]
            [flyer.utils :as utils]
            [flyer.storage :as storage]))

(defn list-frame-windows
  "returns a list of all of the frames that the provided window has"
  [window]
  (let [framelist (-> window .-frames)
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
  (let [window-refs (storage/get-window-set)
        windows (map #(aget js/window %) window-refs)]
    (filter #(not (nil? %)) windows)))

(defn generate-broadcast-list
  "generates a list of windows that we wish to send the message to"
  ([current-window & {:keys [first-iteration]
                      :or {first-iteration false}}]
     (let [current-child-list 
           (list-frame-windows current-window)
           map-reduce-fn
           (comp (partial reduce concat)
                 (partial map generate-broadcast-list))]
       (if (and (not first-iteration)
                (= (.-top current-window) current-window))
         [current-window]
         (filter #(not (nil? %))
                 (concat (map-reduce-fn current-child-list)
                         (when first-iteration
                           (map-reduce-fn (list-external-windows)))
                         [current-window])))))
  ([]
     (generate-broadcast-list (utils/get-main-parent) :first-iteration true)))

;;(.log js/console (clj->js (generate-broadcast-list)))
