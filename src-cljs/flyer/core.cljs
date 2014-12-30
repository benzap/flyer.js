(ns flyer.core
  (:use [flyer.window
         :only [register-external]])
  (:require [flyer.utils :as utils]
            [flyer.wrapper]))

(defonce VERSION "1.1.0")

;;if the window is external, we should re-register it for cases where
;;the window gets refreshed
(when (not (nil? (.-opener js/window)))
  (register-external))

;;places the flyer.wrapper.subscribe and flyer.wrapper.broadcast
;;functions within flyer namespace.
(js/goog.exportProperty (aget js/window "flyer") "subscribe", flyer.wrapper/subscribe)
(js/goog.exportProperty (aget js/window "flyer") "broadcast", flyer.wrapper/broadcast)
