Feature: Pagamento ad iniziativa PSP

# Fornire l'esito del pagamento atteso:
# 		PAGAMENTO_ESEGUITO_SENZA_RPT ("R00"), 
# 		PAGAMENTO_ESEGUITO ("R01"), 
# 		PAGAMENTO_NON_ESEGUITO ("R02"), 
# 		PAGAMENTO_PARZIALMENTE_ESEGUITO ("R03"), 
# 		DECORRENZA_TERMINI ("R04"), 
# 		DECORRENZA_TERMINI_PARZIALE ("R05"), 
# 		PAGAMENTO_ESEGUITO_SENZA_RPT_CON_RT("R12"); 

Scenario: Pagamento ad iniziativa PSP

# * def numeroAvviso = ??
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
# * def importo = ??

# Attivo il pagamento 

# * def tipoRicevuta = "??"

* def ndpsym_psp_url = ndpsym_url + '/psp/rs/psp'

Given url ndpsym_psp_url 
And path 'attiva' 
And param codDominio = idDominioPendenza
And param numeroAvviso = numeroAvviso
And param ccp = ccp
And param importo = importo
And param tipoRicevuta = tipoRicevuta
And param ibanAccredito = ibanAccredito
When method get
Then assert responseStatus == 200

# Verifico la notifica di terminazione

* configure retry = { count: 30, interval: 1000 }

Scenario: 

Given url ente_api_url
And path 'notificaTerminazione', idDominioPendenza, iuv, ccp
And retry until responseStatus == 200 
When method get
