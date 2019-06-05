Feature: Configurazione

Background:

* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def tipoPendenzaDovuta = 'tipoPendenzaDovuta'

Scenario: configurazione anagrafica base

Given url backofficeBaseurl
And path 'tipiPendenza', tipoPendenzaDovuta
And headers basicAutenticationHeader
And request read('classpath:configurazione/v1/msg/tipoPendenza-dovuta-form.json')
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio, 'tipiPendenza', tipoPendenzaDovuta
And headers basicAutenticationHeader
And request {  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'tipiPendenza', tipoPendenzaDovuta
And headers basicAutenticationHeader
And request {  }
When method put
Then assert responseStatus == 200 || responseStatus == 201

#### resetCache
* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

