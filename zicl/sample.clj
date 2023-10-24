(def game-state
  (atom
   {:rooms {}
    :objects {:global {}
              :shared {}}
    :variables {}}))

(swap! game-state assoc-in [:variables :rug-moved?] false)

(require 'clojure.pprint)
(clojure.pprint/pprint @game-state)

(get-in @game-state [:variables :rug-moved?])

(defn start []
  (loop [state (initial-state)]
    (let [command (get-player-input)]
      (if (= "QUIT" command)
        (println "Bye!")
        (recur (next-state state command))))))