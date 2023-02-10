(ns zicl.exit)

(defn TO
  ([room-key] {:to room-key})
  ([room-key condition] (merge {:to room-key} (apply hash-map condition))))
(defn SORRY [message] {:sorry message})

(def IF :global)
(def ELSE :sorry)

(defmacro EXIT [direction-key exit-spec]
  `(fn [room#]
     (update room#
             :exits
             merge
             (assoc {} ~direction-key ~exit-spec))))

(defmacro NORTH
  ([arg] `(NORTH (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :north (~exit-fn ~room-key ~(vec args)))))

(defmacro NE
  ([arg] `(NE (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :north-east (~exit-fn ~room-key ~(vec args)))))

(defmacro EAST
  ([arg] `(EAST (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :east (~exit-fn ~room-key ~(vec args)))))

(defmacro SE
  ([arg] `(SE (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :south-east (~exit-fn ~room-key ~(vec args)))))

(defmacro SOUTH
  ([arg] `(SOUTH (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :south (~exit-fn ~room-key ~(vec args)))))

(defmacro SW
  ([arg] `(SW (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :soutw-west (~exit-fn ~room-key ~(vec args)))))

(defmacro WEST
  ([arg] `(WEST (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :west (~exit-fn ~room-key ~(vec args)))))

(defmacro NW
  ([arg] `(NW (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :north-west (~exit-fn ~room-key ~(vec args)))))

(defmacro UP
  ([arg] `(UP (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :up (~exit-fn ~room-key ~(vec args)))))

(defmacro DOWN
  ([arg] `(DOWN (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :down (~exit-fn ~room-key ~(vec args)))))

(defmacro IN
  ([arg] `(IN (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :in (~exit-fn ~room-key ~(vec args)))))

(defmacro OUT
  ([arg] `(OUT (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn arg] `(~exit-fn ~arg))
  ([exit-fn room-key & args] `(EXIT :out (~exit-fn ~room-key ~(vec args)))))
