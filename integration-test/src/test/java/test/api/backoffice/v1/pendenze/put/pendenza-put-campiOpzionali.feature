Feature: Caricamento pendenza multivoce

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})

Scenario Outline: Caricamento pendenza con controllo del campo opzionale <field> 

* set pendenzaPut.<field> = <value>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def checkValue = <value> != null ? <value> : '#notpresent'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
When method get
Then status 200
And match response.<field> == checkValue

Examples:
| field | value |
| cartellaPagamento | "ABC00000001" |
| cartellaPagamento | null |
| annoRiferimento | 2020 |
| annoRiferimento | null |
| tassonomia | 'Tassonomia Custom' |
| tassonomia | null |
| direzione | 'Direzione_Test' |
| direzione | null |
| divisione | 'Divisione_Test' |
| divisione | null |