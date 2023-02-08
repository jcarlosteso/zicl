(ns zicl.state)

(def INITIAL-STATE
  {:rooms {}
   :objects {:global {} :shared {}}
   :player {:location {}}})

(def ^:dynamic *STATE* (atom INITIAL-STATE))