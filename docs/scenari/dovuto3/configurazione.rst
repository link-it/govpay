.. _govpay_scenari_dovuto3_configurazione:

Configurazione
--------------

Oltre alla configurazione di base di intermediario e stazione (:ref:`govpay_configurazione_intermediari`),
dell'ente creditore (:ref:`govpay_configurazione_enti`) e dell'applicazione gestionale autorizzata
alle *API Pendenze* (:ref:`govpay_configurazione_applicazioni`), sono necessarie le
seguenti configurazioni per predisporre l'ambiente necessario alla
realizzazione dello scenario descritto:

1. **Configurazione del tipo pendenza** 
   Deve essere censita una tipologia di pendenza di tipo *Dovuto*, che negli esempi è stata registrata con codice identificativo *TICKET*.
   Per maggiori dettagli sugli altri parametri di configurazione si faccia riferimento alla sezione :ref:`govpay_configurazione_tipipendenze`.

2. **Configurazione dell'ente creditore** 
   Il tipo pendenza deve essere associato all'ente creditore. 
   Per i dettagli di configurazione si consulti la sezione :ref:`govpay_configurazione_enti`.

3. **Configurazione del connettore dell'applicazione gestionale**
   Nella sezione *API Integrazione* dell'applicazione gestionale vanno indicati l'URL delle API Ente Creditore
   esposte dal gestionale e la *Versione API* da invocare (negli esempi ``REST_2``), così che GovPay possa
   invocare le operazioni di verifica della pendenza e di notifica del pagamento.
   Per i dettagli di configurazione si consulti la sezione :ref:`govpay_configurazione_applicazioni`.
