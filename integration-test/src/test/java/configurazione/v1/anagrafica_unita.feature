Feature: Censimento unita operative

Background:

* def unita = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')
* def unita2 = read('classpath:test/api/backoffice/v1/domini/put/msg/unita.json')
* def idUnitaOperativa = '12345678901_01'
* def idUnitaOperativa2 = '12345678901_02'

Scenario: Aggiunta di due unita operative

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201

* set unita2.ragioneSociale = 'Ufficio tre'

Given url backofficeBaseurl
And path 'domini', idDominio, 'unitaOperative', idUnitaOperativa2
And headers basicAutenticationHeader
And request unita2
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'domini', idDominio_2, 'unitaOperative', idUnitaOperativa
And headers basicAutenticationHeader
And request unita
When method put
Then assert responseStatus == 200 || responseStatus == 201