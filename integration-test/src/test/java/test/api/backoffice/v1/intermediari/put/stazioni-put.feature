Feature: Validazione sintattica stazioni

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioBasicAuth.json')
* def stazione = read('classpath:test/api/backoffice/v1/intermediari/put/msg/stazione.json')
* def idStazione = idIntermediario + '_01'

Scenario: Configurazione stazione

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione
And headers basicAutenticationHeader
And request stazione
When method put
Then assert responseStatus == 200 || responseStatus == 201

Given url backofficeBaseurl
And path 'intermediari', idIntermediario, 'stazioni', idStazione
And headers basicAutenticationHeader
When method get
Then status 200
And match response == 
"""
{
	idStazione : '#(idStazione)',
	password : "GovPayTest",
	abilitato : true,
	domini : '#ignore'
}
"""
