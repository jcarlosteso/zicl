(ns zicl.exit)

(defmacro TO [room-key] `{:to ~room-key})

(defmacro EXIT [direction-key exit-spec]
  `(fn [room#]
     (update room#
             :exits
             merge
             (assoc {} ~direction-key ~exit-spec))))

(defmacro NORTH
  ([room-key] `(NORTH TO ~room-key))
  ([exit-fn room-key] `(EXIT :north (~exit-fn ~room-key))))

(defmacro NE
  ([room-key] `(NE TO ~room-key))
  ([exit-fn room-key] `(EXIT :north-east (~exit-fn ~room-key))))

(defmacro EAST
  ([room-key] `(EAST TO ~room-key))
  ([exit-fn room-key] `(EXIT :east (~exit-fn ~room-key))))

(defmacro SE
  ([room-key] `(SE TO ~room-key))
  ([exit-fn room-key] `(EXIT :south-east (~exit-fn ~room-key))))

(defmacro SOUTH
  ([room-key] `(SOUTH TO ~room-key))
  ([exit-fn room-key] `(EXIT :south (~exit-fn ~room-key))))

(defmacro SW
  ([room-key] `(SW TO ~room-key))
  ([exit-fn room-key] `(EXIT :soutw-west (~exit-fn ~room-key))))

(defmacro WEST
  ([room-key] `(WEST TO ~room-key))
  ([exit-fn room-key] `(EXIT :west (~exit-fn ~room-key))))

(defmacro NW
  ([room-key] `(NW TO ~room-key))
  ([exit-fn room-key] `(EXIT :north-west (~exit-fn ~room-key))))

(defmacro UP
  ([room-key] `(UP TO ~room-key))
  ([exit-fn room-key] `(EXIT :up (~exit-fn ~room-key))))

(defmacro DOWN
  ([room-key] `(DOWN TO ~room-key))
  ([exit-fn room-key] `(EXIT :down (~exit-fn ~room-key))))

(defmacro IN
  ([room-key] `(IN TO ~room-key))
  ([exit-fn room-key] `(EXIT :in (~exit-fn ~room-key))))

(defmacro OUT
  ([room-key] `(OUT TO ~room-key))
  ([exit-fn room-key] `(EXIT :out (~exit-fn ~room-key))))
