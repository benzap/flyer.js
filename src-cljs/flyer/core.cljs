(ns flyer.core
  (:use [flyer.messaging
         :only [publish subscribe]]))

(defn ^export main []
  (subscribe :callback
             (fn [data]
               (.log js/console "I am called from parent!"))))

(defn ^export child []
  (subscribe :callback
             (fn [data]
               (.log js/console "I am called from child!"))))
