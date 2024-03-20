.. _utente_impostazioni:

Impostazioni
============

La sezione *Impostazioni* consente di configurare la piattaforma GovPay. 

Area iniziale
-------------

.. figure:: ../../_images/IMP01AreaGenerale.png
   :align: center
   :name: AreaInizialeImpostazioni

   Area iniziale Impostazioni

Il sistema consente di configurare le seguenti componenti:

*  Giornale degli Eventi
*  Comunicazioni Mail
*  Comunicazioni AppIO
*  Parser Tracciati CSV
*  Protezioni API Pubbliche

Giornale degli Eventi
---------------------

Il sistema consente di selezionare per quali API attivare la registrazione degli eventi, offrendo la possibilità di registrare le operazioni di lettura e scrittura.

Inoltre, è possibile selezionare se memorizzare anche i messaggi scambiati durante tali operazioni.

Infine, per ogni categoria si può scegliere se memorizzare l'evento sempre, mai o solo in caso di operazione conclusa con errore.

.. figure:: ../../_images/IMP02GDE.png
   :align: center
   :name: ImpostazioniGDE

   Impostazioni Giornale degli Eventi


Comunicazioni Mail
------------------

In questa sezione si configurano le comunicazioni che GovPay manda via mail, nel primo tab si configurano i parametri per la connessione al server di posta.

.. figure:: ../../_images/IMP03ComunicazioniMail01.png
   :align: center
   :name: ImpostazioniMail01

   Impostazioni Comunicazioni Mail


Più in basso nella schermata si trova la sezione per la configurazione dell'autenticazione SSL.

.. figure:: ../../_images/IMP04ComunicazioniMail02.png
   :align: center
   :name: ImpostazioniMail02

   Impostazioni Comunicazioni Mail Connessione SSL

Nei tab successivi si configurano le singole comunicazioni, definendo i template freemarker da utilizzare per la personalizzazione dei messaggi da inviare.

- Notifica Avviso

Per la notifica di un avviso si possono indicare i template per oggetto e messaggio della mail e se inserire il pdf dell'avviso di pagamento.

.. figure:: ../../_images/IMP05ComunicazioniMail03.png
   :align: center
   :name: NotificaAvviso

   Notifica Avviso

- Promemoria Scadenza

Per il promemoria scadenza si possono indicare i template per oggetto e messaggio della mail e i giorni di preavviso rispetto alla data di scadenza della pendenza.

.. figure:: ../../_images/IMP06ComunicazioniMail04.png
   :align: center
   :name: PromemoriaScadenza

   Promemoria Scadenza

- Notifica Ricevuta

Per la notifica di una ricevuta si possono indicare i template per oggetto e messaggio della mail, se inserire il pdf della ricevuta di pagamento e se notificare solo transazioni andate a buon fine.

.. figure:: ../../_images/IMP07ComunicazioniMail05.png
   :align: center
   :name: NotificaRicevuta

   Notifica Ricevuta


Comunicazioni AppIO
-------------------

In questa sezione si configurano le comunicazioni attraverso AppIO, nel primo tab si configurano i parametri per la connessione verso il BackendIO.

.. figure:: ../../_images/IMP08ComunicazioniAppIO01.png
   :align: center
   :name: ImpostazioniAppIO01

   Impostazioni Comunicazioni AppIO

Nei tab successivi si configurano le singole comunicazioni, definendo i template freemarker da utilizzare per la personalizzazione dei messaggi da inviare.

- Notifica Avviso

Per la notifica di un avviso si possono indicare i template per oggetto e messaggio della mail.

.. figure:: ../../_images/IMP09ComunicazioniAppIO02.png
   :align: center
   :name: NotificaAvviso

   Notifica Avviso

- Promemoria Scadenza

Per il promemoria scadenza si possono indicare i template per oggetto e messaggio della mail e i giorni di preavviso rispetto alla data di scadenza della pendenza.

.. figure:: ../../_images/IMP10ComunicazioniAppIO03.png
   :align: center
   :name: PromemoriaScadenza

   Promemoria Scadenza

- Notifica Ricevuta

Per la notifica di una ricevuta si possono indicare i template per oggetto e messaggio della mail e se notificare solo transazioni andate a buon fine.

.. figure:: ../../_images/IMP11ComunicazioniAppIO04.png
   :align: center
   :name: NotificaRicevuta

   Notifica Ricevuta


Parser Tracciati CSV
--------------------

In questa sezione si configura i template per la trasformazione dei tracciati di caricamento pendenze in formato CSV.

Si possono indicare:
- Template Caricamento: definisce la trasformazione dal singola riga del tracciato CSV in una pendenza in formato JSON delle API native.
- Template Esito: definisce la trasformazione del risultato del caricamento in una riga in formato CSV da inserire nel tracciato di esito.
- Linea Intestazione: definisce l'header da inserire nel tracciato di esito

.. figure:: ../../_images/IMP12ParserCSV.png
   :align: center
   :name: ImpostazioniParser01

   Impostazioni Parser CSV

Protezioni API Pubbliche
------------------------

In questa sezione si configura la protezione tramite Captcha delle risorse public messe a disposizione da GovPay ai portali di pagamento.

.. figure:: ../../_images/IMP13APIPubbliche.png
   :align: center
   :name: ImpostazioniAPIPubbliche

   Impostazioni API Pubbliche



