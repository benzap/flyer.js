(ns flyer.window)

(def this-window js/window)

(def external-window-list
  "based on the current window, it will store references to any
external windows that are opened using the 'open' function"
  (atom {}))

(defn gen-window-options [& options]
  (if (even? (count options))
    (let [options-twos
          (loop [options options
                 options-vonc []]
            (if (not (empty? options))
              (recur (rest (rest options))
                     (conj options-vonc
                           (map #(if (keyword? %) (name %) %)
                                (take 2 options))))
              options-vonc))
          options-inter
          (map #(apply str (interpose "=" %)) options-twos)]
      (apply str (interpose ", " options-inter)))
    (.error js/console "options needs an even number of terms")))

(defn mark-external-window!
  [w]
  (aset w "flyer_bMarkExternal" true))

(defn unmark-external-window!
  [w]
  (aset w "flyer_bMarkExternal" nil))

(defn is-window-external?
  [w]
  (aget w "flyer_bMarkExternal"))

(defn ^export open [url name & options]
  (let [options-str (cond
                     (and (= (count options) 1)
                          (= (type (first options)) js/String))
                     (first options)
                     :else
                     (apply gen-window-options options))
        window (.open this-window url name options-str)]
    (mark-external-window! window)
    (aset window "flyer_bMarkExternal" true)
    (.log js/console "marked" (is-window-external? window))
    (swap! external-window-list assoc (.-name window) window)1
    (.addEventListener
     window "beforeunload"
     (fn [event]
       (let [name (.-name window)]
         (swap! external-window-list dissoc (.-name window))
         nil)))
    window))


