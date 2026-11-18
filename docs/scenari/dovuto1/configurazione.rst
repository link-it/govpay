.. _govpay_scenari_dovuto1_configurazione:

Configurazione
--------------

Oltre alla configurazione di base di intermediario e stazione (:ref:`govpay_configurazione_intermediari`),
dell'ente creditore (:ref:`govpay_configurazione_enti`) e dell'applicazione gestionale autorizzata
alle *API Pendenze* (:ref:`govpay_configurazione_applicazioni`), procedere con le seguenti configurazioni
per predisporre l'ambiente necessario alla realizzazione dello scenario descritto:

1. **Configurazione del tipo pendenza**
   Deve essere censita una tipologia di pendenza che negli esempi è stata registrata con codice identificativo *SANZIONE*.
   Per maggiori dettagli sugli altri parametri di configurazione si faccia riferimento alla sezione :ref:`govpay_configurazione_tipipendenze`.

2. **Configurazione dell'ente creditore**
   Il tipo pendenza deve essere associato all'ente creditore.
   Per i dettagli di configurazione si consulti la sezione :ref:`govpay_configurazione_enti`.

3. **Installazione di govpay-portal-api**
   Il componente va installato e configurato come descritto nel suo
   `README <https://github.com/link-it/govpay-portal-api>`__ (vedi anche :ref:`inst_componenti_aggiuntivi`).
   Per le funzioni riservate al cittadino, come la consultazione della posizione debitoria,
   il componente va pubblicato dietro un reverse proxy che autentica l'utente con SPID o CIE
   e inoltra il codice fiscale nell'header ``X-SPID-FISCALNUMBER``.

4. **Abilitazione al Checkout di pagoPA**
   L'avvio del pagamento tramite Checkout non richiede configurazioni in GovPay, ma le abilitazioni
   previste da pagoPA per l'ente (ad esempio la chiave di sottoscrizione delle API Checkout).
