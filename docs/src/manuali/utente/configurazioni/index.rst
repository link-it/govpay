.. _utente_configurazioni:

Configurazioni
==============

La sezione di configurazione, visibile solo agli amministratori del
sistema, consente il censimento e manutenzione delle entità logiche
coinvolte nel processo di pagamento. 

.. figure:: ../_images/08Configurazioni.png
   :align: center

   Figura 1: Lista delle funzionalità di configurazione


-  *Intermediari*: rappresentano le entità “Intermediario” o “Partner Tecnologico” censiti presso il Nodo dei Pagamenti scelti in
   fase di adesione dagli Enti Creditore per l'accesso al sistema pagoPA.
-  *Domini*: corrispondono agli enti creditori aderenti al sistema pagoPA.
-  *Tipi Pendenza*: rappresentano le esigenze dell'ente creditore dalle quali
   scaturiscono le tipologie di pagamenti che possono essere gestiti dal
   sistema (tassa rifiuti, licenza di caccia, bollo auto, ...).
-  *Applicazioni*: rappresentano i portali di pagamento e i gestionali
   delle posizioni debitorie degli enti Creditori integrati con GovPay
   tramite gli appositi servizi.
-  *Operatori*: sono le utenze del cruscotto di gestione GovPay.
-  *Ruoli*: definizione dei ruoli, in termini di autorizzazioni
   consentite sulle entità dati, che saranno assegnati agli utenti del
   cruscotto.

.. note:: Nell'analisi delle funzionalità di configurazione, le immagini esemplificative mostrate mancheranno della
   sezione di sinistra (*Lista funzionalità*) al fine di permettere la concentrazione sulla sola parte importante, una volta
   che si sia selezionata la funzionalità, ovvero il suo dettaglio, posto a destra.

Intermediari
------------

Gli intermediari o partner tecnologici sono entità censite da AgID sul
circuito pagoPA al momento dell'adesione di un Ente Creditore. Per il
corretto funzionamento di GovPay, gli intermediari di interesse devono
essere censiti con le informazioni di corredo necessarie.

.. figure:: ../_images/09Intermediari.png
   :align: center

   Figura 2: Vista di dettaglio intermediari

Accedendo alla sezione corrispondente (*Configurazioni > Intermediari*), viene visualizzato l'elenco degli intermediari censiti
sul sistema. È possibile filtrare gli intermediari in relazione al loro stato, con una funzionalità di filtro disponibile sulla sinistra del box di dettaglio, come di seguito mostrato:

.. figure:: ../_images/10FiltroSuIntermediari.png
   :align: center

   Figura 3: Filtro su ricerca Intermediari


Nuovo Intermediario
~~~~~~~~~~~~~~~~~~~

Per inserire un nuovo intermediario è necessario premere l'apposito
pulsante, presente nella pagina di elenco in basso a destra, e compilare il form che viene
aperto:

.. figure:: ../_images/11CampiNuovoIntermediario.png
   :align: center

   Figura 4: Informazioni che definiscono un nuovo intermediario

Le informazioni contenute nel form sono le seguenti:

.. csv-table:: Titolo
  :header: "Campo", "Significato", "Note"
  :widths: 40,40,20
  
  "Denominazione", "Nome associato all'Intermediario o al Partner Tecnologico", "Obbligatorio"
  "Id Intermediario", "Identificativo dell'intermediario o Partner Tecnologico, fornito da AgID, corrisponde alla Partita IVA del soggetto", "Obbligatorio"
  "Principal", "identificativo (subject certificato o principal) corrispondente alle credenziali con cui Govpay riceve le chiamate in entrata da pagoPA", ""
  "Abilitato/Non Abilitato", "Stato del nuovo intermediario: indica se l'intermediario è usabile da GovPay per gestire nuovi pagamenti o se impedire nuove richieste.", ""
  "Servizio RPT", "Riferimenti utilizzati da Govpay per comunicare con il Nodo SPC: Endpoint per le chiamate in uscita verso il Nodo SPC", ""
  "Tipo Autenticazione", "Lista a discesa per selezionare il tipo di autenticazione adottata per le comunicazioni con il Nodo SPC. Si sceglie tra: Nessuna e HTTP-Basic. Nel caso si scelga una modalità di autenticazione, dovranno essere inserite i relativi dati di configurazione", ""  

