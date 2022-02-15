Feature: PA VerificaVersamento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def legacyGpAppBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPAppService', versione: 'v1', autenticazione: 'basic'})
* def legacyGpPrtBaseurl = getGovPayApiBaseUrl({api: 'legacy', ws: 'PagamentiTelematiciGPPrtService', versione: 'v1', autenticazione: 'basic'})
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def esitoAttivaRPT = {"faultCode":"PAA_SYSTEM_ERROR","faultString":"Errore generico.","id":"12345678901","description":"#notnull","serial": "#ignore"}
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

* def idA2A_ESSE3 = 'IDA2A01-ESSE3'
* def idA2A_ESSE3BasicAutenticationHeader = getBasicAuthenticationHeader( { username: idA2A_ESSE3, password: pwdA2A } )

# Elimino il dominio da quelli autorizzati per tutte le applicazioni

Given url backofficeBaseurl
And path 'applicazioni'
And headers gpAdminBasicAutenticationHeader
When method get
Then assert responseStatus == 200

* def result = call read('classpath:utils/govpay-elimina-domini-applicazione.feature') response.risultati

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

# Creo Applicazione per utilizzare Handler WsSEC

* def applicazione = read('classpath:configurazione/v1/msg/applicazione.json')
* set applicazione.principal = idA2A_ESSE3
* set applicazione.servizioIntegrazione.versioneApi = 'SOAP v1'
* set applicazione.servizioIntegrazione.url = ente_api_url + '/v1/SOAP'

Given url backofficeBaseurl
And path 'applicazioni', idA2A_ESSE3
And headers gpAdminBasicAutenticationHeader
And request applicazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

Scenario: Verifica Pendenza OK

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def idA2A = idA2A_ESSE3
* def pendenzaPut = read('classpath:test/api/legacy/v1/client/msg/gpVerificaVersamentoResponse-riferimento.xml')
* def ccp = getCurrentTimeMillis()
* xml pendenzaVerificata = pendenzaPut
* def importo = $pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/importoTotale
* def causaleToCheck = $pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/causale

* call read('classpath:utils/pa-prepara-avviso-soap.feature')

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '#notnull'}
* match response.dati.importoSingoloVersamento == importo
* match response.dati.causaleVersamento == causaleToCheck

Scenario Outline: <field> non valida

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def idA2A = idA2A_ESSE3
* def pendenzaPut = read('classpath:test/api/legacy/v1/client/msg/gpVerificaVersamentoResponse-riferimento.xml')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* xml pendenzaVerificata = pendenzaPut
* set pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/<fieldRequest> = <fieldValue>

* call read('classpath:utils/pa-prepara-avviso-soap.feature')

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| causale | causale | loremIpsum | 'is not facet-valid with respect to maxLength \'140\' for type \'string140\'.' |
| causale | causale | '' | 'cvc-minLength-valid' |
| iuv | iuv | loremIpsum | 'is not facet-valid with respect to pattern \'[^ ]*\' for type \'cod35\'.' |
| iuv | iuv | 'ABC000000000000' | 'iuv' |
| dataScadenza | dataScadenza | '2030-19-40' | 'Invalid value 19 for Month field.' |
| annoTributario | annoTributario | 'aaaa' | 'Not a number: aaaa' |
| importoTotale | importoTotale | '10.001' | 'Value \'10.001\' has 3 fraction digits, but the number of fraction digits has been limited to 2.' |
| importoTotale | importoTotale | '10,000' | 'java.lang.NumberFormatException' |
| importoTotale | importoTotale | '10,00.0' | 'java.lang.NumberFormatException' |
| importoTotale | importoTotale | 'aaaa' | 'java.lang.NumberFormatException' |

Scenario Outline: <field> non valida

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def idA2A = idA2A_ESSE3
* def pendenzaPut = read('classpath:test/api/legacy/v1/client/msg/gpVerificaVersamentoResponse-riferimento.xml')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* xml pendenzaVerificata = pendenzaPut
* set pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/singoloVersamento/<fieldRequest> = <fieldValue>

* call read('classpath:utils/pa-prepara-avviso-soap.feature')

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| singoloVersamento.codSingoloVersamentoEnte | codSingoloVersamentoEnte | loremIpsum | 'is not facet-valid with respect to pattern \'[^ ]*\' for type \'cod35\'.' |
| singoloVersamento.importo | importo | '10.001' | 'Value \'10.001\' has 3 fraction digits, but the number of fraction digits has been limited to 2.' |
| singoloVersamento.importo | importo | '10,000' | 'java.lang.NumberFormatException' |
| singoloVersamento.importo | importo | '10,00.0' | 'java.lang.NumberFormatException' |
| singoloVersamento.importo | importo | 'aaaa' | 'java.lang.NumberFormatException' |
| singoloVersamento.codTributo | codTributo | 'xxxxx' | 'xxxxx' |


Scenario Outline: <field> non valida

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def idA2A = idA2A_ESSE3
* def pendenzaPut = read('classpath:test/api/legacy/v1/client/msg/gpVerificaVersamentoResponse-definito.xml')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* xml pendenzaVerificata = pendenzaPut
* set pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/singoloVersamento/tributo/<fieldRequest> = <fieldValue>

* call read('classpath:utils/pa-prepara-avviso-soap.feature')

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| singoloVersamento.ibanAccredito | ibanAccredito | 'ASC402002040500330560600203' | 'ibanAccredito' |
| singoloVersamento.tipoContabilita | tipoContabilita | 'xxx' | 'Value \'xxx\' is not facet-valid with respect to enumeration \'[CAPITOLO, SPECIALE, SIOPE, ALTRO]\'. It must be a value from the enumeration.' |
| singoloVersamento.codContabilita | codContabilita | '' | 'codContabilita' |


Scenario Outline: <field> non valida

* def numeroAvviso = buildNumeroAvviso(dominio, applicazione)
* def iuv = getIuvFromNumeroAvviso(numeroAvviso)	
* def generaIuv = false
* def idPendenza = getCurrentTimeMillis()
* def idA2A = idA2A_ESSE3
* def pendenzaPut = read('classpath:test/api/legacy/v1/client/msg/gpVerificaVersamentoResponse-bollo.xml')
* def ccp = getCurrentTimeMillis()
* def importo = 100.99

* xml pendenzaVerificata = pendenzaPut
* set pendenzaVerificata /Envelope/Body/paVerificaVersamentoResponse/versamento/singoloVersamento/bolloTelematico/<fieldRequest> = <fieldValue>

* call read('classpath:utils/pa-prepara-avviso-soap.feature')

* def tipoRicevuta = "R01"
* call read('classpath:utils/psp-attiva-rpt.feature')
* match response contains { dati: '##null'}
* match response.faultBean == esitoAttivaRPT
* match response.faultBean.description contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| singoloVersamento.tipo | tipo | "09" | 'Value \'09\' is not facet-valid with respect to enumeration \'[01]\'. It must be a value from the enumeration.' |
| singoloVersamento.hash | hash | loremIpsum | 'with length = \'730\' is not facet-valid with respect to maxLength \'70\' for type \'string70\'.' |
| singoloVersamento.provincia | provincia | "xxx" | 'Value \'xxx\' is not facet-valid with respect to pattern \'[A-Z]{2,2}\' for type \'string2\'.' |






