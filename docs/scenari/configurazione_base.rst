.. _govpay_scenari_configurazione_base:

Configurazione di base
----------------------

La configurazione *tipo* di GovPay prevede il censimento delle delle seguenti entità:
- Un intermediario o partner tecnologico
- Un ente creditore (che in caso di adesione diretta coincide con l'intermediario)
- Un applicativo gestionale delle posizioni debitorie
- Un portale di pagamento ad uso dei cittadini
- Un applicativo di contabilità per la riconciliazione di tesoreria

I passi di configurazione necessari sono i seguenti:

1. **Intermediario/Stazione**: L'adesione a pagoPA prevede il censimento, tramite Portale delle Adesioni,
di una stazione associata ad un Intermediario o Partner Tecnologico. A valle di questa operazione di deteminano
le informazioni necessarie al censimento di un Intermediario e di una Stazione in GovPay.
La descrizione di dettaglio di queste interfacce di configurazione sono consultabili nella 
sezione :ref:`govpay_configurazione_intermediari`.

2. **Configurazione dell'ente creditore**: Tramite il Portale delle Adesioni, gli enti creditori vengono associati
associati alla stazione, indicando i conti di accredito dei pagamenti e determinandone il codice di segregazione. 
Le medesime informazioni saranno poi utilizzate in GovPay per censire gli Enti Creditori e la loro anagrafica, come 
descritto nella sezione :ref:`govpay_configurazione_enti`.

3. **Configurazione dell'applicazione gestionale**: si devono censire le applicazioni che corrispondono ai sistemi per la gestione dei pagamenti nel dominio dell'ente creditore. Tali applicativi sono responsabili dell'inserimento, nel registro di GovPay, delle pendenze corrispondenti ai dovuti che dovranno essere pagati dai debitori. Per i dettagli sulla configurazione delle applicazioni si consulti la sezione :ref:`govpay_configurazione_applicazioni`.

.. note::
    Ai fini dello scenario attuale si procede alla registrazione dell'applicazione *A2A-DEMO* prevedendo:

    - l'associazione con l'ente creditore *Comune Dimostrativo* creato in precedenza

    - la correlazione con il tipo pendenza *SANZIONE* creato in precedenza

    - l'abilitazione per l'accesso alle **API Pendenze**, necessarie per il caricamento delle pendenze


5. **Configurazione dell'applicazione di pagamento (Portale)**: si devono censire le applicazioni utilizzate per l'esecuzione dei pagamenti, tipicamente il portale dei pagamenti dell'ente. Vale quanto già detto al passo precedente

.. note::
    Ai fini dello scenario attuale si procede alla registrazione dell'applicazione *A2A-PORTALE* prevdendo:

    - l'associazione con l'ente creditore *Comune Dimostrativo* creato in precedenza

    - la correlazione con il tipo pendenza *SANZIONE* creato in precedenza

    - l'abilitazione per l'accesso alle **API Pagamenti**, necessarie per gestire il flusso di pagamento delle pendenze
 