Selezionando un intermediario dalla pagina che li elenca si accede alla
pagina di dettaglio.

Dettaglio Intermediario
~~~~~~~~~~~~~~~~~~~~~~~

La pagina di dettaglio di un intermediario mostra i singoli campi che lo
compongono unitamente all'elenco delle stazioni ad esso associate.
Infatti, al censimento di un Intermediario o Partner Tecnologico, AgID
assegna anche una o più Stazioni Tecnologiche che devono essere
registrate su GovPay.

Il pulsante di modifica presente nella pagina consente di aprire il form
per modificare le proprietà dell'intermediario.

Stazioni
~~~~~~~~

Nella sezione della pagina di dettaglio dell'intermediario dedicata alle
stazioni è possibile utilizzare il pulsante per crearne di nuove. Le
stazioni create vengono automaticamente associate all'intermediario ed è
inoltre necessario fornire i seguenti dati:

-  *IdStazione*: Identificativo della stazione. Obbligatorio, fornito da
   AgID.
-  *Password*: Chiave segreta. Obbligatoria, fornita da AgID.
-  *Abilitato*: indica se la stazione è usabile da GovPay per gestire
   nuovi pagamenti (abilitato) o se si vogliono impedire nuove richieste
   (disabilitato).

È possibile visualizzare il dettaglio di una stazione selezionandola
dall’elenco.

In corrispondenza di ciascuna stazione presente in elenco sono presenti
i pulsanti per la modifica delle informazioni, ad eccezione
dell'identificativo, e per eliminare la stazione.

Domini
------

Ogni Ente Creditore su pagoPA corrisponde ad un Dominio da registrare
nell'anagrafica di GovPay.

Accedendo alla sezione "Configurazioni > Domini”, viene visualizzato
l'elenco dei domini già censiti. Sul lato sinistro è presente il form
per filtrare i domini visualizzati in elenco.

Ciascun dominio presente in elenco è identificato tramite denominazione
e codice identificativo.

Nuovo Dominio
~~~~~~~~~~~~~

Utilizzando il pulsante di creazione, presente nella pagina di elenco, è
possibile procedere con la creazione di un nuovo dominio. È necessario
compilare il form di creazione inserendo i seguenti dati:

-  *Id Dominio*: identificativo del dominio. Obbligatorio, fornito da
   AgID, corrisponde alla Partita Iva dell'ente.
-  Global Location Number: identificativo del dominio nella codifica
   standard GS1. Obbligatorio, fornito da AgID.
-  Stazione: stazione tecnologica scelta in fase di adesione a pagoPA,
   deve essere censita sul sistema. Obbligatorio, fornito da AgID.
-  Dati anagrafici: riferimenti anagrafici del dominio forniti dal
   Referente dei Pagamenti. Obbligatoria solo la ragione sociale.
-  CBILL: codice CBILL per i domini che supportano questa modalità di
   pagamento.
-  Logo: elemento per il caricamento del logo dell’ente creditore
   corrispondente al dominio.
-  *Abilitato*: indica se il dominio è usabile da GovPay per gestire
   nuovi pagamenti o se impedire nuove richieste che lo riguardano.
-  *Codice di segregazione*: se configurato come pluri-intermediato,
   imposta il codice numerico di segregazione. Fornito da AgID.
-  *Prefisso IUV*: prefisso da inserire negli IUV generati da GovPay per
   questo dominio. Il prefisso, numerico, può contenere dei placeholder
   racchiusi tra graffe, ad esempio “1%(a)%(t)”. I placeholder vengono
   sostituiti a runtime con i valori forniti dagli applicativi
   richiedenti o con i valori di sistema configurati. La lunghezza del
   prefisso riduce lo spazio di IUV generabili, quindi e' necessario
   limitarlo il più possibile. Questi i placeholder di sistema,
   sovrascrivibili dall'applicazione chiamante:

   -  a: codice IUV assegnato all'applicazione che gestisce il debito
   -  t: codice IUV assegnato al tributo
   -  y: anno di emissione dello iuv, due cifre
   -  Y: anno di emissione dello iuv, quattro cifre

