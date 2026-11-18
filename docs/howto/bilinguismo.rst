.. _howto_bilinguismo:

Avvisi pagoPA bilingue
==========================

La piattaforma GovPay supporta la possibilità di stampare gli Avvisi pagoPA in formato bilingue fornendo l'indicazione
della lingua secondaria desiderata come parametro della pendenza.

Lingua secondaria nelle richieste json
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

E' possibile indicare la lingua secondaria nell'elemento **proprieta** della pendenza, disponibile nelle
API Pendenze v2 e nelle API Backoffice:

.. code-block:: json
    :caption: Esempio di pendenza con lingua secondaria

    {
      "proprieta": {
	    "linguaSecondaria": "de",
	    "linguaSecondariaCausale": "Zahlungsgegenstand"
      }
    }

Di seguito le indicazioni sulla valorizzazione dei campi:

  - **linguaSecondaria**: lingua da affiancare all'italiano nella stampa dell'avviso. I valori ammessi sono
    ``de`` (tedesco), ``en`` (inglese), ``fr`` (francese) e ``sl`` (sloveno); il valore ``false`` forza la stampa
    nella sola lingua italiana.
  - **linguaSecondariaCausale**: causale della pendenza tradotta nella lingua secondaria, riportata sull'avviso.

Le operazioni di stampa dell'avviso e dei documenti delle API Pendenze v2
(*GET /avvisi/{idDominio}/{numeroAvviso}* e *GET /documenti/{idDominio}/{numeroDocumento}/avvisi*)
accettano inoltre il parametro di query **linguaSecondaria**, con gli stessi valori, per scegliere la lingua
secondaria al momento della stampa.

Lingua secondaria nel tracciato CSV
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

E' possibile indicare la lingua secondaria nel tracciato CSV valorizzando opportunamente il campo **linguaSecondaria**,
con gli stessi valori; la causale tradotta si indica nello stesso campo, dopo il carattere ``|`` (pipe).
Per maggiori informazioni consultare la sezione :ref:`utente_avanzate_csv`.
