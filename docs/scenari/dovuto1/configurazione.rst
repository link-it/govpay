.. _govpay_scenari_dovuto1_configurazione:

Configurazione
--------------



L'esecuzione dello scenario, illustrata nella sezione precedente, richiede che siano stati effettuati in precedenza alcuni passi di configurazione su GovPay.

La fase di configurazione, normalmente eseguita dalla figura che amministra GovPay, è relativa all'inserimento di tutte le informazioni necessarie per la gestione dei pagamenti e l'abilitazione del canale di intermediazione verso pagoPA.

Le operazioni di configurazione possono essere effettuate sia tramite le *API Backoffice* che utilizzando il cruscotto *GovPayConsole* con le credenziali di un utente autorizzato per l'operazione di scrittura sulle aree di anagrafica (applicazioni, enti, operatori e pagoPA). Per i dettagli di utilizzo del cruscotto per le operazioni di configurazione si faccia riferimento alla sezione :ref:`govpay_configurazione`.

I passi di configurazione necessari sono i seguenti:

1. **Configurazione dell'Intermediario/Stazione**: le informazioni da fornire per la configurazione dell'intermediario sono quelle ottenute al termine del processo di accreditamento a pagoPA. In questo contesto dovranno quindi essere indicati i dati identificativi dell'intermediario utilizzato, i riferimenti per l'accesso ai servizi pagoPA, il dettaglio della stazione con relativa password. La descrizione di dettaglio di queste interfacce di configurazione sono consultabili nella sezione :ref:`govpay_configurazione_intermediari`.

.. note::
    Nell'ambiente demo di GovPay è presente a scopo dimostrativo l'entità *Soggetto Intermediario* con la relativa stazione.


2. **Registrazione dei Tipi Pendenza**: devono essere censiti i tipi di pendenza che saranno gestiti per l'ente creditore. Ciascun tipo pendenza individua un ben preciso capitolo d'entrata per l'ente, con le relative informazioni per il pagamento e la rendicontazione. Nello scenario corrente si deve utilizzare la configurazione di tipi pendenza aventi tipologia "Dovuto". Per i dettagli sullo svolgimento di questo passaggio si faccia riferimento alla sezione :ref:`govpay_configurazione_tipipendenze`.

.. note::
    Ai fini dello scenario illustrato, supponiamo l'esistenza di un tipo pendenza *SANZIONE* che deve avere tipo *dovuto*.

3. **Configurazione dell'ente creditore**: l'anagrafica dell'ente aderente deve essere creata in associazione all'intermediario censito in precedenza. Oltre ai dati anagrafici dovranno essere fornite informazioni derivanti dal processo di accreditamento a pagoPA, come il codice di segregazione o il CBILL. Nel contesto dell'anagrafica dell'ente dovranno essere indicate le pendenze che lo riguardano e le singole voci d'entrata con gli iban di accredito. Per i dettagli di configurazione si consulti la sezione :ref:`govpay_configurazione_enti`.

.. note::
    Supponiamo di avere registrato l'ente *Comune Dimostrativo* associato all'intermediario/stazione precedentemente configurato e al tipo pendenza *SANZIONE*.

4. **Configurazione dell'applicazione gestionale**: si devono censire le applicazioni che corrispondono ai sistemi per la gestione dei pagamenti nel dominio dell'ente creditore. Tali applicativi sono responsabili dell'inserimento, nel registro di GovPay, delle pendenze corrispondenti ai dovuti che dovranno essere pagati dai debitori. Per i dettagli sulla configurazione delle applicazioni si consulti la sezione :ref:`govpay_configurazione_applicazioni`.

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
