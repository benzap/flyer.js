(ns flyer.external)

(def external-key "flyer_bExternal")

(defn mark-external! [window]
  (aset window external-key true))

(defn is-marked-external? [window]
  (aget window external-key))
