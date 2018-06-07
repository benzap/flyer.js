(ns flyer.messaging
  "Includes all of the desired messaging functions"
  (:require [flyer.traversal :as traversal]
            [goog.events :as events]))


(def default-message 
  "default message structure"
  {:data nil
   :topic "*"
   :channel "*"})


(def default-window 
  "the main window needs to be localized, so that it can be
  referenced"
  js/window)


(defn ^:export default-callback 
  "default callback for testing"
  [data topic channel]
  (.log js/console "callback-data:" data)
  (.log js/console "callback-topic:" topic)
  (.log js/console "callback-channel:" channel))


(defn ^:export window-post-message
  "performs the window postback"
  ([window msg target]
   (let [data-js (clj->js msg)
         data-json (.stringify js/JSON data-js)
         target-origin (case (keyword target)
                         :local (-> window .-location .-origin)
                         :all "*"
                         nil "*"
                         target)]
     (.postMessage window data-json target-origin)))
  ([window msg] (window-post-message window msg "*")))


(defn ^:export broadcast
  "broadcast message to currently active frames"
  [& {:keys [data channel topic target]
      :or {data (:data default-message)
           channel (:channel default-message)
           topic (:topic default-message)
           target :all}
      :as param}]
  (let [msg {:data data :channel channel :topic topic}
        msg-js (doto (js/Object.)
                 (aset "data" data)
                 (aset "channel" channel)
                 (aset "topic" topic))
        broadcast-list (traversal/generate-broadcast-list)]
    (doseq [window broadcast-list] 
      (window-post-message window msg target))))


(defn create-broadcast-listener
  "used to subscribe to the messages being broadcasted"
  ([window callback]
     (events/listen
      window (.-MESSAGE events/EventType) callback))
  ([callback] (create-broadcast-listener default-window callback)))


(defn like-this-channel? 
  [msg-channel callback-channel]
  (or (= callback-channel (default-message :channel))
      (= msg-channel callback-channel)))


(defn like-this-topic?
  "Checks if the given topic matches, and if it fails, attempts to
  match the callback's topic to the msg topic as though it were a
  regular expression"
  [msg-topic callback-topic]
  (or
   (= callback-topic (default-message :topic))
   (= msg-topic callback-topic)
   ;;try and see if it's a regex
   (try 
     (-> callback-topic re-pattern (re-matches msg-topic) string?)
     (catch js/Error e nil))))


(defn like-this-origin?
  [msg-origin callback-origin]
  (or
   (= (keyword callback-origin) :all)
   (and (= (keyword callback-origin) :local)
        (= (-> js/window .-location .-origin) msg-origin))
   (= msg-origin callback-origin)))


(defn ^:export like-this-flyer?
  "determines if the callback should be called based on the channel
  and the topic"
  [msg-topic msg-channel msg-origin
   callback-topic callback-channel callback-origin]
  (and
   (like-this-channel? msg-channel callback-channel)
   (like-this-topic? msg-topic callback-topic)
   (like-this-origin? msg-origin callback-origin)))


(defn ^:export subscribe
  "subscribe to broadcast messages"
  [& {:keys [window channel topic callback origin]
      :or {window default-window
           channel (:channel default-message)
           topic (:topic default-message)
           callback default-callback
           origin :all}
      :as sub}]
  (let [callback-wrapper
        (fn ^:export [event]
          (let [data (-> event .getBrowserEvent .-data)
                
                ;;Since we're hijacking window .postMessage, it's
                ;;possible we'll come across foreign messages. In
                ;;situations where that happens, this should grab the
                ;;msg, and places it on the 'FOREIGN' channel with the
                ;;default topic.
                msg-js 
                (try (.parse js/JSON data)
                     (catch js/Error e
                       (doto (js/Object.)
                         (aset "channel" "FOREIGN")
                         (aset "topic" (default-message :topic))
                         (aset "data" data))))

                ;;extract data from channel
                msg-channel (or (aget msg-js "channel") "FOREIGN")
                msg-topic (or (aget msg-js "topic") (default-message :topic))
                msg-data (or (aget msg-js "data") (default-message :data))
                msg-origin (-> event .getBrowserEvent .-origin)]

            (when (like-this-flyer? msg-topic msg-channel msg-origin topic channel origin)
              (callback msg-data msg-topic msg-channel msg-origin))))]
    (create-broadcast-listener js/window callback-wrapper)))
