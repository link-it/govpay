Feature: Setup pagamenti

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')

* callonce read('classpath:configurazione/v1/operazioni-resetCacheConSleep.feature')

Scenario: Setup Pagamenti

* call read('classpath:utils/workflow/modellounico/v1/modellounico-bunch-pagamenti-v3-no-conf.feature')