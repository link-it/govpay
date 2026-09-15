.. _inst_container:

Installazione su container
==========================

In alternativa al dispiegamento su application server descritto nelle sezioni
precedenti, GovPay può essere installato in container. Tutti i componenti del
prodotto sono distribuiti come immagini Docker sul registro pubblico
`linkitaly <https://hub.docker.com/u/linkitaly>`_, e possono essere orchestrati
con un unico file Docker Compose.

Questa modalità non sostituisce la procedura guidata di installazione: la
configurazione dei componenti avviene per variabili d'ambiente, e la creazione
dello schema del database è a carico di chi installa, come descritto più avanti.

Componenti dell'ambiente
------------------------

L'esempio allegato avvia l'insieme completo dei componenti previsti dal
rilascio:

.. list-table::
   :header-rows: 1
   :widths: 25 75

   * - Componente
     - Ruolo
   * - ``postgres``
     - Base dati, condivisa da tutti i componenti.
   * - ``core``
     - GovPay: API di integrazione, colloquio con il Nodo dei Pagamenti,
       console di backoffice.
   * - ``console`` e ``console-api``
     - Cruscotto di gestione e le API che lo servono.
   * - ``govpay-gde``
     - Giornale degli eventi. I batch non partono finché non risponde, per non
       perdere gli eventi dei primi cicli.
   * - ``govpay-stampe``
     - Produzione degli avvisi di pagamento in PDF.
   * - ``govpay-aca``
     - Sincronizzazione dell'Archivio dei pagamenti in attesa.
   * - ``govpay-fdr``
     - Acquisizione dei flussi di rendicontazione.
   * - ``govpay-iban``
     - Sincronizzazione degli IBAN.
   * - ``govpay-maggioli-jppa``
     - Integrazione con il protocollo JPPA.
   * - ``govpay-notify``
     - Spedizione di promemoria e ricevute verso i debitori e gli enti.
   * - ``govpay-rt``
     - Recupero delle ricevute telematiche da pagoPA.
   * - ``govpay-tracciati``
     - Elaborazione dei tracciati di caricamento e annullamento pendenze.

Preparazione
------------

Copiare in una directory di lavoro i due file allegati a questa pagina,
``docker-compose.yml`` e ``.env``, e crearvi accanto due sottodirectory:

**1. Lo schema del database, in** ``initdb/``

Lo script SQL completo del rilascio è pubblicato come asset
``govpay-sql-<versione>.zip`` della release di GovPay su GitHub. Contiene, per
ciascun dialetto supportato, uno script di installazione da zero e uno di
aggiornamento da una versione precedente. Estrarre lo script di installazione
per il proprio dialetto e depositarlo in ``initdb/``:

::

   unzip govpay-sql-3.10.0.zip
   cp govpay-3.10.0-install-postgresql.sql initdb/

Quello script contiene lo schema del core, le tabelle dei componenti e le
tabelle dei metadati di Spring Batch usate dai batch, già deduplicate e
nell'ordine corretto di applicazione. PostgreSQL esegue il contenuto di
``initdb/`` una sola volta, alla creazione del volume dati: per riapplicarlo
occorre rimuovere il volume.

**2. Il driver JDBC, in** ``jdbc-drivers/``

Alcuni componenti caricano il driver JDBC da ``/opt/jdbc-drivers``. Per
PostgreSQL e MySQL il driver è già incluso nelle immagini e la directory può
restare vuota; per Oracle va depositato il driver, che non è redistribuibile e
deve essere scaricato dal sito del produttore.

**3. L'utenza applicativa dei batch**

I batch che invocano le API del core si autenticano con un'utenza applicativa,
indicata in ``.env`` da ``GOVPAY_BATCH_USER`` e ``GOVPAY_BATCH_PASSWORD``.
L'utenza va creata in anagrafica prima di avviare l'ambiente, e la password va
cambiata rispetto al valore di esempio.

Avvio
-----

::

   docker compose up -d
   docker compose ps

Al termine dell'avvio, che richiede qualche minuto perché il core attende il
database e i batch attendono il giornale degli eventi, i servizi sono
raggiungibili sulle porte indicate in ``.env``: il core su
``http://localhost:8080`` e la console su ``http://localhost:8081``.

Versioni
--------

Le versioni delle immagini sono raccolte nel file ``.env``, una per componente,
e sono quelle previste dal rilascio. Per i componenti che portano struttura di
database corrispondono a quelle dichiarate in
``src/main/resources/db/release-components.env`` del progetto ``govpay``, che
registra la composizione del rilascio ed è aggiornato nello stesso commit che
porta la versione del core.

Aggiornando l'ambiente a un rilascio successivo vanno cambiate le versioni in
``.env`` e applicato lo script di aggiornamento del dialetto in uso, contenuto
nello stesso asset ``govpay-sql-<versione>.zip``.

File di esempio
---------------

.. literalinclude:: esempio/docker-compose.yml
   :language: yaml
   :caption: docker-compose.yml

.. literalinclude:: esempio/.env
   :language: bash
   :caption: .env
