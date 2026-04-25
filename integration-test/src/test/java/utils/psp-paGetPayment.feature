Feature: Attivazione della rpt API SANP 2.4

#		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"), 
#		PAGAMENTO_ESEGUITO ("R01"), 
#		PAGAMENTO_NON_ESEGUITO ("R02"), 
#		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"), 
#		DECORRENZA_TERMINI ("R04"), 
#		DECORRENZA_TERMINI_PARZIALE ("R05"), 
#		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12");
 
Background:

* call read('classpath:utils/common-utils.feature')

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'
* def versionePagamento = '2'
* def riversamentoCumulativo = typeof riversamentoCumulativo != 'undefined' ? riversamentoCumulativo : 0
* def idCart = typeof idCart != 'undefined' ? idCart : getCurrentTimeMillis()

Scenario:

Given url ndpsym_psp_url
And path 'attiva'
And param codDominio = idDominio
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
And param riversamentoCumulativo = riversamentoCumulativo
And param versione = versionePagamento
And param idCart = idCart
And param inviaRicevuta = inviaRicevuta
When method get
Then assert responseStatus == 200
