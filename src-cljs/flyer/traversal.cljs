(ns flyer.traversal
  "includes the necessary functions for accumulating all of the
  iframes, frames, and windows that are currently active."
  (:require [flyer.window :as w]))

(defn is-frame? 
  "Determines whether the current window is a frame within a set of
  frames currently showing."  
  [window]
  (let [parent-window (.-parent window)
        current-location (.-location window)
        parent-location (.-location parent-window)]
  (not= current-location parent-location)))

(defn is-external-window?
  "Determines whether the current window was opened externally"
  [window]
  (not (nil? (.-opener window))))

(defn get-main-parent 
  "Finds the main parent by traversing down till it has been determined that it is the parent"
  ([window]
     (cond
      (is-external-window? window) (get-main-parent (.-opener window))
      (is-frame? window) (get-main-parent (.-parent window))
      :else window))
  ([] (get-main-parent js/window)))

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
  [])

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
         (conj (map-reduce-fn current-child-list) current-window))))
  ([]
     (generate-broadcast-list (get-main-parent) :first-iteration true)))
