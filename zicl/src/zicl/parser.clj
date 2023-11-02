(ns zicl.parser
  (:require [zicl.globals :refer [SETG]]
            [zicl.parser.globals :as g]
            [zicl.rooms :refer [FSET?]]))

(defn- clear-word [barray index]
  (let [offset (* 2 index)]
    (-> barray
        (aset-byte offset (byte 0))
        (aset-byte (inc offset) 0))))

(defn- global-object? [obj]
  (let [{:keys [object scope]} (meta #'obj)]
    (and (= :object object) (= :global scope))))

(defn- room? [obj]
  (-> #'obj
      meta
      :object
      (= :room)))

(defn- META-LOC [object]
  (loop [obj object]
    (cond
      (nil? obj)           false
      (global-object? obj) :global-objects
      (room? obj)          obj
      :else                (recur (:loc obj)))))

;; <ROUTINE LIT? (RM "OPTIONAL" (RMBIT T) "AUX" OHERE (LIT <>))
;; 	<COND (<AND ,ALWAYS-LIT <EQUAL? ,WINNER ,PLAYER>>                    ;; Si g/ALWAYS-LIT Y el actor es el jugador...
;; 	       <RTRUE>)>                                                     ;;   devuelve TRUE y sal.
;; 	<SETG P-GWIMBIT ,ONBIT>                                              ;; Prepara el filtro de Get What I Mean para que busque objetos "encendidos"
;; 	<SET OHERE ,HERE>                                                    ;; Guarda en "AUX" OHERE la ubicación actual
;; 	<SETG HERE .RM>                                                      ;; Haz que la ubicación actual sea RM
;; 	<COND (<AND .RMBIT <FSET? .RM ,ONBIT>>                               ;; Si RMBIT Y RM tiene el flag de "iluminada"...
;;         <SET LIT T>)                                                  ;;   Haz que "AUX" LIT sea true,
;; 	      (T                                                             ;; En caso contrario...
;; 	       <PUT ,P-MERGE ,P-MATCHLEN 0>                                  ;;   Borra en el byte-array g/P-MERGE la "palabra" indicada por g/P-MATCHLEN
;; 	       <SETG P-TABLE ,P-MERGE>                                       ;;   Copia g/P-MERGE en g/P-TABLE
;; 	       <SETG P-SLOCBITS -1>                                          ;;   Guarda -1 en g/SLOCBITS
;; 	       <COND (<EQUAL? .OHERE .RM>                                    ;;   Si "AUX" OHERE = RM
;; 		            <DO-SL ,WINNER 1 1>                                    ;;     Llama a DO-SL !!
;; 		            <COND (<AND <NOT <EQUAL? ,WINNER ,PLAYER>>
;; 				                    <IN? ,PLAYER .RM>>
;; 			                 <DO-SL ,PLAYER 1 1>)>)>
;; 	       <DO-SL .RM 1 1>
;; 	       <COND (<G? <GET ,P-TABLE ,P-MATCHLEN> 0>
;;                <SET LIT T>)>)>
;; 	<SETG HERE .OHERE>
;; 	<SETG P-GWIMBIT 0>
;; 	.LIT>

(defn LIT?
  ([room] (LIT? room true))
  ([room switch]
   (if (and @g/ALWAYS-LIT (= @g/WINNER @g/PLAYER))
     true
     (let [here @g/HERE]
       (SETG g/P-GWIMBIT #{:onbit})
       (SETG g/HERE room)
       (cond
         (and switch (FSET? room :onbit)))))))

;; <ROUTINE PARSER ("AUX" (PTR ,P-LEXSTART) WRD (VAL 0) (VERB <>) (OF-FLAG <>) OWINNER OMERGED LEN (DIR <>) (NW 0) (LW 0) (CNT -1))
;; ***** COPY ,P-ITBL INTO ,P-OTBL AND CLEAR ,P-ITBL
;; 	<REPEAT ()
;; 		<COND (<G? <SET CNT <+ .CNT 1>> ,P-ITBLLEN>
;;               <RETURN>)
;; 		      (T
;; 		       <COND (<NOT ,P-OFLAG>
;; 			            <PUT ,P-OTBL .CNT <GET ,P-ITBL .CNT>>)>
;; 		       <PUT ,P-ITBL .CNT 0>)>>
;;
;;
;; 	<SET OWINNER ,WINNER>
;; 	<SET OMERGED ,P-MERGED>
;; 	<SETG P-ADVERB <>>
;; 	<SETG P-MERGED <>>
;; 	<SETG P-END-ON-PREP <>>
;; 	<PUT ,P-PRSO ,P-MATCHLEN 0>
;; 	<PUT ,P-PRSI ,P-MATCHLEN 0>
;; 	<PUT ,P-BUTS ,P-MATCHLEN 0>
;; 	<COND (<AND <NOT ,QUOTE-FLAG> <N==? ,WINNER ,PLAYER>>
;; 	       <SETG WINNER ,PLAYER>
;; 	       <SETG HERE <META-LOC ,PLAYER>>
;; 	       ;<COND (<NOT <FSET? <LOC ,WINNER> ,VEHBIT>>
;; 		      <SETG HERE <LOC ,WINNER>>)>
;; 	       <SETG LIT <LIT? ,HERE>>)>
;; 	<COND (,RESERVE-PTR
;; 	       <SET PTR ,RESERVE-PTR>
;; 	       <STUFF ,RESERVE-LEXV ,P-LEXV>
;; 	       <COND (<AND <NOT ,SUPER-BRIEF> <EQUAL? ,PLAYER ,WINNER>>
;; 		      <CRLF>)>
;; 	       <SETG RESERVE-PTR <>>
;; 	       <SETG P-CONT <>>)
;; 	      (,P-CONT
;; 	       <SET PTR ,P-CONT>
;; 	       <COND (<AND <NOT ,SUPER-BRIEF>
;; 			   <EQUAL? ,PLAYER ,WINNER>
;; 			   <NOT <VERB? SAY>>>
;; 		      <CRLF>)>
;; 	       <SETG P-CONT <>>)
;; 	      (T
;; 	       <SETG WINNER ,PLAYER>
;; 	       <SETG QUOTE-FLAG <>>
;; 	       <COND (<NOT <FSET? <LOC ,WINNER> ,VEHBIT>>
;; 		      <SETG HERE <LOC ,WINNER>>)>
;; 	       <SETG LIT <LIT? ,HERE>>
;; 	       <COND (<NOT ,SUPER-BRIEF> <CRLF>)>
;; 	       <TELL ">">
;; 	       <READ ,P-INBUF ,P-LEXV>)>
;; 	<SETG P-LEN <GETB ,P-LEXV ,P-LEXWORDS>>
;; 	<COND (<ZERO? ,P-LEN> <TELL "I beg your pardon?" CR> <RFALSE>)>
;; 	<COND (<EQUAL? <SET WRD <GET ,P-LEXV .PTR>> ,W?OOPS>
;; 	       <COND (<EQUAL? <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>
;; 			      ,W?PERIOD ,W?COMMA>
;; 		      <SET PTR <+ .PTR ,P-LEXELEN>>
;; 		      <SETG P-LEN <- ,P-LEN 1>>)>
;; 	       <COND (<NOT <G? ,P-LEN 1>>
;; 		      <TELL "I can't help your clumsiness." CR>
;; 		      <RFALSE>)
;; 		     (<GET ,OOPS-TABLE ,O-PTR>
;; 		      <COND (<AND <G? ,P-LEN 2>
;; 				  <EQUAL? <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>
;; 					  ,W?QUOTE>>
;; 			     <TELL
;; "Sorry, you can't correct mistakes in quoted text." CR>
;; 			     <RFALSE>)
;; 			    (<G? ,P-LEN 2>
;; 			     <TELL
;; "Warning: only the first word after OOPS is used." CR>)>
;; 		      <PUT ,AGAIN-LEXV <GET ,OOPS-TABLE ,O-PTR>
;; 			   <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>>
;; 		      <SETG WINNER .OWINNER> ;"maybe fix oops vs. chars.?"
;; 		      <INBUF-ADD <GETB ,P-LEXV <+ <* .PTR ,P-LEXELEN> 6>>
;; 				 <GETB ,P-LEXV <+ <* .PTR ,P-LEXELEN> 7>>
;; 				 <+ <* <GET ,OOPS-TABLE ,O-PTR> ,P-LEXELEN> 3>>
;; 		      <STUFF ,AGAIN-LEXV ,P-LEXV>
;; 		      <SETG P-LEN <GETB ,P-LEXV ,P-LEXWORDS>>
;; 		      <SET PTR <GET ,OOPS-TABLE ,O-START>>
;; 		      <INBUF-STUFF ,OOPS-INBUF ,P-INBUF>)
;; 		     (T
;; 		      <PUT ,OOPS-TABLE ,O-END <>>
;; 		      <TELL "There was no word to replace!" CR>
;; 		      <RFALSE>)>)
;; 	      (T
;; 	       <COND (<NOT <EQUAL? .WRD ,W?AGAIN ,W?G>>
;; 		      <SETG P-NUMBER 0>)>
;; 	       <PUT ,OOPS-TABLE ,O-END <>>)>
;; 	<COND (<EQUAL? <GET ,P-LEXV .PTR> ,W?AGAIN ,W?G>
;; 	       <COND (<ZERO? <GETB ,OOPS-INBUF 1>>
;; 		      <TELL "Beg pardon?" CR>
;; 		      <RFALSE>)
;; 		     (,P-OFLAG
;; 		      <TELL "It's difficult to repeat fragments." CR>
;; 		      <RFALSE>)
;; 		     (<NOT ,P-WON>
;; 		      <TELL "That would just repeat a mistake." CR>
;; 		      <RFALSE>)
;; 		     (<G? ,P-LEN 1>
;; 		      <COND (<OR <EQUAL? <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>
;; 					,W?PERIOD ,W?COMMA ,W?THEN>
;; 				 <EQUAL? <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>
;; 					,W?AND>>
;; 			     <SET PTR <+ .PTR <* 2 ,P-LEXELEN>>>
;; 			     <PUTB ,P-LEXV ,P-LEXWORDS
;; 				   <- <GETB ,P-LEXV ,P-LEXWORDS> 2>>)
;; 			    (T
;; 			     <TELL "I couldn't understand that sentence." CR>
;; 			     <RFALSE>)>)
;; 		     (T
;; 		      <SET PTR <+ .PTR ,P-LEXELEN>>
;; 		      <PUTB ,P-LEXV ,P-LEXWORDS
;; 			    <- <GETB ,P-LEXV ,P-LEXWORDS> 1>>)>
;; 	       <COND (<G? <GETB ,P-LEXV ,P-LEXWORDS> 0>
;; 		      <STUFF ,P-LEXV ,RESERVE-LEXV>
;; 		      <SETG RESERVE-PTR .PTR>)
;; 		     (T
;; 		      <SETG RESERVE-PTR <>>)>
;; 	       ;<SETG P-LEN <GETB ,AGAIN-LEXV ,P-LEXWORDS>>
;; 	       <SETG WINNER .OWINNER>
;; 	       <SETG P-MERGED .OMERGED>
;; 	       <INBUF-STUFF ,OOPS-INBUF ,P-INBUF>
;; 	       <STUFF ,AGAIN-LEXV ,P-LEXV>
;; 	       <SET CNT -1>
;; 	       <SET DIR ,AGAIN-DIR>
;; 	       <REPEAT ()
;; 		<COND (<IGRTR? CNT ,P-ITBLLEN> <RETURN>)
;; 		      (T <PUT ,P-ITBL .CNT <GET ,P-OTBL .CNT>>)>>)
;; 	      (T
;; 	       <STUFF ,P-LEXV ,AGAIN-LEXV>
;; 	       <INBUF-STUFF ,P-INBUF ,OOPS-INBUF>
;; 	       <PUT ,OOPS-TABLE ,O-START .PTR>
;; 	       <PUT ,OOPS-TABLE ,O-LENGTH <* 4 ,P-LEN>>
;; 	       <SET LEN
;; 		    <* 2 <+ .PTR <* ,P-LEXELEN <GETB ,P-LEXV ,P-LEXWORDS>>>>>
;; 	       <PUT ,OOPS-TABLE ,O-END <+ <GETB ,P-LEXV <- .LEN 1>>
;; 					  <GETB ,P-LEXV <- .LEN 2>>>>
;; 	       <SETG RESERVE-PTR <>>
;; 	       <SET LEN ,P-LEN>
;; 	       <SETG P-DIR <>>
;; 	       <SETG P-NCN 0>
;; 	       <SETG P-GETFLAGS 0>
;; 	       <REPEAT ()
;; 		<COND (<L? <SETG P-LEN <- ,P-LEN 1>> 0>
;; 		       <SETG QUOTE-FLAG <>>
;; 		       <RETURN>)
;; 		      (<OR <SET WRD <GET ,P-LEXV .PTR>>
;; 			   <SET WRD <NUMBER? .PTR>>>
;; 		       <COND (<ZERO? ,P-LEN> <SET NW 0>)
;; 			     (T <SET NW <GET ,P-LEXV <+ .PTR ,P-LEXELEN>>>)>
;; 		       <COND (<AND <EQUAL? .WRD ,W?TO>
;; 				   <EQUAL? .VERB ,ACT?TELL ;,ACT?ASK>>
;; 			      <SET WRD ,W?QUOTE>)
;; 			     (<AND <EQUAL? .WRD ,W?THEN>
;; 				   <G? ,P-LEN 0>
;; 				   <NOT .VERB>
;; 				   <NOT ,QUOTE-FLAG> ;"Last NOT added 7/3">
;; 			      <COND (<EQUAL? .LW 0 ,W?PERIOD>
;; 				     <SET WRD ,W?THE>)
;; 				    (ELSE
;; 				     <PUT ,P-ITBL ,P-VERB ,ACT?TELL>
;; 				     <PUT ,P-ITBL ,P-VERBN 0>
;; 				     <SET WRD ,W?QUOTE>)>)>
;; 		       <COND (<EQUAL? .WRD ,W?THEN ,W?PERIOD ,W?QUOTE>
;; 			      <COND (<EQUAL? .WRD ,W?QUOTE>
;; 				     <COND (,QUOTE-FLAG
;; 					    <SETG QUOTE-FLAG <>>)
;; 					   (T <SETG QUOTE-FLAG T>)>)>
;; 			      <OR <ZERO? ,P-LEN>
;; 				  <SETG P-CONT <+ .PTR ,P-LEXELEN>>>
;; 			      <PUTB ,P-LEXV ,P-LEXWORDS ,P-LEN>
;; 			      <RETURN>)
;; 			     (<AND <SET VAL
;; 					<WT? .WRD
;; 					     ,PS?DIRECTION
;; 					     ,P1?DIRECTION>>
;; 				   <EQUAL? .VERB <> ,ACT?WALK>
;; 				   <OR <EQUAL? .LEN 1>
;; 				       <AND <EQUAL? .LEN 2>
;; 					    <EQUAL? .VERB ,ACT?WALK>>
;; 				       <AND <EQUAL? .NW
;; 					            ,W?THEN
;; 					            ,W?PERIOD
;; 					            ,W?QUOTE>
;; 					    <NOT <L? .LEN 2>>>
;; 				       <AND ,QUOTE-FLAG
;; 					    <EQUAL? .LEN 2>
;; 					    <EQUAL? .NW ,W?QUOTE>>
;; 				       <AND <G? .LEN 2>
;; 					    <EQUAL? .NW ,W?COMMA ,W?AND>>>>
;; 			      <SET DIR .VAL>
;; 			      <COND (<EQUAL? .NW ,W?COMMA ,W?AND>
;; 				     <PUT ,P-LEXV
;; 					  <+ .PTR ,P-LEXELEN>
;; 					  ,W?THEN>)>
;; 			      <COND (<NOT <G? .LEN 2>>
;; 				     <SETG QUOTE-FLAG <>>
;; 				     <RETURN>)>)
;; 			     (<AND <SET VAL <WT? .WRD ,PS?VERB ,P1?VERB>>
;; 				   <NOT .VERB>>
;; 			      <SET VERB .VAL>
;; 			      <PUT ,P-ITBL ,P-VERB .VAL>
;; 			      <PUT ,P-ITBL ,P-VERBN ,P-VTBL>
;; 			      <PUT ,P-VTBL 0 .WRD>
;; 			      <PUTB ,P-VTBL 2 <GETB ,P-LEXV
;; 						    <SET CNT
;; 							 <+ <* .PTR 2> 2>>>>
;; 			      <PUTB ,P-VTBL 3 <GETB ,P-LEXV <+ .CNT 1>>>)
;; 			     (<OR <SET VAL <WT? .WRD ,PS?PREPOSITION 0>>
;; 				  <EQUAL? .WRD ,W?ALL ,W?ONE ;,W?BOTH>
;; 				  <WT? .WRD ,PS?ADJECTIVE>
;; 				  <WT? .WRD ,PS?OBJECT>>
;; 			      <COND (<AND <G? ,P-LEN 1>
;; 					  <EQUAL? .NW ,W?OF>
;; 					  <ZERO? .VAL>
;; 					  <NOT <EQUAL? .WRD
;; 						       ,W?ALL ,W?ONE ,W?A>>
;; 					  ;<NOT <EQUAL? .WRD ,W?BOTH>>>
;; 				     <SET OF-FLAG T>)
;; 				    (<AND <NOT <ZERO? .VAL>>
;; 				          <OR <ZERO? ,P-LEN>
;; 					      <EQUAL? .NW ,W?THEN ,W?PERIOD>>>
;; 				     <SETG P-END-ON-PREP T>
;; 				     <COND (<L? ,P-NCN 2>
;; 					    <PUT ,P-ITBL ,P-PREP1 .VAL>
;; 					    <PUT ,P-ITBL ,P-PREP1N .WRD>)>)
;; 				    (<EQUAL? ,P-NCN 2>
;; 				     <TELL
;; "There were too many nouns in that sentence." CR>
;; 				     <RFALSE>)
;; 				    (T
;; 				     <SETG P-NCN <+ ,P-NCN 1>>
;; 				     <SETG P-ACT .VERB>
;; 				     <OR <SET PTR <CLAUSE .PTR .VAL .WRD>>
;; 					 <RFALSE>>
;; 				     <COND (<L? .PTR 0>
;; 					    <SETG QUOTE-FLAG <>>
;; 					    <RETURN>)>)>)
;; 			     (<EQUAL? .WRD ,W?OF>
;; 			      <COND (<OR <NOT .OF-FLAG>
;; 					 <EQUAL? .NW ,W?PERIOD ,W?THEN>>
;; 				     <CANT-USE .PTR>
;; 				     <RFALSE>)
;; 				    (T
;; 				     <SET OF-FLAG <>>)>)
;; 			     (<WT? .WRD ,PS?BUZZ-WORD>)
;; 			     (<AND <EQUAL? .VERB ,ACT?TELL>
;; 				   <WT? .WRD ,PS?VERB ,P1?VERB>
;; 				   <EQUAL? ,WINNER ,PLAYER>>
;; 			      <TELL
;; "Please consult your manual for the correct way to talk to other people
;; or creatures." CR>
;; 			      <RFALSE>)
;; 			     (T
;; 			      <CANT-USE .PTR>
;; 			      <RFALSE>)>)
;; 		      (T
;; 		       <UNKNOWN-WORD .PTR>
;; 		       <RFALSE>)>
;; 		<SET LW .WRD>
;; 		<SET PTR <+ .PTR ,P-LEXELEN>>>)>
;; 	<PUT ,OOPS-TABLE ,O-PTR <>>
;; 	<COND (.DIR
;; 	       <SETG PRSA ,V?WALK>
;; 	       <SETG PRSO .DIR>
;; 	       <SETG P-OFLAG <>>
;; 	       <SETG P-WALK-DIR .DIR>
;; 	       <SETG AGAIN-DIR .DIR>)
;; 	      (ELSE
;; 	       <COND (,P-OFLAG <ORPHAN-MERGE>)>
;; 	       <SETG P-WALK-DIR <>>
;; 	       <SETG AGAIN-DIR <>>
;; 	       <COND (<AND <SYNTAX-CHECK>
;; 			   <SNARF-OBJECTS>
;; 			   <MANY-CHECK>
;; 			   <TAKE-CHECK>>
;; 		      T)>)>>

(defn PARSER []
  ;; First <REPEAT () ...>
  (reset! g/P-OTBL @g/P-ITBL)
  (reset! g/P-ITBL (vec (repeat 10 0)))

  (let [OWINNER @g/WINNER
        OMERGED @g/P-MERGED]
    (reset! g/P-ADVERB false)
    (reset! g/P-MERGED false)
    (reset! g/P-END-ON-PREP false)
    (reset! g/P-PRSO (clear-word @g/P-PRSO @g/P-MATCHLEN))
    (reset! g/P-PRSI (clear-word @g/P-PRSI @g/P-MATCHLEN))
    (reset! g/P-BUTS (clear-word @g/P-buts @g/P-MATCHLEN))
    (when (and (not @g/QUOTE-FLAG) (not= g/WINNER g/PLAYER))
      (reset! g/WINNER @g/PLAYER)
      (reset! g/HERE (META-LOC @g/PLAYER))))

  ;; Let OWINNER = WINNER
  ;; Let OMERGED = P-MERGED
  ;; Let P-ADVERB = <>
  ;; Let P-MERGED = <>
  )