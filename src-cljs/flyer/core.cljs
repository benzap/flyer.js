(ns flyer.core
  (:use [flyer.messaging
         :only [broadcast subscribe]]))

#_(defn ^export main []
  (subscribe 
   :callback
   (fn [data]
     (.log js/console "I am called from parent!"))))

#_(defn ^export child []
  (subscribe 
   :topic "general"
   :callback
   (fn [data]
     (.log js/console "I am called from child!")
     (.log js/console "data: " data)
     (.log js/console "data: " data)
     (.log js/console "data: " data))))

#_(if-let [button (.getElementById js/document "main-button")]
  (do 
    (.log js/console "button found!")
    (.addEventListener 
     button "click"
     (fn [event]
       (broadcast :topic "general")
       (.log js/console "Button Pressed!")))))
