.. _integrazione_autenticazione:

Modalità di Autenticazione
==========================

Le modalità di autenticazione supportate dalle API di GovPay sono quelle indicate nella seguente tabella. Non tutte le modalità sono disponibili per tutte le API: la colonna *API* indica dove ciascuna è prevista dalla configurazione di Spring Security.

.. list-table:: Modalità di Autenticazione
    :name: tabella_autenticazione
    :widths: 12 20 30 12 26
    :header-rows: 1

    * - Modalità
      - Principal
      - Descrizione
      - AUTH_METHOD
      - API
    * - basic
      - username
      - username e password fornite tramite header Authorization, verificate sulle utenze registrate in GovPay
      - basic
      - Pendenze, Ragioneria, Backoffice
    * - basic (LDAP)
      - username
      - username e password fornite tramite header Authorization, verificate su un server LDAP
      - basic
      - Pendenze, Ragioneria, Backoffice
    * - form
      - username
      - username e password fornite tramite form di autenticazione custom
      - form
      - Backoffice
    * - ssl
      - subject del certificato
      - invocazione SSL con certificato client
      - ssl
      - Pendenze, Ragioneria, Backoffice
    * - ssl-header
      - subject del certificato
      - invocazione con certificato codificato in header HTTP
      - sslheader
      - Pendenze, Ragioneria, Backoffice
    * - header
      - valore dell'header
      - principal comunicato in header HTTP
      - header
      - Pendenze, Ragioneria, Backoffice
    * - spid
      - codice fiscale o partita iva dell'utenza spid
      - proprietà dell'utenza SPID propagate in header HTTP
      - spid
      - Backoffice
    * - session
      - principal individuato in sede di creazione della sessione
      - autenticazione tramite sessione precedentemente creata
      - session
      - Backoffice
    * - api-key
      - valore dell'header
      - API-Id e API-Key forniti tramite header HTTP
      - apikey
      - Pendenze, Ragioneria, Backoffice
    * - oauth2
      - claim del token JWT indicato in configurazione
      - autenticazione tramite token Bearer OAuth2 (JWT)
      - oauth2
      - Pendenze (solo v2), Ragioneria (v2 e v3), Backoffice (solo v1, flusso PKCE usato dalla console)
    * - public
      - nessun principal
      - accesso anonimo, limitato alle risorse ``/info``, ``/sonde`` e ``/tracciatiNotificaPagamenti``
      - public
      - Backoffice
    * - default
      - come ssl o basic
      - autenticazione ssl o basic sulle URL prive del segmento AUTH_METHOD (``BASE_URL + /rs/ + versione``)
      - (nessuno)
      - Pendenze, Ragioneria, Backoffice

Il valore AUTH_METHOD viene utilizzato per costruire la URL di invocazione delle API con la modalità di autenticazione prescelta. Lo schema per costruire la URL di invocazione è il seguente:

BASE_URL + /rs/ + AUTH_METHOD

Esempio di URL di invocazione: *https://govpay.example.org/govpay-api-pendenze/rs/basic/v2/pendenze*

Le API pagoPA, invocate dal Nodo dei Pagamenti, non prevedono il segmento ``/rs/AUTH_METHOD``: la modalità di autenticazione si applica a tutti i servizi esposti, ad eccezione del percorso ``/internal``, che ha una configurazione propria (vedi :ref:`integrazione_autorizzazione`).

L'utilizzo delle modalità di autenticazione ssl e ssl-header prevedono che i certificati utilizzati vengano autorizzati nella configurazione di livello trasporto su GovPay.
La modalità session consente di riusare una sessione di autenticazione attiva, fornendo il relativo identificativo, evitando quindi di effettuare una nuova autenticazione.

La modalità oauth2 valida il token JWT con la chiave pubblica pubblicata dal server di autorizzazione e ricava il principal da un claim del token. Si configura con i seguenti valori, impostati come proprietà di sistema della JVM (``-DOAUTH2_JWK_SET_URI=...``) o come variabili d'ambiente:

- ``OAUTH2_JWK_SET_URI``: URL del JWK Set del server di autorizzazione;
- ``OAUTH2_VALORE_CLAIM_ISS``: valore atteso del claim ``iss``;
- ``OAUTH2_VALORE_CLAIM_AUD``: valore atteso del claim ``aud``;
- ``OAUTH2_NOME_CLAIM_PRINCIPAL``: nome del claim da cui ricavare il principal;
- ``OAUTH2_LOGOUT_URI`` e ``OAUTH2_POST_LOGOUT_REDIRECT_URI``: solo per il flusso PKCE delle API Backoffice, URL di logout del server di autorizzazione e URL a cui tornare dopo il logout.

Se ``OAUTH2_VALORE_CLAIM_ISS`` o ``OAUTH2_VALORE_CLAIM_AUD`` sono valorizzate con una stringa vuota, la verifica del relativo claim non viene effettuata.

Seguendo le impostazioni di default, le modalità di autenticazione attive sono:

- **basic** e **ssl** per le API Pendenze e Ragioneria;
- **basic**, **form** e **ssl** per le API Backoffice;
- **ssl** per le API pagoPA, con l'autenticazione **basic** sul solo percorso ``/internal``.

È possibile abilitare/disabilitare le modalità di autenticazione descritte nella tabella :ref:`tabella_autenticazione` seguendo la modalità di deploy prevista in fase di installazione come descritto in :ref:`inst_dispiegamento_auth`.
