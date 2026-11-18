.. _utente_manutenzione:

Manutenzione
============

Accedendo alla sezione *Manutenzione* si ha la possibilità di effettuare
in modalità immediata operazioni che sono normalmente eseguite in
modalità temporizzata dal core di GovPay o dai componenti esterni
(vedi :ref:`inst_componenti_aggiuntivi`).

Le operazioni disponibili sono:

-  *Acquisisci rendicontazioni*: avvia subito il componente esterno govpay-fdr-batch, che acquisisce
   da pagoPA i flussi di rendicontazione e che di norma viene eseguito ogni 2 ore.
   Richiede la proprietà ``it.govpay.batch.fdr.endpoint.url`` del file ``govpay.properties``, con la URL dell'API REST del batch:
   se non è valorizzata (predefinito), l'operazione termina con l'esito "URL endpoint non configurato per il batch".
   Se il batch non è raggiungibile o è già in esecuzione, la console chiede se forzare l'operazione.
-  *Recupera ricevute*: avvia subito il componente esterno govpay-rt-batch, che recupera da pagoPA
   le ricevute dei pagamenti rendicontati ma non ancora acquisiti in GovPay e che di norma viene eseguito ogni 2 ore.
   Richiede la proprietà ``it.govpay.batch.recuperoRT.endpoint.url`` del file ``govpay.properties``.
-  *Resetta la cache*: svuota la cache delle configurazioni per rendere immediatamente
   operative le eventuali modifiche. La cache viene normalmente svuotata ogni 2 ore.
   Le modifiche fatte dalla console sono comunque propagate a tutte le istanze di GovPay, che controllano periodicamente
   la data dell'ultimo reset (proprietà ``it.govpay.batch.cacheCheck.cron``, predefinita ogni minuto).
-  *Invia posizioni debitorie ACA*: avvia subito il componente esterno govpay-aca-batch, che invia le posizioni debitorie
   all'Archivio Centralizzato degli Avvisi e che di norma viene eseguito ogni 10 minuti.
   Richiede la proprietà ``it.govpay.batch.aca.endpoint.url`` del file ``govpay.properties``.

Le voci *Recupera ricevute* e *Invia posizioni debitorie ACA* si possono nascondere o mostrare nel file di configurazione della console
``assets/Config.js``. La prima è visibile per default, la seconda è nascosta per default:

.. code-block:: javascript
   :linenos:

   global.GovPayConfig = {
       MANUTENZIONE: {
   	  RECUPERO_RT: {
   	    ENABLED: true // abilita la funzione di recupero ricevute
   	  },
   	  INVIA_POSIZIONI_DEBITORIE_ACA: {
   	    ENABLED: false // abilita il comando per l'invio posizioni debitorie ACA
   	  }
   	}
   };

Il valore iniziale del file viene dalle proprietà ``it.govpay.backoffice.gui.sezioneManutenzione.recuperaRT.enabled``
(predefinita ``true``) e ``it.govpay.backoffice.gui.sezioneManutenzione.invioACA.enabled`` (predefinita ``false``),
usate nella compilazione e dall'installer.