-  *Aux Digit*: Valore numerico che definisce la struttura del codice
   IUV in funzione del numero di punti di generazione dello stesso (vedi
   "SPECIFICHE ATTUATIVE DEI CODICI IDENTIFICATIVI DI VERSAMENTO,
   RIVERSAMENTO E RENDICONTAZIONE").

Dettaglio Dominio
~~~~~~~~~~~~~~~~~

Selezionando uno dei domini presenti nella pagina di elenco si accede
alla pagina di dettaglio. La pagina di dettaglio di un dominio è
ripartita in quattro distinte aree:

-  *Riepilogo Informazioni*: la visualizzazione dei dati che
   caratterizzano il dominio.
-  *Unità Operative*: sono gli uffici di gestione dei pagamenti in cui è
   suddiviso il dominio dell’ente creditore.
-  *Iban*: sono i codici iban dei conti correnti su cui l’ente creditore
   riceve gli accrediti in banca tesoriera. Tali Iban sono quelli già
   comunicati ad AgID per l’accreditamento.
-  *Entrate*: sono le entrate attive nel dominio dell’ente creditore e
   quindi sulle quali è predisposto per ricevere dei pagamenti.

Tramite il pulsante di modifica presente nella pagina di dettaglio è
possibile procedere con l'aggiornamento dei dati di base, visualizzati
nell'area "Riepilogo Informazioni". Si tenga presente che il valore del
campo “Codice Dominio” non è modificabile.

Le tre aree seguenti contengono i propri pulsanti di creazione, modifica
e cancellazione degli elementi visualizzati.

Unità Operative
^^^^^^^^^^^^^^^

La specifica pagoPA consente di indicare l'anagrafica dell'Unità
operativa titolare del credito, qualora sia diversa da quella dell'Ente
Creditore. È quindi possibile censire le Unità operative del Dominio in
GovPay da utilizzare poi in fase di pagamento.

Il form di creazione di una Unità Operativa deve essere compilato con i
seguenti dati:

-  *Id unità*: identificativo ad uso interno dell'unità operativa.
   Obbligatorio a scelta dell'amministratore.
-  *Anagrafica*: riferimenti anagrafici del dell'unità forniti dal
   Referente dei Pagamenti. Obbligatoria la ragione sociale, opzionali
   le altre informazioni.
-  *Abilitato*: consente di scegliere se abilitare l'unità operativa o
   meno per l'utilizzo nel contesto del dominio.

Tornando all'elenco delle unità operative, è possibile scegliere le
operazioni di modifica e cancellazione degli elementi precedentemente
creati.

Iban
^^^^

Gli iban utilizzati per l'accredito degli importi versati devono essere
censiti su GovPay.

Il form di creazione di un Iban deve essere compilato con i dati
seguenti:

-  *Iban Accredito*: il codice iban del conto di accredito.
   Obbligatorio, fornito dal referente dei .
-  *Bic Accredito*: bic del conto di accredito. Opzionale.
-  *My Bank*: indica se l'iban è abilitato alle transazioni MyBank
-  *Postale*: indica se l'iban di accredito è riferito ad un conto
   corrente postale.
-  *Abilitato*: indica se l'iban è usabile da GovPay per gestire nuovi
   pagamenti o se impedire nuove richieste.

Tornando all'elenco degli Iban, è possibile scegliere le operazioni di
modifica e cancellazione degli elementi precedentemente creati. Il campo
Iban Accredito non è modificabile.

Entrate
^^^^^^^

Ogni importo che costituisce un versamento deve essere associato ad una
entrata censita sul sistema. L'entrata associata al versamento ne
determina l'iban di accredito dell'importo e le coordinate di
rendicontazione.

Il form di creazione di una entrata deve essere compilato con i seguenti
dati:

-  *Tipo entrata*: selezione dell'entrata a scelta tra quelle già
   censite. Obbligatorio. Se non è presente la voce desiderata è
   possibile crearla selezionando la voce "Nuova Entrata":

   -  Compilare i campi della finestra per la creazione della nuova
      entrata fornendo:

      -  *Id Entrata*: identificativo dell'entrata. Obbligatorio, a
         discrezione dell'operatore.
      -  Descrizione: testo di descrizione dell'entrata per facilitarne
         il riconoscimento agli operatori. Obbligatorio, a discrezione
         dell'operatore.
      -  Tipo Contabilità: tipologia di codifica contabile assegnata
         all'entrata (SIOPE/SPECIALE/...). Obbligatorio, fornito dalla
         segreteria.
      -  Codice Contabilità: codice contabilità assegnato all'entrata
         secondo la codifica indicata precedentemente. Obbligatorio,
         fornito dalla segreteria.
      -  *Codifica IUV*: codifica dell'entrata nel contesto degli IUV
         generati da GovPay, se configurato in tal senso.

      *Nota Bene*: I campi *Tipo Contabilità, Codice Contabilità* e
      *Codifica IUV* rappresentano i valori di default per il tipo
      entrata e saranno attualizzabili nel contesto di ciascun dominio a
      scelta dell'operatore.

-  *Iban Accredito*: iban di accredito del tributo a scelta tra quelli
   censiti per il dominio. Obbligatorio.
-  *Iban Appoggio*: L'iban di appoggio viene utilizzato nelle situazioni
   in cui il PSP non è in condizioni di accreditare somme sul conto di
   accredito (si considerino le limitazioni in essere nel circuito
   postale). Opzionale.
-  *Tipo contabilità, Codice contabilità *\ e *Codifica IUV*: se
   valorizzati, sovrascrivono le impostazioni previste nei valori di
   default per l'entrata cui si fa riferimento (come descritto in
   precedenza). Opzionali.
