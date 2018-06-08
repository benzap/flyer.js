(ns flyer.storage
  "includes functions for storing window information. This window
  information is stored in the parent window under the
  `window-list-key`."
  (:require [flyer.utils :as utils]))

(declare get-window-refs)


(def storage (utils/get-main-parent))


(def window-list-key "flyer_WindowReferences")


(defn init-window-refs 
  "Initializes our window references"
  []
  (when (nil? (get-window-refs))
    (aset storage window-list-key (set nil))))


(defn get-window-refs 
  "Returns the window references, or an empty set"
  []
  (or
   (aget storage window-list-key)
   nil))


(defn insert-window-ref! [window]
  (init-window-refs)
  (aset storage window-list-key
        (conj (get-window-refs) window)))


(defn remove-window-ref! [window]
  (init-window-refs)
  (aset storage window-list-key
        (disj (get-window-refs) window)))

