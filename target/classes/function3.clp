(deffunction max1 (?a ?b ) (if( > ?b ?*MaxVal*) then (bind ?*TriggerMsg* ?a) (bind ?*MaxVal* ?b)))
 (defglobal ?*MaxVal* = 0)
   (defglobal ?*TriggerMsg* = "")
 
   (defrule message1
	(A ?A)
	(weather sunny)
	(age <=18)
	(gender MALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.2)
		(bind ?A (+ ?A ?w_per))

		(bind ?A (+ ?A ?a_per))

		(bind ?A (+ ?A ?g_per))
	
	(max1 Flight_1 ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	(defrule message2
	(A ?A)
	(weather Cloudy)
	(age 18_25)
	(gender MALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 Men_1 ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	(defrule message3
	(A ?A)
	(weather Cloudy)
	(age 25_40)
	(gender MALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 Men_2 ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
(defrule message4
	(A ?A)
	(weather Cloudy)
	(age 40_60)
	(gender MALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 Men_3 ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	(defrule message5
	(A ?A)
	(weather Mostly sunny)
	(age >=60)
	(gender MALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 NEC_100_MALE ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	(defrule message6
	(A ?A)
	(weather Mostly sunny)
	(age <=18)
	(gender FEMALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.2)
		(bind ?A (+ ?A ?w_per))

		(bind ?A (+ ?A ?a_per))

		(bind ?A (+ ?A ?g_per))
	
	(max1 NECTI_ANIMAL ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	(defrule message7
	(A ?A)
	(weather sunny)
	(age 18_25)
	(gender FEMALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 NECTI_Assetscar ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	(defrule message8
	(A ?A)
	(weather sunny)
	(age 25_40)
	(gender FEMALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 NECTI_Drink ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
(defrule message9
	(A ?A)
	(weather Mostly sunny)
	(age 40_60)
	(gender FEMALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 NECTI_shoes ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	(defrule message10
	(A ?A)
	(weather Mostly sunny)
	(age >=60)
	(gender FEMALE)
	=>
	(bind ?w_per 0.3)
	(bind ?a_per 0.5)
	(bind ?g_per 0.3)
	
		(bind ?A (+ ?A ?w_per))
	
		(bind ?A (+ ?A ?a_per))
	
		(bind ?A (+ ?A ?g_per))
	
	(max1 NECTI_Woman ?A)
	(printout t "TriggerMsg:-->>" ?*TriggerMsg* crlf)
	(printout t "MaxVal:-->>" ?*MaxVal* crlf)
	)
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
