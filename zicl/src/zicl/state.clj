(ns zicl.state)

(def INITIAL-STATE
  {:rooms {}
   :objects {:global {} :shared {}}
   :globals {}
   :player {:location {}}})

(def ^:dynamic *STATE* (atom INITIAL-STATE))