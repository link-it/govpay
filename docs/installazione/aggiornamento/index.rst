.. _update_ambiente:

Aggiornamento di versione
=========================

GovPay viene costantemente aggiornato per la risoluzione di problemi o l'implementazione
di nuove funzionalità, pertanto può risultare necessario effettuare l'installazione di una nuova
versione nella propria piattaforma.

GovPay segue il `versionamento semantico <https://semver.org/lang/it/>`_, pertanto si presentano solitamente due casistiche:

- Aggiornamento di una patch version
- Aggiornamento di una minor version

Non tratteremo il caso di aggiornamento di una major version poichè attualmente non previste.

Aggiornamento di una patch version
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Nel caso di aggiornamento di una patch version, ad esempio per aggiornare la versione 3.1.1 alla 3.1.3,
è sufficiente sostituire l'archivio EAR dispiegato nell'Application Server. 

Si consiglia di effettuare l'aggiornamento dell'EAR con l'Application Server spento.

Aggiornamento di una minor version
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Nel caso di aggiornamento di una minor version oltre a sostituire l'EAR può essere necessario:

- Applicare una o più patch al database
- Aggiungere i file di Spring security alla directory di lavoro la configurazione usata lo prevede

Ad esempio, nel caso di aggiornamento dalla versione 3.4.1 alla versione 3.6.0 è necessario

- Applicare le patch 3.5.sql e 3.6.sql
- Copiare il file di configurazione api-jppapdp-applicationContext-security.xml nella directory di lavoro
 
Si consiglia di effettuare le operazioni di aggiornamento con l'Application Server spento
e di eseguire un backup del DB prima di applicare le patch.

Composizione della patch di aggiornamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Individuare a mano quali patch applicare e in quale ordine è la parte più
soggetta a errore dell'aggiornamento. La distribuzione dei sorgenti contiene uno
script che compone in un unico file tutte le patch necessarie a portare
un'installazione da una versione a un'altra:

.. code-block:: bash

   cd src/main/resources/db
   ./build-upgrade-sql.sh <tipoDB> <versioneDA> <versioneA>
   ./build-upgrade-sql.sh postgresql 3.8.2 3.10.0

I dialetti ammessi sono ``postgresql``, ``oracle``, ``mysql``, ``sqlserver`` e
``hsql``. Lo script scrive il risultato in ``target/upgrade-sql/`` e ne elenca a
schermo il contenuto.

Le patch del core sono nominate con la versione a cui **portano**, quindi
aggiornare da X ad A significa applicare, in ordine di versione, tutte quelle
comprese fra X escluso e A incluso: chi si trova alla 3.8.2 ha già applicato
``3.8.2.sql``. L'ordinamento è per versione e non alfabetico, perché la 3.10.0
segue e non precede la 3.9.

Non tutte le versioni hanno una patch: un rilascio che non ha modificato la base
dati non ne ha, e la 3.9.3 è un caso reale. Indicarne una come estremo non è
quindi un errore, né in partenza né in destinazione: l'intervallo viene calcolato
per confronto di versione e l'ultima patch inclusa è la più alta che non superi
la destinazione. Lo script lo segnala a schermo e nell'intestazione del file
prodotto. Si ferma invece se fra le due versioni non cade alcuna patch, o se la
versione di partenza non precede quella di destinazione.

Dopo quelle del core, lo script accoda le eventuali patch dei componenti
aggiuntivi del rilascio, lette dallo SQL che accompagna ciascun componente.
Questa parte richiede accesso alla rete e a Docker; con ``--senza-componenti`` si
salta, ottenendo le sole patch del core.

Restano sempre fuori le patch sotto ``patch/clienti/``, specifiche di singole
installazioni, e le patch cumulative preconfezionate come
``patch/3.8.2_to_3.9.2/``, che sono un'alternativa a questo script e non un suo
ingrediente. I file presenti fra le patch il cui nome non è una versione vengono
elencati fra quelli **non inclusi**, a schermo e nell'intestazione dello script
prodotto, invece di essere scartati in silenzio.

.. note::
   Lo script vive nella distribuzione dei sorgenti, non nell'archivio prodotto
   dalla procedura di installazione: quest'ultimo contiene ``sql/gov_pay.sql`` e
   l'insieme delle patch del dialetto scelto sotto ``sql/patch/``, che sono gli
   ingredienti, non lo strumento che li compone.

