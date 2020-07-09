
(defglobal ?*TriggerMsg* = "")

(defglobal ?*MaxVal* = 0)
(defrule message1
	(A ?A)
	(weather ?weather)
	(age ?age)
	(gender ?gender)
	=>
	(bind ?w_per 0.2)
	(bind ?a_per 0.0)
	(bind ?g_per 0.1)
	(if (eq ?weather sunny)
		then
		(bind ?A (+ ?A ?w_per)))
	(if (eq ?age 25_40)
		then
		(bind ?A (+ ?A ?a_per)))
	(if (eq ?gender MALE)
		then
		(bind ?A (+ ?A ?g_per))
		else
		(bind ?A (+ ?A -1 ))
		)
	
    (if( > ?A ?*MaxVal*)  then 
	(bind ?*MaxVal* ?A )
	(bind ?*TriggerMsg* NECTI-Male )
	else 
	
	(bind ?*TriggerMsg* NECTI_Female )
	
	
	
	)
	(printout t "TriggerMsg:" ?*TriggerMsg* crlf)
	(printout t "MaxVal--------" ?*MaxVal* crlf)
	)
	(watch all)
	

					


	


