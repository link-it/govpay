.. _utente_manutenzione:

Manutenzione
============

Accedendo alla sezione *Manutenzione* si ha la possibilità di effettuare
in modalità immediata le operazioni solitamente effettuate in
modalità temporizzata dagli schedulatori interni di GovPay.

Le operazioni disponibili sono:

-  *Acquisisci Rendicontazioni*: avvia manualmente il processo di acquisizione dei
   flussi di rendicontazione. Il processo è normalmente eseguito automaticamente ogni 
   quattro ore. 
-  *Recupera pagamenti*: avvia manualmente il processo di recupero dei
   pagamenti per i quali non è stata acquisita la ricevuta
   telematica nei tempi consueti. Il processo viene normalmente eseguito automaticamente
   ogni ora. 
-  *Resetta la cache*: svuota la cache delle configurazioni per rendere immediatamente
   operative le eventuali modifiche effettuate dall'operatore. La cache viene normalmente
   svuotata ogni ora. 

Data la migrazione verso il modello unico di pagamento che non prevede le funzionalità di recupero pagamenti, la voce *Recupera Pagamenti* è nascosta di default.
Per abilitarla si deve intervenire sulla seguente proprietà:

.. code-block:: javascript
   :linenos:

   global.GovPayConfig = {
       MANUTENZIONE: {
   	  RECUPERO_PAGAMENTI: {
   	    ENABLED: true // abilita la funzione di recupero pagamenti
   	  }
   	}
   };
