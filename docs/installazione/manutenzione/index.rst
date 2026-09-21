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
   * - ``tracciati``, ``operazioni``
     - Tracciati di caricamento e annullamento delle pendenze, con le operazioni
       che li compongono e gli eventi a essi collegati.
     - 7 giorni
   * - ``eventi``
     - Giornale degli eventi. È la tabella che cresce di più.
     - 90 giorni
   * - ``BATCH_*``
     - Metadati delle esecuzioni dei batch, gestiti da Spring Batch.
     - 90 giorni

Composizione ed esecuzione
--------------------------

La distribuzione dei sorgenti contiene uno script che compone le tre parti in un
unico script SQL per il dialetto indicato e lo esegue:

.. code-block:: bash

   cd src/main/resources/db
   ./svecchiamento-db.sh <tipoDB> --host <host> --db <database> --user <utente>
   ./svecchiamento-db.sh postgresql --host localhost --db govpay --user govpay

La password si passa con ``--password`` oppure, preferibilmente, con la variabile
d'ambiente ``GOVPAY_DB_PASSWORD``; sono riconosciute anche le altre variabili di
connessione usate dai container, cioè ``GOVPAY_DB_SERVER`` nella forma
``host[:porta]``, ``GOVPAY_DB_NAME`` e ``GOVPAY_DB_USER``.

Prima di procedere lo script chiede conferma indicando utente, host e database.
Con ``-y`` la conferma si salta, ed è la forma da usare in un'esecuzione
pianificata; senza terminale la conferma non è possibile e lo script si
interrompe, per non cancellare dati in un contesto in cui nessuno legge
l'esito.

Con ``--solo-sql`` lo script viene composto e **non** eseguito, e in quel caso i
parametri di connessione non servono. È la forma da preferire la prima volta: il
file prodotto in ``target/svecchiamento-sql/`` può essere riletto, archiviato e
applicato a parte.

Scelta delle parti e della retention
------------------------------------

Le retention predefinite sono quelle della tabella sopra e si sovrascrivono una
per una:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --solo-sql \
       --retention-tracciati 30 --retention-eventi 365 --retention-batch 180

Per i metadati dei batch, in alternativa alla retention si può indicare una data
di taglio esplicita con ``--cutoff-batch 2026-06-30``.

Si può svecchiare una parte sola con ``--senza-core``, che lascia i soli metadati
dei batch, oppure con ``--senza-batch``, che lascia le sole tabelle applicative.

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --senza-core --retention-batch 90

Gli script di svecchiamento dei metadati dei batch non fanno parte del core:
sono cercati fra quelli che accompagnano i componenti del rilascio, dove
arrivano insieme ai binari dei batch. Con ``--sql-batch <dir>`` si indica una
directory già disponibile, per esempio ``/opt/sql/cleanup`` di un'immagine.

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
