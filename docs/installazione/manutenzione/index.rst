.. _inst_manutenzione:

Manutenzione della base dati
============================

Con l'uso, alcune tabelle di GovPay crescono senza che il dato conservato resti
utile a tempo indefinito: i tracciati di caricamento e annullamento con le
operazioni che li compongono, il giornale degli eventi, e i metadati delle
esecuzioni dei batch. Lo svecchiamento periodico di queste tabelle è un'attività
di manutenzione ordinaria, a carico di chi gestisce l'installazione.

Le tabelle interessate sono tre gruppi distinti, che vivono nella stessa base
dati:

.. list-table::
   :header-rows: 1
   :widths: 30 45 25

   * - Tabelle
     - Contenuto
     - Retention predefinita
   * - ``eventi``
     - Giornale degli eventi. È la tabella che cresce di più.
     - 90 giorni
   * - ``tracciati``, ``operazioni``
     - Tracciati di caricamento e annullamento delle pendenze, con le operazioni
       che li compongono e gli eventi a essi collegati.
     - 7 giorni
   * - ``BATCH_*``
     - Metadati delle esecuzioni dei batch, gestiti da Spring Batch.
     - 90 giorni

Sezioni
-------

Ogni area e' uno script SQL a se', in ``sql/<dialetto>/svecchiamento/`` della
distribuzione dei sorgenti: ``tracciati.sql``, ``eventi.sql`` e
``spring-batch.sql``. Ciascuno porta dentro il proprio parametro di retention ed
e' eseguibile da solo con il client del database.

La divisione serve perche' ogni installazione ha cose diverse da svecchiare: si
compongono le sezioni volute, invece di commentare a mano parti di un unico
script.

Composizione ed esecuzione
--------------------------

Lo script ``svecchiamento-db.sh``, nella stessa distribuzione, compone le sezioni
richieste con la retention passata da fuori e le esegue:

.. code-block:: bash

   cd src/main/resources/db
   ./svecchiamento-db.sh <tipoDB> --host <host> --db <database> --user <utente>
   ./svecchiamento-db.sh postgresql --host localhost --db govpay --user govpay

La password si passa con ``--password`` oppure, preferibilmente, con la variabile
d'ambiente ``GOVPAY_DB_PASSWORD``; sono riconosciute anche le altre variabili di
connessione usate dai container, cioe' ``GOVPAY_DB_SERVER`` nella forma
``host[:porta]``, ``GOVPAY_DB_NAME`` e ``GOVPAY_DB_USER``.

Prima di procedere lo script chiede conferma indicando utente, host e database.
Con ``-y`` la conferma si salta, ed e' la forma da usare in un'esecuzione
pianificata; senza terminale la conferma non e' possibile e lo script si
interrompe, per non cancellare dati in un contesto in cui nessuno legge
l'esito.

Con ``--solo-sql`` lo script viene composto e **non** eseguito, e in quel caso i
parametri di connessione non servono. E' la forma da preferire la prima volta: il
file prodotto in ``target/svecchiamento-sql/`` puo' essere riletto, archiviato e
applicato a parte.

Scelta delle sezioni e della retention
--------------------------------------

Con ``--sezioni`` si esegue un sottoinsieme, separato da virgola:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --sezioni eventi,spring-batch --solo-sql

L'ordine di esecuzione resta sempre eventi, tracciati, spring-batch, comunque lo
si scriva: il giornale va per primo perche' è la tabella più grande, e sfoltirlo
rende meno costose le ``DELETE`` della sezione tracciati, che su ``eventi``
passano da una sottoquery. L'esito non dipende dall'ordine, perché ciò che viene
cancellato è l'unione dei due criteri.

Le retention predefinite sono quelle della tabella sopra e si sovrascrivono una
per una:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --solo-sql \
       --retention-tracciati 30 --retention-eventi 365 --retention-batch 180

Cosa viene cancellato, e quando eseguirlo
-----------------------------------------

Sulle tabelle applicative la cancellazione segue la data di completamento dei
tracciati e la data degli eventi, e rispetta l'ordine imposto dalle chiavi
esterne: prima gli eventi e le operazioni collegati ai tracciati scaduti, poi i
tracciati, poi il resto del giornale per età.

Sui metadati dei batch **non c'è filtro sullo stato delle esecuzioni**. È una
scelta degli script di Spring Batch, e serve a poter bonificare le esecuzioni
rimaste appese; ne segue però che un job **in corso** la cui esecuzione è
anteriore alla data di taglio viene cancellato, e Spring Batch ne perde traccia.

.. warning::
   Lo svecchiamento va eseguito a batch fermi, oppure con una data di taglio
   abbastanza indietro nel tempo perché nessuna esecuzione viva vi ricada. La
   cancellazione è definitiva: eseguire un backup del database prima della prima
   esecuzione, e verificare il risultato su un ambiente di collaudo.

Ogni parte è eseguita in una transazione a sé: se una fallisce, quelle
completate prima di essa restano applicate. Lo script lo segnala
esplicitamente. La via di ripristino è correggere la causa e rieseguire, perché
lo svecchiamento cancella per data e non per stato, ed è quindi ripetibile senza
effetti aggiuntivi.

.. note::
   Lo script usa il client nativo del database: ``psql``, ``sqlplus``,
   ``mysql``, ``sqlcmd`` e, su HSQLDB, SqlTool. Deve quindi essere eseguito da
   una postazione che abbia il client del dialetto in uso, e che raggiunga la
   base dati.
