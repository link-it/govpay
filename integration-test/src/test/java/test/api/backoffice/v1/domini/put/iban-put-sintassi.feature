Feature: Validazione sintattica iban

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def iban = { postale:false, mybank:false, abilitato:true, bic:'#(bicAccredito)', descrizione:'#(ibanAccreditoDescrizione)' }
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set iban.<field> = <fieldValue>

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', ibanAccredito
And headers basicAutenticationHeader
And request iban
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains <fieldName>

Examples:
| field | fieldValue | fieldName | 
| postale | null | 'postale' |
| postale | '' | 'postale' |
| abilitato | null | 'abilitato' |
| abilitato | '' | 'abilitato' |
| bic | 'AAAAAA3OZZZ' | 'bic' |
| descrizione | loremIpsum | 'descrizione' |
| descrizione | '' | 'descrizione' |

Scenario: Iban non valido

Given url backofficeBaseurl
And path 'domini', idDominio, 'contiAccredito', '0088ABCDEFGHIJKLMNOPQRSTUVWXYZ01'
And headers basicAutenticationHeader
And request iban
When method put
Then status 400
And match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
And match response.dettaglio contains 'ibanAccredito'

