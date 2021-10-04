.. _howto_nexi:

Pagamento con POS Nexi
======================

Il modello di integrazione richiesto da NEXI per la realizzazione di pagamenti con POS nel circuito
pagoPA prevede che, a seguito di un pagamento tramite POS avvenuto con successo, l'Ente Creditore 
trasmetta una RPT avente precompilati i parametri **identificativoPSP**, 
**identificativoIntermediarioPSP** e **identificativoCanale**. 

Per ottenere questo risultato è sufficiente utilizzare le consuete API di Pagamento valorizzando 
gli omonimi parametri in query string secondo le indicazioni del PSP. 
Il pagamento cosi avviato sarà inoltrato al PSP senza ulteriori interazioni che si occuperà di
esitare positivamente la transazione. In caso di insuccesso, sia questo dovuto a errori di comunicazione 
o di errata gestione da parte del PSP, sarà compito dell'applicativo chiamante valutare la ripetizione 
della richiesta.
