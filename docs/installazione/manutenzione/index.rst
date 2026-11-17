.. _inst_manutenzione:

Manutenzione della base dati
============================

Con l'uso, alcune tabelle di GovPay crescono senza che il dato conservato resti
utile a tempo indefinito: i tracciati di caricamento e annullamento con le
operazioni che li compongono, il giornale degli eventi, i metadati delle
esecuzioni dei batch, i flussi di rendicontazione e le pendenze scadute e mai
pagate. Lo svecchiamento periodico di queste tabelle è un'attività
di manutenzione ordinaria, a carico di chi gestisce l'installazione.

Le tabelle interessate sono divise in cinque sezioni, che vivono nella stessa
base dati. Le retention si esprimono in **mesi**:

.. list-table::
   :header-rows: 1
   :widths: 25 50 25

   * - Sezione
     - Contenuto
     - Retention predefinita
   * - ``eventi``
     - Giornale degli eventi (``eventi``). È la tabella che cresce di più.
     - 3 mesi
   * - ``tracciati``
     - Tracciati di caricamento e annullamento delle pendenze (``tracciati``),
       con le operazioni che li compongono e gli eventi a essi collegati.
     - 1 mese
   * - ``spring-batch``
     - Metadati delle esecuzioni dei batch (``BATCH_*``), gestiti da Spring Batch.
     - 3 mesi
   * - ``rendicontazioni``
     - Flussi di rendicontazione (``fr``) con le loro rendicontazioni e gli eventi
       collegati. Pagamenti, pendenze e incassi non vengono toccati.
     - 24 mesi
   * - ``pendenze_scadute_non_pagate``
     - Pendenze da pagare scadute da più della retention, senza voci rendicontate
       né pagamenti, con ciò che le referenzia (voci, RPT, notifiche, notifiche
       App IO, promemoria, stampe, allegati, operazioni).
     - 12 mesi

Sezioni
-------

Ogni area e' uno script SQL a se', in ``sql/<dialetto>/svecchiamento/`` della
distribuzione dei sorgenti: ``eventi.sql``, ``tracciati.sql``,
``spring-batch.sql``, ``rendicontazioni.sql`` e ``pendenze_scadute_non_pagate.sql``.
Ciascuno porta dentro il proprio parametro di retention ed
e' eseguibile da solo con il client del database.

La divisione serve perche' ogni installazione ha cose diverse da svecchiare: si
compongono le sezioni volute, invece di commentare a mano parti di un unico
script.

Composizione ed esecuzione
--------------------------

Lo script ``svecchiamento-db.sh``, nella stessa distribuzione
(``src/main/resources/db/``), compone le sezioni richieste con la retention
passata da fuori e le esegue. Può essere consegnato insieme alla cartella
``sql/`` e usato anche fuori dal repository: in quel caso il risultato va nella
directory ``svecchiamento-sql`` accanto allo script.

.. code-block:: bash

   cd src/main/resources/db
   ./svecchiamento-db.sh <tipoDB> --host <host> --db <database> --user <utente>
   ./svecchiamento-db.sh postgresql --host localhost --db govpay --user govpay

La password si passa con ``--password`` oppure, preferibilmente, con la variabile
d'ambiente ``GOVPAY_DB_PASSWORD``; sono riconosciute anche le altre variabili di
connessione usate dai container, cioe' ``GOVPAY_DB_SERVER`` nella forma
``host[:porta]``, ``GOVPAY_DB_NAME`` e ``GOVPAY_DB_USER``. Gli altri parametri di
connessione sono ``--port`` e, su Oracle, ``--oracle-conn`` (``servicename`` o
``sid``). Come dialetto sono accettati anche gli alias ``hsqldb`` e ``mariadb``.

Prima di procedere lo script chiede conferma indicando utente, host e database.
Con ``-y`` la conferma si salta, ed e' la forma da usare in un'esecuzione
pianificata; senza terminale la conferma non e' possibile e lo script si
interrompe, per non cancellare dati in un contesto in cui nessuno legge
l'esito.

Con ``--solo-sql`` lo script viene composto e **non** eseguito, e in quel caso i
parametri di connessione non servono. E' la forma da preferire la prima volta: il
file prodotto in ``target/svecchiamento-sql/`` puo' essere riletto, archiviato e
applicato a parte.

Con ``--dry-run`` le sezioni vengono eseguite sul database ma ognuna si chiude
con ``ROLLBACK``: lo script riporta le righe che ogni ``DELETE`` cancellerebbe e
non modifica nulla. Con ``--out`` si sceglie la directory di uscita.

Scelta delle sezioni e della retention
--------------------------------------

Con ``--sezioni`` si esegue un sottoinsieme, separato da virgola:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --sezioni eventi,spring-batch --solo-sql

L'ordine di esecuzione resta sempre eventi, tracciati, spring-batch,
rendicontazioni, pendenze_scadute_non_pagate, comunque lo si scriva: il giornale va per primo perche' è la tabella più grande, e sfoltirlo
rende meno costose le ``DELETE`` della sezione tracciati, che su ``eventi``
passano da una sottoquery. L'esito non dipende dall'ordine, perché ciò che viene
cancellato è l'unione dei due criteri. Le rendicontazioni precedono le pendenze
scadute perché quella sezione esclude le pendenze con voci rendicontate:
cancellati i flussi vecchi, le pendenze che rendicontavano diventano cancellabili
nella stessa esecuzione.

La sezione rendicontazioni ha due filtri, disattivi per default: con
``--solo-obsoleti`` cancella solo le revisioni precedenti di un flusso
ripubblicato, con ``--solo-non-incassati`` solo i flussi non riconciliati con un
incasso. Indicati entrambi, valgono insieme.

Le retention predefinite sono quelle della tabella sopra e si sovrascrivono una
per una:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --solo-sql \
       --retention-tracciati 1 --retention-eventi 12 --retention-spring-batch 6

Cosa viene cancellato, e quando eseguirlo
-----------------------------------------

Sulle tabelle applicative la cancellazione segue la data di completamento dei
tracciati, la data degli eventi, la data di acquisizione dei flussi e la data di
scadenza delle pendenze, e rispetta l'ordine imposto dalle chiavi esterne: per
esempio prima gli eventi e le operazioni collegati ai tracciati scaduti, poi i
tracciati. I documenti a cui appartengono le pendenze cancellate non vengono
toccati, perché raggruppano anche pendenze non scadute.

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
