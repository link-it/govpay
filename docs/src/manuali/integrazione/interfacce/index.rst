.. _integrazione_interfacce:


Gestione automatica delle interfacce
====================================

Una delle caratteristiche più interessanti di GovPay è quella di poter essere personalizzato tramite linguaggi formali atti a descrivere le interefacce verso il debitore: è possibile quindi definire le interfacce di pagamento (e anche quelle di inoltro, ad esempio, via mail della ricevuta telematica) attraverso file di testo con sintassi standard (Angular e Freemarker).
Nel seguito della sezione si affronterà un caso pratico di definizione di intefaccia di una pendenza caricata su un Ente Creditore.




Personalizzazione del tipo pendenza
-----------------------------------

.. figure:: ../_images/INT06_CaratteristichePendenzaConInterfacceAutomatiche.png
   :align: center
   :name: Interfaccepersonalizzabilineltipopendenza

   Interfacce personalizzabili attraverso script nel Tipo Pendenza



I principali attori interni all'Ente Creditore
----------------------------------------------




.. csv-table:: Varianti dei casi d'uso
  :header: "Sintesi", "Descrizione"
  :widths: 30,70
  
  "Pagamenti ad iniziativa Ente", "Scenari d’utilizzo in cui il soggetto debitore utilizza il portale dei pagamenti dell’ente per effettuare uno o più pagamenti"
  "Pagamenti ad iniziativa PSP", "Scenari d’utilizzo in cui l’utente effettua uno o più pagamenti presso il PSP tramite gli avvisi di pagamento"  
  "Riconciliazione dei pagamenti", "Scenari di utilizzo di GovPay che coinvolgono i sistemi contabili dell’ente, responsabili della riconciliazione dei pagamenti realizzati da pagoPA con le entrate in tesoreria"  
