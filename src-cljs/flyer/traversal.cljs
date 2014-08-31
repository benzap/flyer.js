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

(defn get-main-parent 
  "Finds the main parent by traversing down till it has been determined that it is the parent"
  ([window]
  (cond 
   (is-frame? window) (get-main-parent (.-parent window))
   :else window))
  ([] (get-main-parent js/window)))

(defn list-frame-windows
  "returns a list of all of the frames that the provided window has"
  [window]
  (when (not (w/is-window-external? window))
    (let [framelist (-> window .-frames)
          length (.-length framelist)]
      (loop [i 0
             list []]
        (if (< i length)
          (recur (inc i)
                 (conj list (aget framelist i)))
          list)))))

(defn list-external-windows
  "returns a list of all external windows linked to the current
  window" 
  [window]
  (mapv second @w/external-window-list))

(defn list-all-windows
  "generates a list of all frames/windows, etc, that reside in this window"
  [window]
  (concat (list-frame-windows window)
          (list-external-windows window)))

(defn generate-broadcast-list
  "generates a list of windows that we wish to send the message to"
  ([current-window & {:keys [first-iteration]
                      :or {first-iteration nil}}]
     (let [current-child-list 
           (list-all-windows current-window)
           map-reduce-fn
           (comp (partial reduce concat)
                 (partial map generate-broadcast-list))]
       (if (and (not first-iteration)
                (= current-window (.-top current-window)))
         (conj (map-reduce-fn current-child-list) current-window)
         [current-window])))
  ([]
     (generate-broadcast-list (get-main-parent
                               :first-iteration true))))
