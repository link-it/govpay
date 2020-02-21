Feature: Validazione sintattica domini

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def configurazione_generale = read('msg/configurazione_generale.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def string71 = '1GLqJdabGYFpRi4RbM8gWlnpCzVvMyeKC2qoCYkqfvTyGZ1eovAxsFqpGfVqzzXXjCfMsKi'
* def string17 = 'LS2wIWYPN0QPsgTbX'
* def string36 = 'VTnniDMiQ2ngyoDMBnfzeGUPKTbhx2U7fMO1'

Scenario Outline: Configurazione non valida: <path>
 
* set configurazione_generale.<path> = <value>

Given url backofficeBaseurl
And path 'configurazioni'
And headers basicAutenticationHeader
And request configurazione_generale
When method post
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains '<field>'

Examples:
| path | field | value | 
| giornaleEventi.interfacce.apiEnte.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiEnte.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiEnte.letture | letture | null |
| giornaleEventi.interfacce.apiEnte.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiEnte.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiEnte.scritture | scritture | null |
| giornaleEventi.interfacce.apiPagamento.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPagamento.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPagamento.letture | letture | null |
| giornaleEventi.interfacce.apiPagamento.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPagamento.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPagamento.scritture | scritture | null |
| giornaleEventi.interfacce.apiRagioneria.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiRagioneria.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiRagioneria.letture | letture | null |
| giornaleEventi.interfacce.apiRagioneria.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiRagioneria.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiRagioneria.scritture | scritture | null |
| giornaleEventi.interfacce.apiBackoffice.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiBackoffice.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiBackoffice.letture | letture | null |
| giornaleEventi.interfacce.apiBackoffice.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiBackoffice.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiBackoffice.scritture | scritture | null |
| giornaleEventi.interfacce.apiPagoPA.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPagoPA.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPagoPA.letture | letture | null |
| giornaleEventi.interfacce.apiPagoPA.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPagoPA.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPagoPA.scritture | scritture | null |
| giornaleEventi.interfacce.apiPendenze.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPendenze.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPendenze.letture | letture | null |
| giornaleEventi.interfacce.apiPendenze.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiPendenze.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiPendenze.scritture | scritture | null |
| giornaleEventi.interfacce.apiBackendIO.letture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiBackendIO.letture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiBackendIO.letture | letture | null |
| giornaleEventi.interfacce.apiBackendIO.scritture.log | log | 'xxxx' |
| giornaleEventi.interfacce.apiBackendIO.scritture.dump | dump | 'xxxx' |
| giornaleEventi.interfacce.apiBackendIO.scritture | scritture | null |
| giornaleEventi.interfacce.prova | prova | 'xxxx' |
| hardening | hardening | null |
| hardening.abilitato | abilitato | 'xxx' |
| hardening.abilitato | abilitato | null |
| hardening.captcha.serverURL | serverURL | loremIpsum |
| hardening.captcha.serverURL | serverURL | null |
| hardening.captcha.siteKey | siteKey | null |
| hardening.captcha.secretKey | secretKey | null |
| hardening.captcha.soglia | soglia | 2 |
| hardening.captcha.soglia | soglia | true |
| hardening.captcha.soglia | soglia | 'soglia' |
| hardening.captcha.soglia | soglia | null |
| hardening.captcha.parametro | parametro | null |
| hardening.captcha.parametro | parametro | '/&$ #' |
| hardening.captcha.connectionTimeout | connectionTimeout | null |
| hardening.captcha.connectionTimeout | connectionTimeout | true |
| hardening.captcha.connectionTimeout | connectionTimeout | 'aaa' |
| hardening.captcha.connectionTimeout | connectionTimeout | loremIpsum |
| hardening.captcha.readTimeout | readTimeout | null |
| hardening.captcha.readTimeout | readTimeout | true |
| hardening.captcha.readTimeout | readTimeout | 'aaa' |
| hardening.captcha.readTimeout | readTimeout | loremIpsum |
| tracciatoCsv | tracciatoCsv | null |
| tracciatoCsv.tipo | tipo | null |
| tracciatoCsv.tipo | tipo | "aaa" |
| tracciatoCsv.intestazione | intestazione | null |
| tracciatoCsv.richiesta | richiesta | null |
| tracciatoCsv.risposta | risposta | null |
| mailBatch | mailBatch | "aaaa" |
| mailBatch.abilitato | abilitato | "aaaa" |
| mailBatch.abilitato | abilitato | null |
| mailBatch.mailserver | mailserver | 1 |
| mailBatch.mailserver | mailserver | "a" |
| mailBatch.mailserver.host | host | loremIpsum |
| mailBatch.mailserver.host | host | 'true ciao' |
| mailBatch.mailserver.host | host | null |
| mailBatch.mailserver.port | port | null |
| mailBatch.mailserver.port | port | "aaa" |
| mailBatch.mailserver.username | username | null |
| mailBatch.mailserver.username | username | loremIpsum |
| mailBatch.mailserver.password | password | null |
| mailBatch.mailserver.password | password | loremIpsum |
| mailBatch.mailserver.from | from | null |
| mailBatch.mailserver.from | from | loremIpsum |
| mailBatch.mailserver.readTimeout | readTimeout | null |
| mailBatch.mailserver.readTimeout | readTimeout | "aaa" |
| mailBatch.mailserver.connectTimeout | connectTimeout | null |
| mailBatch.mailserver.connectTimeout | connectTimeout | "aaa" |
| mailPromemoria | mailPromemoria | null |
| mailPromemoria.tipo | tipo | null |
| mailPromemoria.tipo | tipo | "aaa" |
| mailPromemoria.oggetto | oggetto | null |
| mailPromemoria.messaggio | messaggio | null |
| mailPromemoria.allegaPdf | allegaPdf | null |
| mailPromemoria.allegaPdf | allegaPdf | "aaa" |
| mailRicevuta | mailRicevuta | null |
| mailRicevuta.tipo | tipo | null |
| mailRicevuta.tipo | tipo | "aaa" |
| mailRicevuta.oggetto | oggetto | null |
| mailRicevuta.messaggio | messaggio | null |
| mailRicevuta.allegaPdf | allegaPdf | null |
| mailRicevuta.allegaPdf | allegaPdf | "aaa" |
| appIO | appIO | null |
| appIO.abilitato | abilitato | "aaaa" |
| appIO.abilitato | abilitato | null |
| appIO.url | url | null |
| appIO.message | message | null |
| appIO.message.tipo | tipo | null |
| appIO.message.tipo | tipo | "aaa" |
| appIO.message.subject | subject | null |
| appIO.message.body | body | null |
| appIO.message.timeToLive | timeToLive | 3599 |
| appIO.message.timeToLive | timeToLive | 604801 |



