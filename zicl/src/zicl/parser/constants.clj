(ns zicl.parser.constants
  (:require [zicl.globals :refer [CONSTANT]]))

;; Constants
(CONSTANT :cc-sbptr 0)
(CONSTANT :cc-septr 1)
(CONSTANT :cc-dbptr 2)
(CONSTANT :cc-deptr 3)
(CONSTANT :o-ptr 0)	            ; word pointer to unknown token in P-LEXV
(CONSTANT :o-start 1)	          ; word pointer to sentence start in P-LEXV
(CONSTANT :o-length 2)	        ; byte length of unparsed tokens in P-LEXV
(CONSTANT :o-end 3)	            ; byte pointer to first free byte in OOPS-INBUF
(CONSTANT :p-lexwords 1)        ; Word offset to start of LEXV entries
(CONSTANT :p-lexstart 1)        ; Number of words per LEXV entry
(CONSTANT :p-lexelen 2)
(CONSTANT :p-wordlen 4)         ; Offset to parts of speech byte
(CONSTANT :p-psoff 4)           ; Offset to first part of speech
(CONSTANT :p-p1off 5)           ; First part of speech bit mask in PSOFF byte
(CONSTANT :p-p1bits 3)
(CONSTANT :p-itbllen 9)
(CONSTANT :p-verb 0)
(CONSTANT :p-verbn 1)
(CONSTANT :p-prep1 2)
(CONSTANT :p-prep1n 3)
(CONSTANT :p-prep2 4)
(CONSTANT :p-prep2n 5)
(CONSTANT :p-nc1 6)
(CONSTANT :p-nc1l 7)
(CONSTANT :p-nc2 8)
(CONSTANT :p-nc2l 9)
(CONSTANT :p-synlen 8)
(CONSTANT :p-sbits 0)
(CONSTANT :p-sprep1 1)
(CONSTANT :p-sprep2 2)
(CONSTANT :p-sfwim1 3)
(CONSTANT :p-sfwim2 4)
(CONSTANT :p-sloc1 5)
(CONSTANT :p-sloc2 6)
(CONSTANT :p-saction 7)
(CONSTANT :p-sonums 3)
(CONSTANT :p-all 1)
(CONSTANT :p-one 2)
(CONSTANT :p-inhibit 4)
(CONSTANT :sh 128)
(CONSTANT :sc 64)
(CONSTANT :sir 32)
(CONSTANT :sog 16)
(CONSTANT :stake 8)
(CONSTANT :smany 4)
(CONSTANT :shave 2)
(CONSTANT :p-srcbot 2)
(CONSTANT :p-srctop 0)
(CONSTANT :p-srcall 1)