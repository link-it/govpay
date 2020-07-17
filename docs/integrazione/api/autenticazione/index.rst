.. _integrazione_autenticazione:

Modalità di Autenticazione
==========================

Le modalità di autenticazione supportate dalle API di GovPay sono quelle indicate nella seguente tabella.


.. table:: Modalità di Autenticazione
    :name: tabella_autenticazione
    :widths: auto

==========  ==========================================================  =================================================================   ===========
Modalità    Principal                                                   Descrizione                                                         AUTH_METHOD
==========  ==========================================================  =================================================================   ===========
basic       username                                                    username e password fornite tramite header Authorization            basic
form        username                                                    username e password fornite tramite form di autenticazione custom   form
ssl         subject del certificato                                     invocazione SSL con certificato client                              ssl
ssl-header  subject del certificato                                     invocazione con certificato codificato in header HTTP               sslheader
header      valore dell'header                                          principal comunicato in header HTTP                                 header
spid        codice fiscale o partita iva dell'utenza spid               proprietà dell'utenza SPID propagate in header HTTP                 spid
session     principal individuato in sede di creazione della sessione   autenticazione tramite sessione precedentemente creata              session
public      nessun principal                                            accesso anonimo                                                     public
==========  ==========================================================  =================================================================   ===========

Il valore AUTH_METHOD viene utilizzato per costruire la URL di invocazione delle API con la modalità di autenticazione prescelta. Lo schema per construire la URL di invocazione è il seguente:

BASE_URL + /rs/ + AUTH_METHOD

Esempio di URL di invocazione: *https://host-gp/govpay/frontend/api/pagamento/rs/basic/v2/pagamenti*

L'utilizzo delle modalità di autenticazione ssl e ssl-header prevedono che i certificati utilizzati vengano autorizzati nella configurazione di livello trasporto su GovPay.
La modalità session consente di riusare una sessione di autenticazione attiva, fornendo il relativo identificativo, evitando quindi di effettuare una nuova autenticazione.
Seguendo le impostazioni di default, le modalità di autenticazione attive saranno le sole **basic** e **ssl** per tutte le API, con la sola eccezione della API di Pagamento dove è prevista per default la sola autenticazione **ssl**.
E' possibile abilitare/disabilitare le modalità di autenticazione descritte nella tabella :ref:`tabella_autenticazione` seguendo il processo di configurazione descritto in :ref:`inst_dispiegamento_auth`.


