Feature: Validazione sintattica avvisi ente

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica_estesa.feature')
* def idPendenza = getCurrentTimeMillis()
* def esitoAttivaRPT = {"faultCode":"PAA_SYSTEM_ERROR","faultString":"Errore generico.","id":"12345678901","description":"#notnull","serial": "#ignore"}

# Configurazione Applicazione per utilizzare le API V2


* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.servizioIntegrazione.versioneApi = 'REST v2'
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v2'

Given url backofficeBaseurl
And path 'applicazioni', idA2A
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Scenario: Numero avviso su multivoce

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_multivoce_bollo.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

# Attivo il pagamento 

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains 'numero avviso per una pendenza di tipo multivoce'

Scenario Outline: <field> non valida

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_definito.json')

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def ccp = getCurrentTimeMillis()
* def importo = 10.00

* set pendenzaPut.idA2A = idA2A
* set pendenzaPut.idPendenza = idPendenza
* set pendenzaPut.numeroAvviso = numeroAvviso

* set pendenzaVerificataV2.pendenza = pendenzaPut
* set pendenzaVerificataV2.stato = 'NON_ESEGUITA'

* set pendenzaVerificataV2.<fieldRequest> = <fieldValue>

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenzaVerificataV2
When method post
Then status 200

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| numeroAvviso | pendenza.numeroAvviso | buildNumeroAvviso(dominio, applicazione) | 'NumeroAvviso' |
| numeroAvviso | pendenza.numeroAvviso | null | 'NumeroAvviso' |
| idDominio | pendenza.idDominio | idDominio_2 | 'IdDominio' |
| importo | pendenza.importo | 0.01 | 'importo' |

Scenario: Caricamento pendenza con contabilita errore validazione importi

* def pendenzaPut = read('classpath:test/api/pendenza/v3/pendenze/put/msg/pendenza-put_monovoce_riferimento_contabilita.json')
* def pendenzaPutImportoOrig = pendenzaPut.voci[0].importo
* set pendenzaPut.voci[0].importo = pendenzaPut.voci[0].importo + 10

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* call read('classpath:utils/pa-prepara-avviso.feature')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* set pendenzaPut.idA2A = idA2A
* set pendenzaPut.idPendenza = idPendenza
* set pendenzaPut.numeroAvviso = numeroAvviso
* remove pendenzaPut.stato

* set pendenzaVerificataV2.pendenza = pendenzaPut
* set pendenzaVerificataV2.stato = 'NON_ESEGUITA'

Given url ente_api_url
And path '/v2/avvisi', idDominio, numeroAvviso
And request pendenzaVerificataV2
When method post
Then status 200

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description == '#("La voce ("+pendenzaPut.voci[0].idVocePendenza+") della pendenza (IdA2A:" + idA2A + ", Id:" + idPendenza + ") ha un importo (" + pendenzaPut.voci[0].importo + ") diverso dalla somma dei singoli importi definiti nella lista delle contabilita\' (" + pendenzaPutImportoOrig + ")")'