-  *Abilitato*: indica se l'entrata è usabile da GovPay per gestire
   nuovi pagamenti o se impedire nuove richieste.

Tornando all'elenco delle entrate è possibile scegliere le operazioni di
modifica e cancellazione degli elementi precedentemente creati. Il campo
*Codice Entrata* non è modificabile. Fa eccezione l'entrata
preconfigurata “Marca da Bollo Telematica” per la quale si ha la sola
possibilità di modificare i parametri di contabilizzazione.

Applicazioni
------------

Le Applicazioni in GovPay rappresentano i portali di pagamento e i
sistemi applicativi gestionali dei debiti che si interfacciano tramite
le Web API di integrazione.

Accedendo alla sezione “Configurazioni > Applicazioni”, viene
visualizzato l'elenco delle applicazioni già censite. Sul lato sinistro
della pagina è presente un form che consente di filtrare i dati
visualizzati nella pagina.

Nuova Applicazione
~~~~~~~~~~~~~~~~~~

Utilizzando l'apposito pulsante presente nella pagina di elenco, è
possibile creare nuove applicazioni, inserendo nel form di creazione i
seguenti dati:

-  *Id A2A*: identificativo dell'applicazione. Obbligatorio, a
   discrezione del gestore.
-  Principal: identificativo del principal autenticato nelle chiamate
   alle Web API di integrazione. Obbligatorio, a discrezione del
   gestore.
-  Abilitato: se disabilitato, tutte le nuove richieste
   dell'applicazione saranno negate.
-  Codifica Avvisi

   -  Codifica IUV: numero identificativo dell'applicazione nel prefisso
      IUV, se configurato. Opzionale
   -  RegExp IUV: espressione regolare che consente di effettuare la
      validazione dei codici IUV inviati dall'applicazione.
   -  Generazione IUV interna: attivare questo flag nel caso in cui
      l'applicazione generi autonomamente i codici IUV relativi alle
      proprie pendenze. In alternativa i codici saranno generati da
      GovPay.

-  Servizio Verifica

   -  *URL*: Endpoint del servizio di verifica erogato dall'applicazione
      e descritto nel manuale di integrazione (GP-API).
   -  Versione API: versione delle interfacce di integrazione utilizzate
      dall'applicazione. Obbligatorio ed avanzato.
   -  Tipo Autenticazione: selezione a scelta tra: Nessuna, Http-Basic e
      SSL. In base al valore selezionato sarà necessario inserire i
      conseguenti dati di configurazione della specifica modalità di
      autenticazione.

-  Servizio Notifica

   -  *URL*: Endpoint del servizio di notifica erogato dall'applicazione
      e descritto nel manuale di integrazione (GP-API).
   -  Versione API: versione delle interfacce di integrazione utilizzate
      dall'applicazione. Obbligatorio ed avanzato.
   -  Tipo Autenticazione: selezione a scelta tra: Nessuna, Http-Basic e
      SSL. In base al valore selezionato sarà necessario inserire i
      conseguenti dati di configurazione della specifica modalità di
      autenticazione.

