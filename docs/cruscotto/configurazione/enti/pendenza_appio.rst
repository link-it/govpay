.. _govpay_configurazione_enti_appio:

Configurazione dell'avvisatura al cittadino via AppIO
-----------------------------------------------------

Nel caso in cui l'Ente abbia correttamente eseguito l'adesione al backoffice IO e configurato
tale servizio nelle impostazioni (vedi :ref:`utente_impostazioni`), è possibile
predisporre delle comunicazioni automatiche verso il cittadino tramite l'AppIO. 
Queste comunicazioni sono ovviamente attivate se il cittadino ha installato
l'AppIO su uno dei suoi dispositivi personali.

I messaggi sono spediti da un task del core di GovPay, abilitato nel file ``govpay.properties`` dalla proprietà
``it.govpay.batch.spedizioneNotificheAppIO.enabled`` (attiva per default).

.. figure:: ../../_images/31TipoPendenza_Appio.png
   :align: center
   :name: 31TipoPendenza_Appio

   Avvisatura via App IO

Per attivare il servizio è necessario specificare l'``ApiKey`` fornita dal
backoffice IO per il servizio oggetto di pagamento.

Notifica avviso di pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

La notifica di avviso di pagamento viene inviata quando viene creata una nuova pendenza oppure alla data indicata nel campo ``dataNotificaAvviso`` se la posizione sarà ancora pagabile.

.. csv-table:: *Notifica avviso*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipo template", "Motore del template di trasformazione: Nessuno o Freemarker"
   "Template oggetto", "Template di trasformazione per la produzione del testo usato come oggetto del messaggio"
   "Template messaggio", "Template di trasformazione per la produzione del Markdown usato come corpo del messaggio"
   "Abilitato", "Indica se il servizio di notifica deve o meno essere operativo"

Promemoria scadenza pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

All'approssimarsi della data di scadenza indicata sull'avviso, il sistema può inviare un messaggio App IO di promemoria. Il promemoria
viene inviato nella data indicata nel campo ``dataPromemoriaScadenza`` o qualche giorno prima della scadenza se la pendenza risulta
ancora pagabile.

.. csv-table:: *Promemoria scadenza*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Giorni di preavviso", "Numero di giorni dalla scadenza in cui comunicare il promemoria"
   "Tipo template", "Motore del template di trasformazione: Nessuno o Freemarker"
   "Template oggetto", "Template di trasformazione per la produzione del testo usato come oggetto del messaggio"
   "Template messaggio", "Template di trasformazione per la produzione del Markdown usato come corpo del messaggio"
   "Abilitato", "Indica se il servizio di notifica deve o meno essere operativo"

Notifica ricevuta pagamento
~~~~~~~~~~~~~~~~~~~~~~~~~~~

A fronte di un tentativo di pagamento il sistema può inviare al debitore ed al pagatore, qualora diversi, un messaggio
di notifica dell'esito:

.. csv-table:: *Notifica ricevuta*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipo template", "Motore del template di trasformazione: Nessuno o Freemarker"
   "Template oggetto", "Template di trasformazione per la produzione del testo usato come oggetto del messaggio"
   "Template messaggio", "Template di trasformazione per la produzione del Markdown usato come corpo del messaggio"
   "Abilitato", "Indica se il servizio di notifica deve o meno essere operativo"
   "Notifica solo eseguiti", "Indica se inviare la notifica solo nel caso di transazioni avvenute con successo"