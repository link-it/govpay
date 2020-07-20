.. _integrazione_autorizzazione:

Modalità di Autorizzazione
==========================

A valle del processo di autenticazione, quindi dopo che è stato identificato l'utente che invocato una API, GovPay valuta i criteri di autorizzazione per stabilire quali operazioni sono consentite. La tabella che segue descrive le poliiche generali di autorizzazione che vengono applicate.

.. csv-table:: Modalità di Autorizzazione
  :header: "API", "Tipi di Autenticazione", "Operazioni Consentite"
  :widths: auto

  "Backoffice", "Tutte tranne *public*", "

    - Il principal individuato deve essere associato ad un Operatore o Applicazione registrato in GovPay a cui sono assegnati uno o più ruoli che determinano le operazioni autorizzate limitatamente ad un insieme di Enti Creditori e Tipologie di Pendenza."
  "Pendenze", "Tutte tranne *public*", "

    - Se il principal individua una applicazione autorizzata all'uso dell'API, questa può inserire nuove pendenze delle Tipologie e per gli Enti creditori autorizzati, mentre la consultazione e l'aggiornamento è limitata a quelle da lei create."
  "Pagamento", "Tutte", "
       
    - Se il principal individua una applicazione autorizzata all'uso dell'API, questa può avviare transazioni di pagamento e consultare le pendenze delle Tipologie di Pendenza per gli Enti creditori autorizzati, mentre la consultazione delle transazioni di pagamento è limitata a quelle da lei create. 
    - Se il principal individua un cittadino, questi può vedere le pendenze e pagamenti di cui risulta pagatore o versante e avviare transazioni di pagamento per proprie pendenze, avvisi o spontanei.
    - Se l'accesso è anonimo, l'utente può avviare pagamenti di avvisi o spontanei e consultare pagamenti associati alla sessione d'uso."
  "Ragioneria", "Tutte tranne *public*", "

    - Se il principal individua un'applicazione autorizzata all'uso dell'API, questa può creare nuove riconciliazioni e consultare le rendicontazioni, le riscossioni e le transazioni per gli Enti creditori autorizzati, mentre la consultazione delle riconciliazioni di pagamento è limitata a quelle da lei create."
  "pagoPA", "*ssl* o *basic*", "

    - Il principal deve corrispondere a quello indicato nel connettore pagoPA configurato nell'intermediario indicato."

Autorizzazione Applicazioni
---------------------------

Una volta determinate le modalità di autenticazione da adottare, e preso visione del partizionamento dei criteri di autorizzazione poc'anzi descritti, per consentire l'accesso ad un applicativo per invocare una determinata API è necessario procedere alla configurazione sul cruscotto di gestione. I passi seguenti descrivono un esempio nel caso delle API di Pagamento:

1. Censire una nuova applicazione (vedi :ref:`govpay_configurazione_applicazioni`) assegnando il principal adeguato alla modalità di autenticazione (come il subject del certificato nel caso ssl o la username nel caso basic).
2. Autorizzare l'applicazione alle API Pagamento per gli Enti e i Tipi Pendenza desiderati.
3. Per un immediato riscontro della configurazione effettuata procedere con il reset della cache (Manutenzione > Resetta la cache).

Protezione delle API pubbliche
------------------------------

GovPay integra alcune protezioni per le API di pagamento esposte ai cittadini per mitigarne l'uso improprio.
Abilitando la relativa funzione nella pagina delle impostazioni, si attivano le seguenti funzioni:

    - **google reCAPTCHA**: le operazioni POST /pagamenti e GET /avvisi vengono autorizzate se superati i controlli previste dal reCAPTCHA v2 o v3
    - **hardening delle url**: per la GET /avvisi, in alternativa al reCAPTCHA, è possibile passare il parametro UUID in query string, utile per la predisposizione di link diretti alle pagine di pagamento.
