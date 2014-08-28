(ns flyer.messaging)

(defprotocol AMessage
  "messaging protocol for creating a message from a datatype"
  (create-message [this]
    "create a message to be used by the flyer protocol"))

(extend-protocol AMessage
  PersistentArrayMap
  (create-message [this]
    {:channel (or (keyword (:channel this)) :default)
     :data (or (:data this) "")})
  js/Object
  (create-message [this]
    (let [this-map (js->clj this :keywordize-keys true)]
      (create-message this-map))))

