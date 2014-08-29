(ns flyer.messaging
  (:require [flyer.traversal :as traversal]))

(def default-message {:data nil
                      :channel "default"
                      :topic "*"})

(def default-window js/window)

(defn default-callback [msg]
  (.log js/console msg)
  (.log js/console (clj->js msg)))

(defn ^export window-post-message
  "performs the window postback"
  [window msg]
  (let [data-js (clj->js msg)
        data-json (.stringify js/JSON data-js)]
    (.postMessage window data-json "*")))

(defn ^export broadcast
  "broadcast message to currently active frames"
  [{:keys [data channel topic]
    :or {data (:data default-message)
         channel (:channel default-message)
         topic (:topic default-message)}}]
  (let [msg {:data data :channel channel :topic topic}
        msg-js (clj->js msg)
        broadcast-list (traversal/generate-broadcast-list)]
    (doseq [window broadcast-list] 
         (window-post-message window msg))))

(defn ^export create-broadcast-listener
  "used to subscribe to the broadcast messages this takes advantage of
  message postback"
  ([window callback]
     (.addEventListener window "message" callback true))
  ([callback] (create-broadcast-listener js/window callback)))

(defn ^export subscribe
  "subscribe to broadcast messages"
  [& {:keys [window channel topic callback]
      :or {window default-window
           channel (:channel default-message)
           topic (:topic default-message)
           callback default-callback}
      :as sub}]
  (.log js/console "window:" js/window)
  (let [callback-wrapper
        (fn [event]
          (.log js/console event)
          (let [data (.-data event)
                msg-js (.parse js/JSON data)
                msg (js->clj msg-js)]
            (callback msg-js)))]
    (create-broadcast-listener js/window callback-wrapper)))

(subscribe)
;;(broadcast)


