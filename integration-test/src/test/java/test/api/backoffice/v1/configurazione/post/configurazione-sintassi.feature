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

Scenario Outline: <field> non valida

* set configurazione_generale.<path> = <value>

Given url backofficeBaseurl
And path 'configurazione'
And headers basicAutenticationHeader
And request configurazione_generale
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <field>

Examples:
| path | field | value | 
| interfacce.apiEnte.letture.log | log | 'xxxx' |
| interfacce.apiEnte.letture.dump | dump | 'xxxx' |
| interfacce.apiEnte.letture | letture | null |
| interfacce.apiEnte.scritture.log | log | 'xxxx' |
| interfacce.apiEnte.scritture.dump | dump | 'xxxx' |
| interfacce.apiEnte.scritture | scritture | null |

| interfacce.apiPagamento.letture.log | log | 'xxxx' |
| interfacce.apiPagamento.letture.dump | dump | 'xxxx' |
| interfacce.apiPagamento.letture | letture | null |
| interfacce.apiPagamento.scritture.log | log | 'xxxx' |
| interfacce.apiPagamento.scritture.dump | dump | 'xxxx' |
| interfacce.apiPagamento.scritture | scritture | null |

| interfacce.apiRagioneria.letture.log | log | 'xxxx' |
| interfacce.apiRagioneria.letture.dump | dump | 'xxxx' |
| interfacce.apiRagioneria.letture | letture | null |
| interfacce.apiRagioneria.scritture.log | log | 'xxxx' |
| interfacce.apiRagioneria.scritture.dump | dump | 'xxxx' |
| interfacce.apiRagioneria.scritture | scritture | null |

| interfacce.apiBackoffice.letture.log | log | 'xxxx' |
| interfacce.apiBackoffice.letture.dump | dump | 'xxxx' |
| interfacce.apiBackoffice.letture | letture | null |
| interfacce.apiBackoffice.scritture.log | log | 'xxxx' |
| interfacce.apiBackoffice.scritture.dump | dump | 'xxxx' |
| interfacce.apiBackoffice.scritture | scritture | null |

| interfacce.apiPagoPA.letture.log | log | 'xxxx' |
| interfacce.apiPagoPA.letture.dump | dump | 'xxxx' |
| interfacce.apiPagoPA.letture | letture | null |
| interfacce.apiPagoPA.scritture.log | log | 'xxxx' |
| interfacce.apiPagoPA.scritture.dump | dump | 'xxxx' |
| interfacce.apiPagoPA.scritture | scritture | null |

| interfacce.apiPendenze.letture.log | log | 'xxxx' |
| interfacce.apiPendenze.letture.dump | dump | 'xxxx' |
| interfacce.apiPendenze.letture | letture | null |
| interfacce.apiPendenze.scritture.log | log | 'xxxx' |
| interfacce.apiPendenze.scritture.dump | dump | 'xxxx' |
| interfacce.apiPendenze.scritture | scritture | null |

| interfacce.prova | prova | 'xxxx' |