(ns zicl.exit)

(defn TO [room-key] {:to room-key})
(defn SORRY [message] {:sorry message})

(defmacro EXIT [direction-key exit-spec]
  `(fn [room#]
     (update room#
             :exits
             merge
             (assoc {} ~direction-key ~exit-spec))))

(defmacro NORTH
  ([arg] `(NORTH (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :north (~exit-fn ~room-key))))

(defmacro NE
  ([arg] `(NE (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :north-east (~exit-fn ~room-key))))

(defmacro EAST
  ([arg] `(EAST (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :east (~exit-fn ~room-key))))

(defmacro SE
  ([arg] `(SE (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :south-east (~exit-fn ~room-key))))

(defmacro SOUTH
  ([arg] `(SOUTH (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :south (~exit-fn ~room-key))))

(defmacro SW
  ([arg] `(SW (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :soutw-west (~exit-fn ~room-key))))

(defmacro WEST
  ([arg] `(WEST (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :west (~exit-fn ~room-key))))

(defmacro NW
  ([arg] `(NW (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :north-west (~exit-fn ~room-key))))

(defmacro UP
  ([arg] `(UP (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :up (~exit-fn ~room-key))))

(defmacro DOWN
  ([arg] `(DOWN (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :down (~exit-fn ~room-key))))

(defmacro IN
  ([arg] `(IN (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :in (~exit-fn ~room-key))))

(defmacro OUT
  ([arg] `(OUT (if (keyword? ~arg) TO SORRY) ~arg))
  ([exit-fn room-key] `(EXIT :out (~exit-fn ~room-key))))
