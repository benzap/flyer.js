(ns flyer.window
  (:require
   [clojure.string :as str]
   [flyer.storage :as storage]
   [flyer.utils :as utils]
   [goog.events :as events]))


(def this-window js/window)


(def external-window-list
  "based on the current window, it will store references to any
external windows that are opened using the 'open' function"
  (atom {}))


(defn gen-window-options
  [& options]
  (as-> options $
   (partition 2 $)
   (map (fn [[x y]] (str/join "=" [(name x) y])) $)
   (str/join ", " $)))


(defn ^:export open [url name & options]
  (let [options-str (if (and (= (count options) 1)
                             (= (type (first options)) js/String))
                     (first options)
                     (apply gen-window-options options))

        window (.open this-window url name options-str)]

    (storage/insert-window-ref! window)

    (events/listen
     window (.-BEFOREUNLOAD events/EventType)
     (fn [event]
       (storage/remove-window-ref! window)
       nil))

    window))


(defn register-external
  ([window] (storage/insert-window-ref! window))
  ([] (register-external js/window)))