Lo script composto va applicato con il client del database, in modo che
l'esecuzione si interrompa al primo errore. Su PostgreSQL:

.. code-block:: bash

   psql -v ON_ERROR_STOP=1 -h <host> -U <utente> -d <database> \
        -f target/upgrade-sql/govpay-upgrade-3.8.2-a-3.10.0-postgresql.sql

.. warning::
   Le patch non sono applicate in transazione: un errore a metà lascia applicati
   gli statement precedenti. Eseguire un backup del database prima di
   applicarle, come indicato sopra, e in caso di errore correggere la causa e
   riprendere da lì.

Migrazione dei metadati di Spring Batch
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

I batch di GovPay registrano l'esito delle proprie esecuzioni nelle tabelle
``BATCH_*`` di Spring Batch. Quando l'aggiornamento comporta un cambio di
versione del framework, quelle tabelle vanno migrate, e la migrazione è fornita
da Spring Batch stesso.

Non è inclusa per default nella patch di aggiornamento, e va chiesta
esplicitamente indicando la versione del framework di destinazione:

.. code-block:: bash

   ./build-upgrade-sql.sh postgresql 3.8.2 3.10.0 --con-migrazione-batch 6.0

Viene accodata in una sezione separata e finale dello script prodotto. Gli script
di migrazione sono cercati fra quelli che accompagnano i componenti del
rilascio, dove arrivano da Spring Batch; con ``--sql-batch <dir>`` si può
indicare una directory ``sql/spring-batch`` già disponibile, per esempio estratta
dall'archivio ``sql.zip`` di un componente o presa da ``/opt/sql`` di
un'immagine. Se la versione richiesta non è disponibile, lo script elenca quelle
presenti.

.. warning::
   Questi script trasformano tabelle ``BATCH_*`` che esistono già nella forma
   precedente: **non sono ripetibili**, e su un'installazione che non le ha, o
   che le ha già migrate, si interrompono con errore. Va richiesta solo sapendo
   in quale forma si trovano le tabelle dei metadati.

.. note::
   Un'installazione che non ha mai persistito i metadati dei batch non ha nulla
   da migrare: le tabelle vengono create nella forma corrente insieme al resto
   dello schema.

Aggiornamento da versioni precedenti la 3.5
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Il nuovo client HTTP utilizzato da GovPay ignora la configurazione del keystore e trustore della JVM. 
Risulta quindi necessario verificare che i parametri per l'autenticazione SSL, ove prevista, siano
specificati tramite Cruscotto di Backoffice nella configurazione delle Applicazioni e degli Intermediari.

Aggiornamento da versioni precedenti la 3.7
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La distribuzione standard di GovPay prevede il seguente stack applicativo:

- Java 11
- Wildfly 26

Aggiornamento da versioni precedenti la 3.8
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La distribuzione standard di GovPay dalla versione 3.8.0 prevede il seguente stack applicativo:

- Java 21
- Tomcat 10/11

I servizi sono esposti utilizzando nuove BaseUrl, di seguito il mapping con le vecchie versioni:

+---------------------------------+-------------------------------------------------+
| 3.7.x                           |  3.8.0                                          |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/api/pagopa     | /govpay-api-pagopa                              |
+---------------------------------+-------------------------------------------------+
| /govpay/frontend/api/user       | /govpay-api-user                                |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/pendenze    | /govpay-api-pendenze                            |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/ragioneria  | /govpay-api-ragioneria                          |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/jppapdp     | /govpay-api-jppapdp                             |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/api/backoffice  | /govpay-api-backoffice                          |
+---------------------------------+-------------------------------------------------+
| /govpay/backend/gui/backoffice  | /govpay-console                                 |
+---------------------------------+-------------------------------------------------+


Aggiornamento da versioni precedenti la 3.11
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La versione 3.11.0 introduce nel database il modello delle posizioni debitorie
dell'API v3: ogni pendenza appartiene a una posizione debitoria (tabella
``documenti``), che ha una o più opzioni di pagamento (``opzioni_pagamento``),
cioè una soluzione unica, un piano rateale o una soluzione unica entro, oltre
una soglia, ridotta o scontata, e uno o più soggetti debitori
(``soggetti_debitori``).

Le API v2 continuano a funzionare sugli stessi dati e restano disponibili per
retrocompatibilità.

Requisiti del database
^^^^^^^^^^^^^^^^^^^^^^

