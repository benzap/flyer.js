(ns flyer.storage
  "includes functions for storing window information"
  (:require [cljs.reader :as reader]))

;;init local session storage

(defn remove-window-name!
  "remove window from set, if it exists"
  [name])

(defn insert-window-name!
  "insert window into set"
  [name]
  (aset js/sessionStorage "test" name))

(defn get-window-list
  
  [])
