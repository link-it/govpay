.. _utente_accesso:

Accesso al cruscotto e profilo
==============================

Una volta completata con successo la procedura di installazione
sarà possibile procedere con la configurazione accedendo al **Cruscotto
di Gestione** al seguente indirizzo:

http://<hostname>:<port>/backend/gui/backoffice

Dove al posto dei placeholder <hostname> e <port> dovranno essere
inseriti i riferimenti al proprio ambiente di installazione (nome host o
indirizzo IP e relativa porta).

Per l'accesso al cruscotto viene presentata la maschera per l'immissione delle credenziali. Si noti come ad ogni 
utente sia associato un *ruolo* che rappresenta l'insieme delle funzionalità che sono destinate all'utente stesso. Questo meccanismo, che verrà maggiormente dettagliato in seguito, permette di *ritagliare* in modo assolutamente generico il giusto profilo funzionale per tutte le classi di utente che componge la platea della piattaforma.

.. figure:: ../images/01PrimoAccesso.png
   :align: center

   Figura 1: Immissione delle credenziali
   
Dopo aver effettuato l’accesso, con le credenziali in proprio possesso,
si accede alla sezione di consultazione dei pagamenti, descritta più
avanti.

Menu di navigazione
-------------------

La colonna sinistra presente nell'interfaccia rappresenta il menu di
navigazione, con voci che possono variare in base ai ruoli e relative
autorizzazioni associate all'utenza autenticata.



L'area iniziale del menu di navigazione è composta dai seguenti
elementi:

-  Identificativo dell'utente in sessione.
-  Icona per la visualizzazione del profilo utente che mostra i dati
   anagrafici di dettaglio e le autorizzazioni possedute.
-  Icona per il Logout dal cruscotto.

A seguire sono elencate le sezioni del menu di navigazione, che possono
variare in base alle autorizzazioni possedute dall'utente in sessione.
Le sezioni del menu sono:

1. *Pendenze*: sezione di consultazione delle pendenze di pagamento in
   carico ai debitori.
2. *Pagamenti*: sezione di consultazione delle operazioni di pagamento
   effettuate dai debitori.
3. *Giornale degli eventi*: sezione di consultazione del Giornale Eventi
   previsto dalla specifica pagoPA.
4. *Configurazioni:* raccoglie gli strumenti per la consultazione,
   censimento e modifica delle entità alla base della configurazione del
   prodotto (Psp, Domini, Tributi, Applicazioni, ecc.).
5. *Funzioni Avanzate*: sezione dedicata alla consultazione di entità
   avanzate (rendicontazioni, riscossioni, ecc.).
6. *Manutenzione*: Accesso a funzionalità di manutenzione straordinaria.


