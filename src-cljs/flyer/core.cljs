(ns flyer.core
  (:use [flyer.window
         :only [register-external]])
  (:require [flyer.storage :as s]
            [flyer.utils :as utils]))

;;if the window is external, we should re-register it for cases where
;;the window gets refreshed
(when (not (nil? (.-opener js/window)))
  (register-external))

;;if i'm the parent window, intiialize ref variable
(when (= (utils/get-main-parent) js/window)
  (aset s/storage s/window-list-key #{}))
