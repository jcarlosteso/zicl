(ns zicl.exit)

(def TO :to)
(def SORRY :sorry)
(def IF :global)
(def ELSE :sorry)
(def IS :is)
(def OPEN :open)
(def PER :per)

(def TO? (partial = TO))
(def SORRY? (partial = SORRY))
(def IF? (partial = IF))
(def ELSE? (partial = ELSE))
(def IS? (partial = IS))
(def OPEN? (partial = OPEN))
(def PER? (partial = PER))

(def UEXIT-IMPLICIT [keyword?])
(def UEXIT-EXPLICIT [TO? keyword?])
(def NEXIT-IMPLICIT [string?])
(def NEXIT-EXPLICIT [SORRY? string?])
(def CEXIT          [TO? keyword? IF? keyword? ELSE? string?])
(def DEXIT          [TO? keyword? IF? keyword? IS? OPEN? ELSE? string?])
(def FEXIT          [PER? keyword?])

(defmacro matches? [syntax clauses]
  `(and
    (= (count ~syntax) (count ~clauses))
    (every? (fn [[f# token#]] (f# token#))
            (map vector ~syntax ~clauses))))

(defmacro EXIT-SPEC [& clauses]
  `(let [clauses# ~(vec clauses)]
     (cond
       (matches? UEXIT-IMPLICIT clauses#) {:to (first clauses#)}
       (matches? NEXIT-IMPLICIT clauses#) {:sorry (first clauses#)}
       (matches? UEXIT-EXPLICIT clauses#) {:to (last clauses#)}
       (matches? NEXIT-EXPLICIT clauses#) {:sorry (last clauses#)}
       (matches? CEXIT          clauses#) {:to (second clauses#)
                                           :global (get clauses# 3)
                                           :sorry (last clauses#)}
       (matches? DEXIT          clauses#) {:to (second clauses#)
                                           :shared (get clauses# 3)
                                           :sorry (last clauses#)}
       (matches? FEXIT          clauses#) {:action (last clauses#)}
       :else                              nil)))

(defmacro EXIT [direction-key & exit-spec]
  `(fn [room#]
     (update room#
             :exits
             merge
             (assoc {} ~direction-key (EXIT-SPEC ~@exit-spec)))))

(defmacro NORTH [& args] `(EXIT :north ~@args))
(defmacro NE [& args] `(EXIT :north-east ~@args))
(defmacro EAST [& args] `(EXIT :east ~@args))
(defmacro SE [& args] `(EXIT :south-east ~@args))
(defmacro SOUTH [& args] `(EXIT :south ~@args))
(defmacro SW [& args] `(EXIT :soutw-west ~@args))
(defmacro WEST [& args] `(EXIT :west ~@args))
(defmacro NW [& args] `(EXIT :north-west ~@args))
(defmacro UP [& args] `(EXIT :up ~@args))
(defmacro DOWN [& args] `(EXIT :down ~@args))
(defmacro IN [& args] `(EXIT :in ~@args))
(defmacro OUT [& args] `(EXIT :out ~@args))