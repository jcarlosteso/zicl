(ns zicl.parser.globals
  (:require [zicl.globals :refer [GLOBAL]]))

(defn- lexv [size] (byte-array (* size 4)))

;; Atoms (variables)
(GLOBAL :prsa false)
(GLOBAL :prsi false)
(GLOBAL :prso false)
(GLOBAL :p-table 0)
(GLOBAL :p-oneobj 0)
(GLOBAL :p-syntax 0)
(GLOBAL :p-cctbl [0 0 0 0])
(GLOBAL :p-len 0)
(GLOBAL :p-dir 0)
(GLOBAL :here 0)
(GLOBAL :winner 0)
(GLOBAL :p-lexv (lexv 60))
(GLOBAL :again-lexv (lexv 60))
(GLOBAL :reserve-lex (lexv 60))
(GLOBAL :reserve-ptr false)
(GLOBAL :p-inbuf {:length 0 :buffer (byte-array 120)})
(GLOBAL :oops-inbuf {:length 0 :buffer (byte-array 120)})
(GLOBAL :oops-table [false false false false])
(GLOBAL :p-cont false)
(GLOBAL :p-it-object false)
(GLOBAL :p-oflag false)
(GLOBAL :p-merged false)
(GLOBAL :p-aclause false)
(GLOBAL :p-anam false)
(GLOBAL :p-aadj false)
(GLOBAL :p-itbl [0 0 0 0 0 0 0 0 0 0])
(GLOBAL :p-otbl [0 0 0 0 0 0 0 0 0 0])
(GLOBAL :p-vtbl [0 0 0 0])
(GLOBAL :p-ovtbl (byte-array 4))
(GLOBAL :p-ncn 0)
(GLOBAL :quote-flag false)
(GLOBAL :p-end-on-prep false)
(GLOBAL :p-act false)
(GLOBAL :p-walk-dir false)
(GLOBAL :again-dir false)
(GLOBAL :p-number 0)
(GLOBAL :p-direction 0)
(GLOBAL :p-slocbits 0)
(GLOBAL :p-gwimbit #{})
(GLOBAL :p-nam false)
(GLOBAL :p-adj false)
(GLOBAL :p-adverb false)
(GLOBAL :p-adjn false)
(GLOBAL :p-prso (byte-array 100))
(GLOBAL :p-prsi (byte-array 100))
(GLOBAL :p-buts (byte-array 100))
(GLOBAL :p-merge (byte-array 100))
(GLOBAL :p-oclause (byte-array 200))
(GLOBAL :p-matchlen 0)
(GLOBAL :p-getflags 0)
(GLOBAL :p-and false)
(GLOBAL :p-xnam false)
(GLOBAL :p-xadj false)
(GLOBAL :p-xadjn false)
(GLOBAL :always-lit false)
