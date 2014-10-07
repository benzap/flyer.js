(ns flyer.wrapper
  (:require [flyer.messaging :as msg]))

(defn ^:export apply-js-obj
  "used to apply javascript object to function parameters"
  [f obj]
  (let [obj (js->clj obj :keywordize-keys true)
        obj-vec (reduce concat (vec obj))]
    (apply f obj-vec)))

(defn ^:export broadcast
  "Wrapper around broadcast for javascript"
  [obj]
  (apply-js-obj msg/broadcast obj))

(defn ^:export subscribe
  "Wrapper around subscribe for javascript"
  [obj]
  (apply-js-obj msg/subscribe obj))
