(ns flyer.core)

;;(js/alert "Hello World!")

(defn ^:export alert2 [msg]
  (js/alert (str "alert2:" msg)))
