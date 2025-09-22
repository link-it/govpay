Feature: Eliminazione messaggi dal simulatore smtp

Background:

* def smtpSymBaseurl = smtpsym_url

Scenario: 

Given url smtpSymBaseurl
And path 'v1', 'messages' 
When method delete
Then assert responseStatus == 200