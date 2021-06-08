.. _govpay_configurazione:

==============
Configurazione
==============

La configurazione di Govpay è quella attività, visibile solo agli amministratori del sistema, che consente il censimento e la manutenzione delle entità logiche (operatori, ruoli, domini, abilitazioni e quant'altro) coinvolte nel processo di pagamento.

Le attività di configurazione vengono svolte tramite un'apposita sezione del cruscotto grafico :ref:`govpay_console` e sono necessarie alla messa in funzione dell'applicativo.
La lista degli oggetti che è possibile configurare comprende i seguenti elementi:


-  *Intermediari*: rappresentano le entità “Intermediario” o “Partner Tecnologico” censiti presso il Nodo dei Pagamenti scelti in fase di adesione dagli Enti Creditore per l'accesso al sistema pagoPA.
-  *Enti Creditori*: corrispondono agli enti creditori aderenti al sistema pagoPA.
-  *Tipi Pendenza*: rappresentano le esigenze dell'ente creditore dalle quali scaturiscono le tipologie di pagamenti che possono essere gestiti dal sistema (ad esempiotassa rifiuti, licenza di caccia, bollo auto e via dicendo).
-  *Applicazioni*: rappresentano i portali di pagamento e i gestionali delle posizioni debitorie degli enti Creditori integrati con GovPay tramite gli appositi servizi.
-  *Operatori*: sono le utenze del cruscotto di gestione GovPay.
-  *Ruoli*: rappresentano l'insieme delle autorizzazioni consentite sulle entità dati, da assegnarsi agli utenti del
   cruscotto.

.. note:: Nell'analisi delle funzionalità di configurazione, le immagini esemplificative mostrate mancheranno della
   sezione di sinistra (*Lista funzionalità*) al fine di agevolare la focalizzazione sulla parte important della funzionalità, ovvero il suo dettaglio posto a destra.


.. toctree::
   :caption: Argomenti trattati:
   :maxdepth: 2

   intermediari/index
   enti/index
   tipipendenze/index
   applicazioni/index
   operatori/index
   ruoli/index
