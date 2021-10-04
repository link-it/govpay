.. _howto_nexi:

Pagamento con POS Nexi
======================

Il modello di integrazione proposto da NEXI per la realizzazione di pagamenti con POS nel circuito
pagoPA prevede che, a seguito di un pagamento tramite POS avvenuto con successo, l'Ente Creditore 
trasmetta una RPT avente precompilati i parametri **identificativoPSP**, 
**identificativoIntermediarioPSP** e **identificativoCanale**. Per ottenere questo risultato e' sufficiente 
utilizzare le consuete API di Pagamento valorizzando gli omonimi parametri in query string
secondo le indicazioni del PSP.

Nel caso di successo, il pagamento cosi avviato sarà gestito dal PSP restituendo una RT positiva. In caso di 
insuccesso, sia questo dovuto a errori di comunicazione o di errata gestione da parte del PSP, sarà
onere dell'applicativo chiamante ripetere la richiesta.
