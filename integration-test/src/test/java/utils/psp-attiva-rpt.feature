Feature: Attivazione della rpt

#		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"), 
#		PAGAMENTO_ESEGUITO ("R01"), 
#		PAGAMENTO_NON_ESEGUITO ("R02"), 
#		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"), 
#		DECORRENZA_TERMINI ("R04"), 
#		DECORRENZA_TERMINI_PARZIALE ("R05"), 
#		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12");
 
Background: 

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Scenario: 

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
When method get
Then assert responseStatus == 200
