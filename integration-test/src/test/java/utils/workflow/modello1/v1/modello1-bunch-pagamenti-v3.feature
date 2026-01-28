Feature: Setup pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Setup Pagamenti

* call read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v3-no-conf.feature')