(ns flyer.core
  (:use [flyer.window
         :only [register-external]]))

;;if the window is external, we should re-register it for cases where
;;the window gets refreshed
(when (not (nil? (.-opener js/window)))
  (register-external))
