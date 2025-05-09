Feature: Reset cache con attesa per sincronizzazione war

Background:

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario:

# Il reset della cache e' istantaneo solo per il backoffice, aspettiamo che tutti i war recepiscano la notifica di reset 
* call sleep(65000)
# * call sleep(70000)