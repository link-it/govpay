Feature: Ricerca pagamenti

Background:

* callonce read('classpath:utils/workflow/modello1/v1/modello1-bunch-pagamenti-v2.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def dominio = read('classpath:test/api/backoffice/v1/domini/put/msg/dominio-connettore-mypivot.json')
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
* set dominio.servizioMyPivot.emailServer = configurazioneMailServerLocale
* set dominio.servizioMyPivot.emailIndirizzo = "pintori@link.it"


Scenario: Configurazione Dominio per spedizione tracciati mypivot via email e invocazione delle operazioni di creazione e spedizione tracciato

Given url backofficeBaseurl
And path 'domini', idDominio
And headers basicAutenticationHeader
And request dominio
When method put
Then assert responseStatus == 200 || responseStatus == 201

* call read('classpath:configurazione/v1/operazioni-resetCache.feature')

* call read('classpath:utils/govpay-op-elaborazione-tracciati-mypivot.feature')

# * call sleep(30000)

* call read('classpath:utils/govpay-op-spedizione-tracciati-mypivot.feature')

