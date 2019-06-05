Feature: Notifica attivazione RPT

Background: 

* configure retry = { count: 10, interval: 1000 }

Scenario: 

Given url ente_api_url
And path 'notificaAttivazioneByIdSession', idSession
And retry until responseStatus == 200 
When method get

* def rptNotificaAttivazione = response.rpt
