(ns flyer.core
  (:use [flyer.window
         :only [register-external]])
  (:require [flyer.storage :as s]
            [flyer.utils :as utils]))

;;always try and initialize window refs.
;;this way, we don't even need flyer.js in the parent window
;;(s/init-window-refs)

;;if the window is external, we should re-register it for cases where
;;the window gets refreshed
(when (not (nil? (.-opener js/window)))
  (register-external))

(defonce VERSION "1.0.4")
