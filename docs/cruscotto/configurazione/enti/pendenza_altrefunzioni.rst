.. _govpay_configurazione_enti_altrefunzioni:

Configurazione di servizi ancillari.
------------------------------------

In questa sezione sono personalizzabili alcuni servizi addizionali per specifiche tipologie di pendenza.

.. figure:: ../../_images/31TipoPendenza_altrefunzioni.png
   :align: center
   :name: 31TipoPendenza_altrefunzioni

   Configurazione altre funzioni

Parser tracciati csv
~~~~~~~~~~~~~~~~~~~~~

La sintassi del tracciato di caricamento massivo CSV può essere personalizzata per adeguardi a tracciati preesistenti 
prodotti dall'Ente Creditore in modo da semplificarne l'adesione

.. csv-table:: *Parser tracciati csv*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipo template", "Indica il motore di interpretazione della definizione della form"
   "Template richiesta", "Trasformazione di una entry del CSV in un json PendenzaPost"
   "Template risposta", "Trasformazione del json di esito del caricamento in una entry di CSV di risposta"
   "Linea intestazione esito","Linea di testa del CSV di risposta"

Personalizzazione del dettaglio pendenza
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

TBD 