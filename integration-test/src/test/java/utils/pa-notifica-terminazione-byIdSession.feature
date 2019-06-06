Feature: Notifica terminazione RPT

Background: 

* configure retry = { count: 30, interval: 1000 }

Scenario: 

Given url ente_api_url
And path 'notificaTerminazioneByIdSession', idSession
And retry until responseStatus == 200 
When method get

* def notificaTerminazione = response
* def rptNotificaTerminazione = response.rpt
* def rtNotificaTerminazione = response.rt
