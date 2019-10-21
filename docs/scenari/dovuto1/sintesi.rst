
Sintesi
-------
Questo scenario riguarda il caso delle pendenze che hanno origine nell'ambito dell'ente creditore, il quale richiede al debitore il relativo pagamento.

Lo scenario si articola complessivamente nelle seguenti fasi:

1. L'amministratore di Govpay effettua le configurazioni necessarie al fine di abilitare l'accesso al canale pagoPA per il pagamento delle specifiche pendenze.

2. Il responsabile tecnico del portale dei pagamenti dell'ente effettua l'integrazione con le interfacce applicative di Govpay al fine di abilitare il flusso di pagamento.

3. L'utente debitore utilizza gli strumenti offerti dal Portale dei Pagamenti dell’Ente per giungere alla formalizzazione di una
   richiesta di pagamento.

4. L'utente debitore segue il flusso fino all'esecuzione del versamento sul PSP che ha scelto con il successivo ritorno sul portale dell'ente per la visualizzazione dell'esito e della ricevuta di pagamento.

Al fine di realizzare lo scenario sopra descritto è necessario procedere su due distinti processi che sono:

- La configurazione del canale di intermediazione delle tipologie di tributo e delle applicazioni autorizzate. Queste operazioni vengono effettuate tramite il cruscotto (per il relativo manuale consultare la sezione :ref:`govpay_configurazione`).

- L'impiego delle API di Govpay, da parte dei responsabili tecnici delle applicazioni di pagamento per:

    * l'integrazione dei sistemi gestionali nelle fasi di gestione delle pendenze e riconciliazione dei pagamenti

    * l'integrazione del portale dei pagamenti nelle fasi di selezione delle pendenze ed esecuzione del pagamento.
