.. _integrazione_autorizzazione:

Modalità di Autorizzazione
==========================

A valle del processo di autenticazione, quindi dopo che è stato identificato l'utente che ha invocato una API, GovPay valuta i criteri di autorizzazione per stabilire quali operazioni sono consentite. La tabella che segue descrive le politiche generali di autorizzazione che vengono applicate.

.. csv-table:: Modalità di Autorizzazione
  :header: "API", "Tipi di Autenticazione", "Operazioni Consentite"
  :widths: auto

  "Backoffice", "Tutte; *public* solo per le risorse ``/info``, ``/sonde`` e ``/tracciatiNotificaPagamenti``", "

    - Il principal individuato deve essere associato ad un Operatore o Applicazione registrato in GovPay a cui sono assegnati uno o più ruoli che determinano le operazioni autorizzate limitatamente ad un insieme di Enti Creditori e Tipologie di Pendenza."
  "Pendenze", "Tutte tranne *public*", "

    - Se il principal individua una applicazione autorizzata all'uso dell'API, questa può inserire nuove pendenze delle Tipologie e per gli Enti creditori autorizzati, mentre la consultazione e l'aggiornamento è limitata a quelle da lei create."
  "Ragioneria", "Tutte tranne *public*", "

    - Se il principal individua un'applicazione autorizzata all'uso dell'API, questa può creare nuove riconciliazioni e consultare le rendicontazioni, le riscossioni e le ricevute per gli Enti creditori autorizzati, mentre la consultazione delle riconciliazioni di pagamento è limitata a quelle da lei create."
  "pagoPA", "*ssl* (default); attivabili *basic*, *ldap*, *ssl-header*, *header*, *api-key*, *oauth2*. Sul percorso ``/internal``: *basic* (default)", "

    - Il principal deve corrispondere a quello indicato nel connettore pagoPA configurato nell'intermediario indicato.
    - Il percorso ``/internal`` espone gli stessi servizi ed è usato da govpay-rt-batch per consegnare con ``paSendRTV2`` le ricevute recuperate: il batch si autentica in *basic* con un'utenza registrata in GovPay."
  "Portale (govpay-portal-api)", "Autenticazione del cittadino (SPID/CIE) demandata a un reverse proxy; accesso anonimo per le operazioni pubbliche", "

    - Le funzioni rivolte ai cittadini (consultazione delle proprie pendenze, pagamento spontaneo, avvisi e ricevute) sono realizzate dal componente govpay-portal-api, che le descrive nel proprio `README <https://github.com/link-it/govpay-portal-api>`__."

Autorizzazione Applicazioni
---------------------------

Una volta determinate le modalità di autenticazione da adottare, e preso visione del partizionamento dei criteri di autorizzazione poc'anzi descritti, per consentire l'accesso ad un applicativo per invocare una determinata API è necessario procedere alla configurazione sul cruscotto di gestione. I passi seguenti descrivono un esempio nel caso delle API Pendenze:

1. Censire una nuova applicazione (vedi :ref:`govpay_configurazione_applicazioni`) assegnando il principal adeguato alla modalità di autenticazione (come il subject del certificato nel caso ssl o la username nel caso basic).
2. Autorizzare l'applicazione alle API Pendenze per gli Enti e i Tipi Pendenza desiderati.
3. Per un immediato riscontro della configurazione effettuata procedere con il reset della cache (Manutenzione > Resetta la cache).

Protezione delle API pubbliche
------------------------------

Le API pubbliche del portale dei pagamenti, esposte da govpay-portal-api, integrano alcune protezioni per mitigarne l'uso improprio.
La configurazione è gestita da GovPay: abilitando la funzione nella console (*Impostazioni > Protezione API pubbliche*, vedi :ref:`utente_impostazioni`), si attivano le seguenti funzioni:

    - **google reCAPTCHA**: le operazioni ``POST /pendenze/{idDominio}/{idTipoPendenza}`` (pagamento spontaneo) e ``GET /pendenze/{idDominio}/{numeroAvviso}/avviso`` richieste senza autenticazione vengono autorizzate se superati i controlli previsti dal reCAPTCHA v2 o v3
    - **hardening delle url**: per la ``GET /pendenze/{idDominio}/{numeroAvviso}/avviso``, in alternativa al reCAPTCHA, è possibile passare il parametro UUID della pendenza in query string, utile per la predisposizione di link diretti alle pagine di pagamento.

Per i dettagli si rimanda al `README di govpay-portal-api <https://github.com/link-it/govpay-portal-api>`__.