Dettaglio Applicazione
~~~~~~~~~~~~~~~~~~~~~~

Selezionando una delle applicazioni presenti nella pagina di elenco si
accede alla pagina di dettaglio. La pagina di dettaglio di
un'applicazione è ripartita in tre distinte aree:

-  Riepilogo Informazioni
-  Domini
-  Entrate
-  Autorizzazioni

Tramite il pulsante di modifica presente nella pagina di dettaglio è
possibile procedere con l'aggiornamento dei dati di base, visualizzati
nell'area "Riepilogo Informazioni".

Le tre aree seguenti contengono i propri pulsanti di creazione, modifica
e cancellazione degli elementi visualizzati.

.. _domini-1:

Domini
^^^^^^

L'area visualizza l'elenco dei domini su cui l'applicazione può agire.
Ciascun dominio in elenco può essere rimosso tramite il pulsante
visibile alla destra dell'elemento. Il pulsante di creazione consente di
aggiungere nuovi domini tra quelli censiti nel sistema.

.. _entrate-1:

Entrate
^^^^^^^

L'area visualizza l'elenco delle entrate su cui l'applicazione può
agire. Ciascuna entrata in elenco può essere rimossa tramite il pulsante
visibile alla destra dell'elemento. Il pulsante di creazione consente di
aggiungere nuove entrate tra quelle censite nel sistema.

Autorizzazioni
^^^^^^^^^^^^^^

L'area visualizza le autorizzazioni possedute dall'applicazione. Le
autorizzazioni visualizzate in elenco possono essere modificate o
eliminate tramite i pulsanti presenti alla destra di ciascun elemento.
Il pulsante di creazione consente di aggiungere nuove autorizzazioni. Il
form di creazione di una autorizzazione deve essere compilato con i
seguenti dati:

-  *Servizio*: indica la specifica funzionalità o entità dati sulla
   quale l'autorizzazione ha effetto (Giornale Eventi, Rendicontazioni,
   ...). Si seleziona un valore da un elenco predefinito. Obbligatorio.
-  *Operazioni*: indica l'operazione consentita sul servizio sopra
   selezionato. Si seleziona una o più scelte tra:

   -  Lettura
   -  Scrittura
   -  Esecuzione

Operatori
---------

Gli operatori rappresentano gli utenti autorizzati all'accesso al
cruscotto di gestione di GovPay.

Accedendo alla sezione “Configurazioni > Operatori”, viene visualizzato
l'elenco degli operatori già censiti. Sul lato sinistro della pagina è
presente un form che consente di filtrare i dati visualizzati nella
pagina.

Gli elementi nell'elenco identificano gli operatori presenti
visualizzando i campi principal e nome.

Nuovo Operatore
~~~~~~~~~~~~~~~

Tramite il pulsante presente nella pagina di elenco è possibile aprire
il form di creazione di un operatore, che deve essere compilato con i
seguenti dati:

-  *Principal*: identificativo del principal autenticato. Obbligatorio,
   a discrezione del gestore.
-  Nome: Nome e cognome dell'utente operatore. Obbligatorio.
-  *Abilitato*: se disabilitato, sarà negato l'accesso al cruscotto di
   gestione.

Dettaglio Operatore
~~~~~~~~~~~~~~~~~~~

Dalla pagina elenco degli operatori, selezionando uno degli elementi, si
giunge alla relativa pagina di dettaglio.

La pagina di dettaglio dell'operatore è composta dalle seguenti aree:

-  Riepilogo Informazioni: area che visualizza i dati identificativi
   dell'operatore.
-  Domini: area che elenca gli enti creditori su cui l'operatore ha
   visibilità.
-  Entrate: area che elenca le entrate sulle quali l'operatore ha
   visibilità.
-  Autorizzazioni: area che elenca le autorizzazioni possedute
   dall'operatore. Le autorizzazioni rappresentano le specifiche
   operazioni che può effettuare.

Tramite il pulsante di modifica presente nella pagina di dettaglio è
possibile aprire il form per l'aggiornamento dei dati identificativi
dell'operatore.

.. _domini-2:

Domini
^^^^^^

L'area visualizza l'elenco dei domini su cui l'operatore può agire.
Ciascun dominio in elenco può essere rimosso tramite il pulsante
visibile alla destra dell'elemento. Il pulsante di creazione consente di
aggiungere nuovi domini tra quelli censiti nel sistema.

