Feature: Validazione sintattica entrate

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def tipoPendenza = 
"""
{
  "descrizione": "Sanzione codice della strada",
  "tipo": "dovuta",
  "codificaIUV": "030",
  "pagaTerzi": true
}
"""          
          
Scenario Outline: Sintassi errata nel campo (<field>)

* set tipoPendenza.<fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'tipiPendenza', 'Multa'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| descrizione | descrizione | null | 'descrizione' | 
| descrizione | descrizione | loremIpsum | 'descrizione' | 
| tipo | tipo | null | 'tipo' |
| tipo | tipo | 'XXXX' | 'tipo' |
| codificaIUV | codificaIUV | '' | 'codificaIUV' |
| codificaIUV | codificaIUV | 'aaa' | 'codificaIUV' |
| codificaIUV | codificaIUV | '00000' | 'codificaIUV' |
| pagaTerzi | pagaTerzi | '' | 'pagaTerzi' |
| pagaTerzi | pagaTerzi | 'si' | 'pagaTerzi' |


Scenario: Sintassi errata nel campo (idTipoEntrata)

Given url backofficeBaseurl
And path 'tipiPendenza', ' *&'
And headers basicAutenticationHeader
And request tipoPendenza
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
