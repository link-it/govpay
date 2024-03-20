.. _utente_riscossioni:

Riscossioni
===========

La sezione *Riscossioni* è dedicata alla consultazione delle somme che sono state correttamente riscosse tramite i versamenti operati dai debitori.


Visualizzazione della sezione
-----------------------------

Attraverso le impostazioni della console si decide se visualizzare questa sezione, la sezione è nascosta di default.
Per abilitarla si deve intervenire sulla seguente proprietà:

.. code-block:: javascript
   :linenos:

   global.GovPayConfig = {
       GESTIONE_RISCOSSIONI: {
           ENABLED: true // abilita la sezione riscossioni
       }
   };


Dettaglio sezione
-----------------

Anche in questo caso risulta possibile filtrare gli elementi presenti nella pagina tramite il form presente sul lato sinistro. Di rilevante importanza è la possibilità di filtrare in base allo stato della riscossione:

-  *Riscossa* - è lo stato iniziale relativo agli importi riversati ma non ancora riconciliati.
-  *Riconciliata* - è lo stato finale che indica che tutti gli importi di una determina pendenza sono stati già riconciliati con le somme riversate.

È inoltre possibile selezionare le riscossioni in base al tipo. Esistono due tipi di riscossione:

-  *Entrata in Tesoreria*: Sono cifre riscosse dai PSP che verranno riversate sul conto della banca tesoriera dell'ente creditore. Si
   tratta di somme soggette a riconciliazione.
-  *Marca da Bollo Telematica*: Sono cifre riscosse dai PSP per il rilascio di una marca da bollo. Tali importi non saranno accreditati
   all'ente e quindi non sono soggetti a riconciliazione.

.. figure:: ../../_images/AV03AreaGeneraleRiscossioni.png
   :align: center
   :name: AreaGeneraleRiscossioni

   Area Generale Riscossioni

È possibile scaricare un file CSV con i dati delle riscossioni, visualizzate con il criterio di ricerca impostato, utilizzando la voce
"Scarica Resoconto" presente nel menu a destra sulla testata della pagina.

Selezionando il singolo elemento dall’elenco si accede alla pagina di dettaglioche riporta ulteriori informazioni non modificabili:

.. figure:: ../../_images/AV04DettaglioRiscossione.png
   :align: center
   :name: DettealioRiscossioni

   Dettaglio Riscossione
