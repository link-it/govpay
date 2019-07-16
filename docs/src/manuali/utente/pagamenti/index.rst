.. _utente_pagamenti:

Pagamenti
=========

La sezione "Pagamenti" è dedicata alla consultazione delle operazioni di pagamento che sono state richieste a Govpay in seguito all'interazione tra l'utente pagatore e il portale dei servizi di pagamento dell'ente creditore.
Si noti come, all'interno del sistema, il diagramma di stato dei pagamenti sia il seguente:





L'area di ricerca è composta dai seguenti elementi:

-  Sulla sinistra è presente il form per impostare i criteri di filtro
   sui pagamenti che si vuole consultare.
-  Sulla destra è presente l'elenco dei pagamenti che corrispondono ai
   criteri di filtro impostati. Di ciascun elemento della lista è
   visualizzato il titolo che corrisponde a quello di una delle pendenze
   comprese nel pagamento e la dicitura "e altre X pendenze" nel caso in
   cui il pagamento sia composto da un carrello di pendenze di numero
   superiore a 1. Oltre al titolo, identificano un elemento della lista
   anche l'importo complessivo, lo stato e la data.

.. figure:: ../_figure_utente/100002010000039A0000020AC2D2B89C7F255727.png
   :alt: Figura 6: Sezione per la consultazione dei pagamenti
   :width: 17cm
   :height: 9.624cm

   Figura 6: Sezione per la consultazione dei pagamenti

Dopo aver effettuato una ricerca è possibile ottenere un CSV di
esportazione relativo all'elenco dei pagamenti che soddisfano i criteri
di ricerca forniti. L'esportazione dell'elenco si effettua selezionando
il collegamento "Esporta" che compare sul menu a discesa azionato con
l'icona in alto a destra nella pagina. Il file prodotto con
l'esportazione è un tracciato CSV in cui ciascun record contiene i
principali dati identificativi di ciascun pagamento.

Selezionando uno degli elementi presenti in elenco si procede alla
visualizzazione del dettaglio del pagamento.

Dettaglio Pagamento
-------------------

La pagina di dettaglio del pagamento comprende:

-  sezione di riepilogo dei dati che caratterizzano l'operazione di
   pagamento in questione (banca, importo, tipo di pagamento, ...)
-  sezione che elenca le pendenze che compongono il carrello associato
   all'operazione di pagamento. Per ciascuna pendenza in elenco sono
   visualizzati i dati identificativi comprensivi di singolo importo e
   stato di avanzamento.

.. figure:: ../_figure_utente/100002010000036F000002142A4826D4FD8E182F.png
   :alt: Figura 7: Dettaglio del pagamento
   :width: 17cm
   :height: 10.289cm

   Figura 7: Dettaglio del pagamento

In testa, sulla destra, è presente un menu a discesa che consente, nel
caso si possiedano le necessarie autorizzazioni, di effettuare
l'operazione *Esporta Pendenza* che consente di scaricare un archivio
ZIP che contiene:

-  RPT e RT, in formato XML, associate all'operazione di pagamento
-  Il file PDF della ricevuta telematica
