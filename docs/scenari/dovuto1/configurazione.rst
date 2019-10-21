
Configurazione
--------------

La fase di configurazione, eseguita dall'amministratore di Govpay, riguarda l'inserimento di tutte le informazioni necessarie per abilitare il canale di intermediazione verso pagoPA. Le operazioni di configurazione vengono effettuate utilizzando il cruscotto *govpayConsole* con le credenziali di un utente autorizzato per l'operazione di scrittura sulle aree di anagrafica (applicazioni, enti, operatori e pagoPA). Per i dettagli di utilizzo del cruscotto per le operazioni di configurazione si faccia riferimento alla sezione :ref:`govpay_configurazione`.

I passi necessari per configurare l'indermediazione sono i seguenti:

1. **Configurazione dell'Intermediario/Stazione**: le informazioni da fornire per la configurazione dell'intermediario sono quelle ottenute al termine del processo di accreditamento a pagoPA. In questo contesto dovranno quindi essere indicati i dati identificativi dell'intermediario utilizzato, i riferimenti per l'accesso ai servizi pagoPA, il dettaglio della stazione con relativa password. La descrizione di dettaglio di queste interfacce di configurazione sono consultabili nella sezione :ref:`govpay_configurazione_intermediari`.

2. **Registrazione dei Tipi Pendenza**: devono essere censiti i tipi di pendenza che saranno gestiti per l'ente creditore. Ciascun tipo pendenza individua un ben preciso capitolo d'entrata per l'ente, con le relative informazioni per il pagamento e la rendicontazione. Nello scenario corrente si deve utilizzare la configurazione di tipi pendenza aventi tipologia "Dovuto". Per i dettagli sullo svolgimento di questo passaggio si faccia riferimento alla sezione :ref:`govpay_configurazione_tipipendenze`.

3. **Configurazione dell'ente creditore**: l'anagrafica dell'ente aderente deve essere creata in associazione all'intermediario censito in precedenza. Oltre ai dati anagrafici dovranno essere fornite informazioni derivanti dal processo di accreditamento a pagoPA, come il codice di segregazione o il CBILL. Nel contesto dell'anagrafica dell'ente dovranno essere indicate le pendenze che lo riguardano e le singole voci d'entrata con gli iban di accredito. Per i dettagli di configurazione si consulti la sezione :ref:`govpay_configurazione_enti`.

4. **Configurazione dell'applicazione gestionale**: si devono censire le applicazioni che corrispondono ai sistemi di gestione dei pagamenti nel dominio dell'ente creditore. Tali applicativi sono responsabili dell'inserimento, nel registro di Govpay, delle pendenze corrispondenti ai dovuti che dovranno essere pagati dai debitori. Tali applicativi dovranno prevedere:

    - l'associazione con l'ente creditore creato in precedenza

    - la correlazione con i tipi pendenza creati in precedenza

    - l'abilitazione per l'accesso alle **API Pendenze**

    Per i dettagli sulla configurazione delle applicazioni si consulti la sezione :ref:`govpay_configurazione_applicazioni`.

5. **Configurazione dell'applicazione di pagamento (Portale)**: si devono censire le applicazioni utilizzate per l'esecuzione dei pagamenti, tipicamente il portale dei pagamenti dell'ente. Per quanto riguarda le modalità di configurazione vale quanto già detto al passo precedente con la differenza che in questo caso occorre autorizzare il portale per l'accesso alle **API Pagamenti**.