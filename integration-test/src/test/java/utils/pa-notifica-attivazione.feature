Feature: Notifica attivazione RPT

Background: 

* configure retry = { count: 30, interval: 1000 }

Scenario: 

Given url ente_api_url
And path 'notificaAttivazione', idDominio, iuv, ccp
And retry until responseStatus == 200 
When method get

* def rptNotificaTerminazione = response.rpt
