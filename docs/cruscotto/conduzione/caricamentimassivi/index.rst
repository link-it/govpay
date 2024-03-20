.. _govpay_caricamentimassivi:

Caricamenti Massivi
=================

La sezione *Caricamenti Massivi* è dedicata all'immissione massiva delle pendenze nel sistema
tramite tracciato.

Dal pulsante azione si apre la form di caricamento seguente:

.. figure:: ../_images/AV05CaricamentoMassivPendenze.png
   :align: center
   :name: AreaGeneraleCaricamentoMassivoPendenze

   Caricamento massivo pendenze


Il form di caricamento il formato del tracciato di caricamento tra JSON e CSV. Nel caso di formato JSON
la sintassi è quella prevista dalle API Backoffice per la `POST /pendenze/tracciati/`. Nel caso di
formato CSV consultare il :ref:`utente_avanzate_csv`, tenendo presente che la sintassi di default può essere
personalizzata sia a livello di impostazioni generali della piattaforma che di tipologia di pendenza.

Il form di caricamento permette di selezionare il file da caricare che deve essere in formato JSON secondo.
All’interno di un tracciato si definiscono le operazioni da eseguire sulle pendenze, che possono essere:

-  Inserimento di una nuova pendenza
-  Annullamento di pendenza esistente

È possibile filtrare gli elementi, in base al proprio stato di elaborazione, utilizzando il form presente sul lato sinistro.

L'elenco a destra riporta gli elementi, visualizzandone i principali dati identificativi (identificativo del
tracciato, data di caricamento e stato dell’elaborazione).

È possibile scaricare un file CSV con i dati di riepilogo dei tracciati, visualizzati con il criterio di ricerca impostato,
utilizzando la voce "Scarica Resoconto" presente, a destra, nel menu sulla testata della pagina.


.. figure:: ../_images/AV05CaricamentoMassivPendenze.png
   :align: center
   :name: AreaGeneraleCaricamentoMassivoPendenze

   Caricamento massivo pendenze


Dettaglio Tracciato
~~~~~~~~~~~~~~~~~~~

La selezione di un elemento dell’elenco ne visualizza il dettaglio, che
comprende le seguenti informazioni:

-  *Riepilogo Informazioni*: dati generali del tracciato
   (identificativo del tracciato, data di caricamento e stato
   dell’elaborazione, operatore che ha effettuato il caricamento,
   contatori delle operazioni totali, operazioni eseguite, operazioni
   fallite, ... )
-  *Operazioni*: L’elenco delle operazioni eseguite a partire dal
   tracciato (tipo operazione, esito esecuzione, applicazione,
   identificativo pendenza, ... ).

È possibile scaricare un file compresso in formato *zip* contentente il tracciato originale, il
tracciato di esito generato dall’elaborazione e gli avvisi di pagamento per le pendenze caricate.
