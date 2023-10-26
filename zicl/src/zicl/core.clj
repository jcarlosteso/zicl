(ns zicl.core
  (:require [clojure.pprint :refer [pprint]]))

(defn start []
  (doseq [room (vals (ns-publics (find-ns 'zicl.temp.rooms)))]
    (pprint (assoc @room :metadata (meta room)))))