.. _entrate-2:

Entrate
^^^^^^^

L'area visualizza l'elenco delle entrate su cui l'operatore può agire.
Ciascuna entrata in elenco può essere rimossa tramite il pulsante
visibile alla destra dell'elemento. Il pulsante di creazione consente di
aggiungere nuove entrate tra quelle censite nel sistema.

.. _autorizzazioni-1:

Autorizzazioni
^^^^^^^^^^^^^^

L'area visualizza le autorizzazioni possedute dall'operatore. Le
autorizzazioni visualizzate in elenco possono essere modificate o
eliminate tramite i pulsanti presenti alla destra di ciascun elemento.
Il pulsante di creazione consente di aggiungere nuove autorizzazioni. Il
form di creazione di una autorizzazione deve essere compilato con i
seguenti dati:

-  *Servizio*: indica la specifica funzionalità, entità o gruppo di
   entità sui quali l'autorizzazione ha effetto (Giornale Eventi,
   Rendicontazioni, ...). Si seleziona un valore da un elenco
   predefinito. Obbligatorio.
-  *Operazioni*: indica l'operazione consentita sul servizio sopra
   selezionato. Si seleziona una o più scelte tra:

   -  Lettura
   -  Scrittura
   -  Esecuzione

Ruoli
-----

I ruoli rappresentano una delle modalità con cui assegnare le
autorizzazioni a operatori e applicazioni. I ruoli vengono acquisiti da
GovPay tramite il profilo utente ottenuto dal sistema che gestisce il
processo di autenticazione. Dopo aver effettuato l'accesso a GovPay,
l'operatore o applicazione ottiene le autorizzazioni che gli sono state
concesse puntualmente (vedi sezioni `7.3.2.3 <#anchor-30>`__ e
`7.4.2.3 <#anchor-36>`__) in aggiunta a quelle associate ai ruoli
posseduti.

La sezione “Configurazioni > Ruoli” mostra l’elenco dei ruoli già
presenti nel sistema.

Nuovo Ruolo
~~~~~~~~~~~

Utilizzando l'apposito pulsante presente nella pagina di elenco, è
possibile creare un nuovo ruolo, inserendo nel form di creazione i
seguenti dati:

-  *Identificativo*: identificativo assegnato al ruolo
-  *Descrizione*: testo che descrive il ruolo
-  *Risorsa*: elenco tra cui selezionare la risorsa protetta sulla quale
   concedere accesso, ad un determinato ruolo, mediante le operazioni
   indicate al punto successivo
-  *Operazioni*: (selezione multipla) consente di specificare quali
   operazioni sono consentite sulla risorsa selezionata per un
   determinato ruolo. Possono essere scelte le seguenti operazioni:

   -  Lettura
   -  Scrittura
   -  Esecuzione

Dettaglio Ruolo
~~~~~~~~~~~~~~~

Selezionando un elemento dall'elenco dei ruoli si accede al suo
dettaglio. La pagina di dettaglio del ruolo è suddivisa in due aree:

-  Riepilogo Informazioni: visualizza i dati identificativi del ruolo:
   Codice Ruolo e Descrizione.
-  Autorizzazioni: visualizza l'elenco delle autorizzazioni che sono
   associate al ruolo

Tramite il pulsante di modifica si accede al form che consente di
aggiornare la descrizione del ruolo.

.. _autorizzazioni-2:

Autorizzazioni
^^^^^^^^^^^^^^

L'area visualizza le autorizzazioni associate al ruolo. Le
autorizzazioni visualizzate in elenco possono essere modificate o
eliminate tramite i pulsanti presenti alla destra di ciascun elemento.
Il pulsante di creazione consente di aggiungere nuove autorizzazioni. Il
form di creazione di una autorizzazione deve essere compilato con i
seguenti dati:

-  *Servizio*: indica la specifica funzionalità, entità o gruppo di
   entità sui quali l'autorizzazione ha effetto (Giornale Eventi,
   Rendicontazioni, ...). Si seleziona un valore da un elenco
   predefinito. Obbligatorio.
-  *Operazioni*: indica l'operazione consentita sul servizio sopra
   selezionato. Si seleziona una o più scelte tra:

   -  Lettura
   -  Scrittura
   -  Esecuzione
