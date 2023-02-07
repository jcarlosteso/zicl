(ns zicl.parser
  (:require [clojure.string :refer [lower-case]]))

(defn- prompt []
  (print "\n> ")
  (flush)
  (read-line))

(def ^:private walking-regex #"^(?:walk )?(\w+)$")

(defn PARSER []
  (when-let [parsed (->> (prompt) lower-case (re-find walking-regex))]
    {:verb :walk :direct (last parsed)}))