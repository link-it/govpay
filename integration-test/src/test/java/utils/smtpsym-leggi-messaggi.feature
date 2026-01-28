Feature: Eliminazione messaggi dal simulatore smtp

Background:

* def smtpSymBaseurl = smtpsym_url

Scenario: 

Given url smtpSymBaseurl
And path 'v2', 'messages' 
When method get
Then assert responseStatus == 200