(ns flyer.storage
  "includes functions for storing window information"
  (:require [cljs.reader :as reader]
            [flyer.utils :as utils]))


(def storage
  (let [parent (utils/get-main-parent)
        session (.-localStorage parent)]
    session))

(def window-list-key "flyer_WindowReferences")

(defn set-window-set! [w-list]
  (let [set-string (prn-str w-list)]
    (aset storage window-list-key set-string)))

(defn get-window-set
  "Get the list of windows stored in the session storage"
  []
  (let [window-str (aget storage window-list-key)]
    (when (string? window-str)
      (reader/read-string window-str))))

(defn init []
  (let [window-list (get-window-set)]
    (when (nil? window-list)
      (.log js/console "Initializing Flyer Session Storage")
      (set-window-set! #{}))))

(defn remove-window-name!
  "remove window from set, if it exists"
  [name]
  (let [windows (get-window-set)]
    (set-window-set! (disj windows name))))

(defn insert-window-name!
  "insert window into set"
  [name]
  (let [windows (get-window-set)]
    (set-window-set! (conj windows name))))

;;init local session storage
(init)
