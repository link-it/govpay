Feature: Caricamento pendenza con documento

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

* def documento =
"""
{
	identificativo: "#(idPendenza + '_DOC')",
	descrizione: "#('Documento ('+ idPendenza +')')",
	rata: null,
	soglia: null
}
"""

* def soglia =
"""
{
	tipo: "ENTRO", 
	giorni: 5
}
"""

Scenario Outline: Caricamento pendenza con controllo del campo opzionale <field> 

* set pendenzaPut.documento = documento
* set pendenzaPut.documento.<field> = <value>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

* def checkValue = <value> != null ? <value> : '#notpresent'

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
When method get
Then status 200
And match response.documento.<field> == checkValue

Examples:
| field | value |
| rata | 1 |
| rata | null |
| soglia | { tipo: "ENTRO", giorni: 5 } |
| soglia | { tipo: "OLTRE", giorni: 5 } |
| soglia | null |

Scenario Outline: <field> non valida

* set pendenzaPut.documento = documento
* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| documento.descrizione | documento.descrizione | null | 'descrizione' |
| documento.descrizione | documento.descrizione | loremIpsum | 'descrizione' |
| documento.identificativo | documento.identificativo | null | 'identificativo' |
| documento.identificativo | documento.identificativo | loremIpsum | 'identificativo' |
| documento.rata | documento.rata | 'xxx' | 'rata' |
| documento.rata | documento.rata | 0 | 'rata' |

Scenario Outline: <field> non valida

* set pendenzaPut.documento = documento
* set pendenzaPut.documento.soglia = soglia

* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers gpAdminBasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| documento.soglia | documento.soglia.tipo | null | 'tipo' |
| documento.soglia | documento.soglia.tipo | 'xxx' | 'tipo' |
| documento.soglia | documento.soglia.giorni | null | 'giorni' |
| documento.soglia | documento.soglia.giorni | 0 | 'giorni' |
| documento.soglia | documento.soglia.giorni | 'xxx' | 'giorni' |


