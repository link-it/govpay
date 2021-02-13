Feature: Creazione Tracciato Secim

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-secim.json')
* def configurazioneMailServerLocale = 
"""
{
	"host": "smtp.link.it",
	"port": "25",
	"username": "govcloud",
	"password": "G65trw%$3we",
	"from": "govcloud@link.it",
	"readTimeout": 180000,
	"connectionTimeout": 20000
}
"""
* set dominio.servizioSecim.emailServer = configurazioneMailServerLocale
* set dominio.servizioSecim.emailIndirizzo = "pintori@link.it"
* set dominio.servizioSecim.versioneCsv = "7.0"


Scenario: Configurazione Dominio per spedizione tracciati secim via email e invocazione delle operazioni di creazione e spedizione tracciato

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/govpay-op-elaborazione-tracciati-notifica-pagamenti.feature')

# * call sleep(30000)

* call read('classpath:utils/govpay-op-spedizione-tracciati-notifica-pagamenti.feature')

