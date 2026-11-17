.. _govpay_configurazione_enti_altrefunzioni:

Configurazione di servizi ancillari
-----------------------------------

In questa sezione sono personalizzabili alcuni servizi addizionali per specifiche tipologie di pendenza.

.. figure:: ../../_images/31TipoPendenza_altrefunzioni.png
   :align: center
   :name: 31TipoPendenza_altrefunzioni

   Configurazione altre funzioni

Parser tracciati csv
~~~~~~~~~~~~~~~~~~~~~

La sintassi del tracciato di caricamento massivo CSV (vedi :ref:`govpay_caricamentimassivi`) può essere personalizzata per adeguarsi a tracciati preesistenti 
prodotti dall'Ente Creditore in modo da semplificarne l'adesione

.. csv-table:: *Parser tracciati csv*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipo template", "Motore dei template di trasformazione: Nessuno o Freemarker"
   "Template richiesta", "Trasformazione di una entry del CSV in un json PendenzaPost"
   "Template risposta", "Trasformazione del json di esito del caricamento in una entry di CSV di risposta"
   "Linea intestazione esito","Linea di testa del CSV di risposta"

Personalizzazione del dettaglio pendenza
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Nel campo *Template* si può caricare un documento JSON che aggiunge al dettaglio delle pendenze del tipo, nella console, un riquadro con
alcuni dati della pendenza. Il documento indica il titolo del riquadro e, per ogni dato, l'etichetta e il percorso del campo nel JSON
della pendenza restituito dalle API Backoffice, con i nomi dei campi separati da un punto:

.. code-block:: json

   {
     "vistaDettaglio": {
       "titolo": "Dati della pratica",
       "campi": [
         { "label": "Causale", "path": "causale" },
         { "label": "Numero pratica", "path": "datiAllegati.numeroPratica" }
       ]
     }
   }

Il riquadro è visualizzato solo se sono indicati il titolo e almeno un campo. 