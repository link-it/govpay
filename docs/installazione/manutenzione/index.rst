.. _inst_manutenzione:

Manutenzione della base dati
============================

Con l'uso, molte tabelle di GovPay crescono senza che il dato conservato resti
utile a tempo indefinito: il giornale degli eventi, i metadati delle esecuzioni
dei batch, i tracciati, l'audit degli operatori, le pendenze chiuse da tempo con
i loro pagamenti, i flussi di rendicontazione e gli incassi. Lo svecchiamento
periodico di queste tabelle è un'attività di manutenzione ordinaria, a carico di
chi gestisce l'installazione.

Lo svecchiamento segue due regole:

- il **giornale degli eventi** e i **metadati dei batch** vengono cancellati e
  basta;
- **tutto il resto** viene prima copiato in tabelle di archivio giornaliere,
  ``<tabella>_AAAAMMGG``, che il DBA salva ed elimina secondo le proprie
  procedure di backup.

Le anagrafiche (domini, applicazioni, operatori, tipi pendenza, configurazione)
non vengono mai toccate.

Sezioni
-------

Lo svecchiamento è diviso in sezioni, una per area di dati, eseguite sempre in
quest'ordine. Le retention sono espresse in giorni.

.. list-table::
   :header-rows: 1
   :widths: 30 45 12 13

   * - Sezione
     - Cosa cancella
     - Retention
     - Archivio
   * - ``eventi``
     - Il giornale degli eventi.
     - 30
     - no
   * - ``tracciati``
     - I tracciati di caricamento completati, con le loro operazioni.
     - 30
     - sì
   * - ``spring-batch``
     - I metadati delle esecuzioni dei batch.
     - 30
     - no
   * - ``tracciati_notifica_pagamenti``
     - I tracciati di notifica dei pagamenti.
     - 30
     - sì
   * - ``audit``
     - L'audit delle operazioni degli operatori.
     - 365
     - sì
   * - ``pendenze_scadute_non_pagate``
     - Le pendenze non pagate, scadute, senza pagamenti né rendicontazioni.
     - 365
     - sì
   * - ``pendenze_annullate``
     - Le pendenze annullate, senza pagamenti né rendicontazioni.
     - 365
     - sì
   * - ``flussi_rendicontazione``
     - I flussi di rendicontazione, con rendicontazioni e incassi, e le pendenze
       che rendicontano.
     - 730
     - sì
   * - ``pendenze_pagate``
     - Le pendenze pagate con ricevuta e mai rendicontate.
     - 730
     - sì
   * - ``documenti_orfani``
     - I documenti rimasti senza pendenze.
     - —
     - sì

Delle pendenze vengono cancellate anche voci, RPT e ricevute, pagamenti,
notifiche, promemoria, stampe, operazioni e allegati. Una pendenza rendicontata
anche in un flusso più recente della soglia, o con un pagamento più recente,
resta.

Esecuzione
----------

Lo script ``svecchiamento-db.sh``, in ``src/main/resources/db`` della
distribuzione dei sorgenti, compone le sezioni e le esegue con il client nativo
del database:

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
pianificata, per esempio ogni notte.

Con ``--sezioni`` si esegue un sottoinsieme, separato da virgola, e con
``--retention-<sezione> <giorni>`` si cambia la retention di una sezione:

.. code-block:: bash

   ./svecchiamento-db.sh postgresql --sezioni eventi,spring-batch \
       --retention-eventi 15 --retention-spring-batch 15

Con ``--dry-run`` lo svecchiamento viene simulato: ogni sezione viene eseguita e
poi annullata, e lo script riporta quante righe cancellerebbe. Con
``--solo-sql`` lo script viene solo composto, senza connettersi al database, e
può essere riletto e applicato a parte.

.. warning::
   Per ``flussi_rendicontazione`` e ``pendenze_pagate`` una retention inferiore
   a 730 giorni viene rifiutata, a meno di aggiungere ``--dry-run``, per
   simulare, o ``--force``, per cancellare davvero.

Archivio
--------

Le righe cancellate dalle sezioni con archivio vengono copiate, nella stessa
transazione della cancellazione, in tabelle con il nome della tabella d'origine e
la data dell'esecuzione, per esempio ``versamenti_20261007`` o
``fr_20261007``. L'archivio contiene esattamente le righe cancellate.

Il backup è giornaliero: se lo script trova nel database anche una sola tabella
di archivio con la data di oggi, salta le sezioni con archivio ed esegue solo
quelle sugli eventi e sui metadati dei batch. Un secondo lancio nello stesso
giorno, quindi, non ripete il backup.

.. note::
   Salvare ed eliminare le tabelle di archivio è compito del DBA. Finché restano
   nel database, i dati svecchiati continuano a occupare spazio. Dopo
   un'esecuzione fallita, per riprovare nello stesso giorno occorre prima
   salvare ed eliminare le tabelle di archivio di quel giorno.

Requisiti e comportamento
-------------------------

Lo script usa il client nativo del database: ``psql``, ``sqlplus``, ``mysql``,
``sqlcmd`` e, su HSQLDB, SqlTool. Deve quindi essere eseguito da una postazione
che abbia il client del dialetto in uso e che raggiunga la base dati, con un
utente che possa cancellare dalle tabelle e crearne di nuove: il più semplice è
lo stesso utente di GovPay.

Ogni sezione è eseguita in una transazione a sé: se una fallisce, quelle
completate prima di essa restano applicate, e lo script lo segnala
esplicitamente.

Sui metadati dei batch non c'è filtro sullo stato delle esecuzioni: è una scelta
degli script di Spring Batch, che serve a bonificare le esecuzioni rimaste
appese, e un'esecuzione creata prima della soglia viene cancellata anche se non
è terminata.

.. warning::
   La cancellazione è definitiva, salvo quanto copiato nelle tabelle di
   archivio: eseguire un backup del database prima della prima esecuzione, e
   verificare il risultato su un ambiente di collaudo con ``--dry-run``.

La documentazione completa, con tutte le opzioni e i requisiti, è nel file
``src/main/resources/db/README-svecchiamento.md`` della distribuzione dei
sorgenti.
