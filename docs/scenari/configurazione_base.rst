.. _govpay_scenari_configurazione_base:

Configurazione di base
----------------------

La configurazione *tipo* di GovPay prevede il censimento delle delle seguenti entità:

1. Intermediario e Stazione: il modello pagoPA prevede che ogni ente creditore
   colloqui con il Nodo dei Pagamenti SPC tramite un intermediario o partner tecnologico che,
   nel caso di adesione diretta, coincide con lo stesso ente creditore.
   Queste entità devono essere censita questa entità e la sua stazione.
   La descrizione di dettaglio di queste interfacce di configurazione sono consultabili nella 
   sezione :ref:`govpay_configurazione_intermediari`.

2. Ente creditore: l'ente creditore viene configurato associandolo
   alla stazione che intermedia il colloquio verso pagoPA. Oltre all'anagrafica, deve aver
   censito almeno un iban di accredito su cui saranno riversati gli importi.
   Per maggiori informazioni consultare la sezione :ref:`govpay_configurazione_enti`.

3. Applicazione gestionale delle pendenze: viene censita un'applicazione
   abilitata al caricamento delle pendenze nell'archivio dei pagamenti in attesa di GovPay
   per l'Ente creditore. 
   L'applicazione quindi deve avere almeno questa configurazione:
   
   - Autorizzata ad operare per l'Ente creditore
   
   - Autorizzata ad operare per qualsiasi tipologia di pendenza
   
   - Autorizzata ad invocare le API Pendenze. 
   
   Per i dettagli sulla configurazione delle applicazioni si consulti la sezione :ref:`govpay_configurazione_applicazioni`.

4. Applicazione portale di pagamento: nell'architettura di pagamento `tipo` il portale di pagamento 
   è un'applicazione distinta dal gestionale pendenze. Vale quanto detto al punto precedente con
   le seguenti particolarità:

   - Autorizzata ad operare per l'Ente creditore
   
   - Autorizzata ad operare per qualsiasi tipologia di pendenza
   
   - Autorizzata ad invocare le API Pagamento.    

5. Applicazione di contabilità: questa applicazione, integrata con la tesoreria, dispone delle
   informazioni relative ai movimenti di cassa dei conti di accredito. 
   Con queste informazioni interagisce con GovPay per riconciliare i moviemnti di entrata con i pagamenti
   operati su pagoPA. Pertanto deve rispettare almeno questa configurazione:

   - Autorizzata ad operare per l'Ente creditore
   
   - Autorizzata ad operare per qualsiasi tipologia di pendenza
   
   - Autorizzata ad invocare le API Ragioneria.   
 




