Feature: Rendicontazioni

Background:



Scenario: Rendicontazioni

* call read('classpath:utils/common-utils.feature')
* call read('classpath:configurazione/v1/anagrafica.feature')
* call read('classpath:utils/nodo-genera-rendicontazioni.feature')
* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataInizioFR = getDateTime()
* call sleep(1000)

* call read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v3.feature')

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio
When method get
Then assert responseStatus == 200

* def idflusso_dom1_1 = response.response.rendicontazioni[0].identificativoFlusso

Given url ndpsym_rendicontazioni_url 
And path 'genera', idDominio_2
When method get
Then assert responseStatus == 200

* def idflusso_dom2_1 = response.response.rendicontazioni[0].identificativoFlusso

* call read('classpath:utils/govpay-op-acquisisci-rendicontazioni.feature')

* call sleep(1000)
* def dataFineFR = getDateTime()
* call sleep(1000)