La patch usa funzioni che non sono disponibili nelle versioni precedenti a:

- PostgreSQL 13;
- Oracle 12.2;
- SQL Server 2017.

Migrazione dei dati nella patch 3.11.0
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Oltre alle tabelle e alle colonne nuove, la patch ``3.11.0.sql`` porta i dati
esistenti nel nuovo modello:

- ogni pendenza senza posizione debitoria ne riceve una, con codice
  ``MIGR-<id della pendenza>`` e la causale come descrizione;
- per ogni posizione vengono create le opzioni di pagamento, ricavate dal codice
  rata delle pendenze:

  - tutte senza codice rata: una soluzione unica;
  - codici rata numerici e distinti: un piano rateale;
  - codici di soglia distinti (es. ``ENTRO5``, ``OLTRE10``, ``RIDOTTO``,
    ``SCONTATO``): un'opzione per ciascuna pendenza;

- lo stato di ogni opzione (disponibile, attivata o annullata) deriva da quello
  delle sue pendenze;
- il debitore della pendenza diventa il primo soggetto debitore della posizione.

Su installazioni con molte pendenze la patch può richiedere un tempo
apprezzabile: va considerato nel pianificare la finestra di aggiornamento.

I casi che non si possono migrare non bloccano l'aggiornamento: restano come
sono e vengono elencati nella tabella ``migrazione_v3_scarti``, con il motivo:

+------------------------------+------------------------------------------------------------+
| Motivo                       | Significato                                                |
+==============================+============================================================+
| CAUSALE_NON_DECODIFICABILE   | la causale della pendenza non è in un formato leggibile    |
+------------------------------+------------------------------------------------------------+
| COLLISIONE_COD_DOCUMENTO     | il codice ``MIGR-<id>`` è già usato da un'altra posizione  |
+------------------------------+------------------------------------------------------------+
| DEBITORI_INCOERENTI          | le pendenze della stessa posizione hanno debitori diversi  |
+------------------------------+------------------------------------------------------------+
| COD_RATA_ANOMALO             | i codici rata delle pendenze della posizione non rientrano |
|                              | in nessuno dei casi previsti                               |
+------------------------------+------------------------------------------------------------+

Dopo l'aggiornamento:

1. consultare ``migrazione_v3_scarti``;
2. correggere i dati indicati;
3. rieseguire la patch ``3.11.0.sql``.

La patch è rieseguibile: le strutture già presenti non vengono ricreate, e la
migrazione considera solo le posizioni non ancora migrate. A ogni esecuzione la
tabella degli scarti viene svuotata e riempita di nuovo.

A bonifica conclusa la tabella degli scarti non serve più e si elimina:

.. code-block:: sql

   DROP TABLE migrazione_v3_scarti;

Riallineamento al passaggio all'API v3
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Fra l'aggiornamento alla 3.11.0 e il passaggio delle applicazioni all'API v3,
le API v2 continuano a creare e aggiornare pendenze senza aggiornare le opzioni
di pagamento. Al momento del passaggio va quindi eseguito, una volta, lo script
di riallineamento. Lo script:

- collega alle posizioni già migrate le pendenze aggiunte nel frattempo, quando
  l'opzione a cui appartengono è univoca;
- aggiorna lo stato delle opzioni e le date delle posizioni migrate;
- migra le pendenze create dopo l'aggiornamento, con le stesse regole della
  patch.

Si trova in ``sql/<tipoDB>/migrazione/riallineamento-v3.sql``, sia nell'archivio
prodotto dalla procedura di installazione sia nei sorgenti
(``src/main/resources/db/sql/<tipoDB>/migrazione/``). Lo SQL allegato ai rilasci
lo contiene come ``govpay-<versione>-riallineamento-v3-<tipoDB>.sql``. Non fa
parte dello script prodotto da ``build-upgrade-sql.sh``, che compone solo le
patch.

Come la patch, è rieseguibile e riempie ``migrazione_v3_scarti`` con i casi che
non riesce a sistemare: anche qui si correggono e lo si riesegue.

Va eseguito con il client del database in modo che si interrompa al primo
errore, come indicato nella sua intestazione:

- PostgreSQL: ``psql -v ON_ERROR_STOP=1``;
- MySQL e MariaDB: il client ``mysql`` senza ``--force``;
- Oracle: ``WHENEVER SQLERROR EXIT FAILURE``;
- SQL Server: ``sqlcmd -b``.
