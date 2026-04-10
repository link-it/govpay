@ignore
Feature: Operazioni Batch

# Utility per eseguire le operazioni batch disponibili in GovPay
#
# Operazioni disponibili:
# - acquisizioneRendicontazioni: Acquisizione flussi di rendicontazione
# - chiusuraRptScadute: Chiusura RPT scadute
# - gestionePromemoria: Gestione promemoria
# - spedizioneNotifiche: Spedizione notifiche
# - spedizioneNotificheAppIO: Spedizione notifiche App IO
# - spedizionePromemoria: Spedizione promemoria
# - resetCacheAnagrafica: Reset cache anagrafica
# - elaborazioneTracciatiPendenze: Elaborazione tracciati pendenze
# - elaborazioneTracciatiNotificaPagamenti: Elaborazione tracciati notifica pagamenti
# - spedizioneTracciatiNotificaPagamenti: Spedizione tracciati notifica pagamenti
# - elaborazioneRiconciliazioni: Elaborazione riconciliazioni
# - recuperoRT: Recupero RT
# - inviaPosizioniDebitorieAca: Invia posizioni debitorie ACA
# - inviaNotificheMaggioli: Invia notifiche Maggioli
#
# Uso:
# * call read('classpath:utils/batch-operations.feature@resetCacheAnagrafica')
# * call read('classpath:utils/batch-operations.feature@eseguiOperazione') { idOperazione: 'spedizioneNotifiche' }

Background:

* call read('classpath:utils/common-utils.feature')
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )


@listaOperazioni
Scenario: Lista delle operazioni disponibili

Given url govpay_operazioni_baseurl
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@eseguiOperazione
Scenario: Esegui operazione generica

Given url govpay_operazioni_baseurl
And path idOperazione
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@eseguiOperazioneForza
Scenario: Esegui operazione generica forzando l'esecuzione

Given url govpay_operazioni_baseurl
And path idOperazione
And param force = true
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@resetCacheAnagrafica
Scenario: Reset cache anagrafica

Given url govpay_operazioni_baseurl
And path 'resetCacheAnagrafica'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@acquisizioneRendicontazioni
Scenario: Acquisizione flussi di rendicontazione

Given url govpay_operazioni_baseurl
And path 'acquisizioneRendicontazioni'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@acquisizioneRendicontazioniForza
Scenario: Acquisizione flussi di rendicontazione (forza esecuzione)

Given url govpay_operazioni_baseurl
And path 'acquisizioneRendicontazioni'
And param force = true
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@chiusuraRptScadute
Scenario: Chiusura RPT scadute

Given url govpay_operazioni_baseurl
And path 'chiusuraRptScadute'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@gestionePromemoria
Scenario: Gestione promemoria

Given url govpay_operazioni_baseurl
And path 'gestionePromemoria'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@spedizioneNotifiche
Scenario: Spedizione notifiche

Given url govpay_operazioni_baseurl
And path 'spedizioneNotifiche'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@spedizioneNotificheAppIO
Scenario: Spedizione notifiche App IO

Given url govpay_operazioni_baseurl
And path 'spedizioneNotificheAppIO'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@spedizionePromemoria
Scenario: Spedizione promemoria

Given url govpay_operazioni_baseurl
And path 'spedizionePromemoria'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@elaborazioneTracciatiPendenze
Scenario: Elaborazione tracciati pendenze

Given url govpay_operazioni_baseurl
And path 'elaborazioneTracciatiPendenze'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@elaborazioneTracciatiNotificaPagamenti
Scenario: Elaborazione tracciati notifica pagamenti

Given url govpay_operazioni_baseurl
And path 'elaborazioneTracciatiNotificaPagamenti'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@spedizioneTracciatiNotificaPagamenti
Scenario: Spedizione tracciati notifica pagamenti

Given url govpay_operazioni_baseurl
And path 'spedizioneTracciatiNotificaPagamenti'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@elaborazioneRiconciliazioni
Scenario: Elaborazione riconciliazioni

Given url govpay_operazioni_baseurl
And path 'elaborazioneRiconciliazioni'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@recuperoRT
Scenario: Recupero RT

Given url govpay_operazioni_baseurl
And path 'recuperoRT'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@recuperoRTForza
Scenario: Recupero RT (forza esecuzione)

Given url govpay_operazioni_baseurl
And path 'recuperoRT'
And param force = true
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@inviaPosizioniDebitorieAca
Scenario: Invia posizioni debitorie ACA

Given url govpay_operazioni_baseurl
And path 'inviaPosizioniDebitorieAca'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@inviaPosizioniDebitorieAcaForza
Scenario: Invia posizioni debitorie ACA (forza esecuzione)

Given url govpay_operazioni_baseurl
And path 'inviaPosizioniDebitorieAca'
And param force = true
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@inviaNotificheMaggioli
Scenario: Invia notifiche Maggioli

Given url govpay_operazioni_baseurl
And path 'inviaNotificheMaggioli'
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201


@inviaNotificheMaggioliForza
Scenario: Invia notifiche Maggioli (forza esecuzione)

Given url govpay_operazioni_baseurl
And path 'inviaNotificheMaggioli'
And param force = true
And headers basicAutenticationHeader
When method get
Then assert responseStatus == 200 || responseStatus == 201

