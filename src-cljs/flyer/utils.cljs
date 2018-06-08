(ns flyer.utils)


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
  "Finds the main parent by traversing down till it has been
  determined that it is the parent"
  ([window]
   (loop [window window]
     (if (is-external-window? window)
       (recur (.-opener window))

       (if (is-frame? window)
         (recur (.-parent window))
         window))))
  ([] (get-main-parent js/window)))
