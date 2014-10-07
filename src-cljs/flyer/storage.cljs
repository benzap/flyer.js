(ns flyer.storage
  "includes functions for storing window information"
  (:require [flyer.utils :as utils]))

(declare get-window-refs)

(def storage (utils/get-main-parent))

(def window-list-key "flyer_WindowReferences")

(defn init-window-refs 
  "Initializes our window references"
  []
  (when (empty? (get-window-refs))
    (aset storage window-list-key #{})))

(defn get-window-refs 
  "Returns the window references, or an empty set"
  []
  (or
   (aget storage window-list-key)
   #{}))

(defn insert-window-ref! [window]
  (init-window-refs)
  (aset storage window-list-key
        (conj (get-window-refs) window)))

(defn remove-window-ref! [window]
  (aset storage window-list-key
        (disj (get-window-refs) window)))
