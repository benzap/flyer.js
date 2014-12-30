(ns flyer.core
  (:use [flyer.window
         :only [register-external]])
  (:require [flyer.utils :as utils]))

(defonce VERSION "1.0.4")

;;if the window is external, we should re-register it for cases where
;;the window gets refreshed
(when (not (nil? (.-opener js/window)))
  (register-external))


