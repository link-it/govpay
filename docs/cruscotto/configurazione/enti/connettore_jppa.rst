.. _govpay_configurazione_connettori_jppa:

Connettore per l'esportazione dei pagamenti verso Maggioli (JPPA)
-----------------------------------------------------------------

Il connettore consente di interfacciarsi verso la piattaforma PagoPA Maggioli ed esportare i pagamenti gestiti da GovPay in un formato compatibile con il protocollo JPPA.

Il protocollo prevede due fasi:

- GovPay invia una notifica al servizio JPPA per ogni ricevuta positiva acquisita da pagoPA.
- Successivamente il servizio JPPA acquisisce le ricevute di pagamento dal servizio *RecuperaRT* esposto da GovPay.

Tramite il servizio *RecuperaRT*, vengono acquisiti i tracciati originali delle Ricevute secondo la specifica del servizio JPPA. Tali tracciati sono arricchiti con le informazioni contabili o di accertamento relativi all'importo riscosso per ogni singola voce pendenza, riportando le informazioni delle quote indicate nell'elemento *contabilita* come segue:

.. csv-table:: *Mapping dati contabili*
   :header: "Campo", "Valore"
   :widths: 40,60

   "Capitolo bilancio", "vocePendenza.contabilita.quote[].capitolo"
   "Anno", "vocePendenza.contabilita.quote[].annoEsercizio"
   "Importo", "vocePendenza.contabilita.quote[].importo"
   "Descrizione", "vocePendenza.contabilita.quote[].descrizione"   

Il servizio *RecuperaRT* è disponibile alla URL: https://host-gp/govpay/backend/api/jppapdp/JppaPdpExternalFacetService. In maniera simile alle altre API messe a disposizione da GovPay, il servizio è fruibile previa autenticazione secondo la modalità individuata dalla configurazione Spring Security, per default con SSL Client Auth.

La spedizione delle notifiche verso la piattaforma Maggioli viene eseguita quotidianamente alle 3 di mattina ed al termine delle spedizioni viene inviato tramite email un report dell'attività in formato CSV.

.. figure:: ../../_images/48ConnettoreMaggioliJPPA.png
   :align: center
   :name: 48ConnettoreMaggioliJPPA

   Configurazione del Connettore Maggioli (JPPA)

.. csv-table:: *Parametri di configurazione generale*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Versione CSV", "Versione del tracciato di esito"
   "Modalità di consegna", "Canale di trasmissione del CSV verso l'ente"
   "Email", "Specifica la lista degli indirizzi destinatari separati da virgola"
   "Oggetto", "Oggetto della email"
   "Invia come allegato", "Indica se il tracciato deve essere inserito in allegato o riferito con un link."
   "Base URL link download", "|Indica la base URL del link di download.
   |Deve essere una URL valida per la risorsa */tracciatiNotificaPagamenti* delle API di Backoffice.
   |Ecco un esempio di Base URL: https://host-gp/govpay/backend/api/backoffice/rs/basic/v1/tracciatiNotificaPagamenti"

.. csv-table:: *Parametri di configurazione servizio acquisizione ricevute*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Principal", "Principal autenticato della chiamata da Maggioli"

.. csv-table:: *Parametri di configurazione servizio notifica pagamenti*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Tipi pendenza", "Elenco dei tipi pendenza oggetto di esportazione"
   "URL", "URL dei servizi esposti da Maggioli"
   "Versione API", "Versione dei servizi da utilizzare"
   "Tipo Autenticazione", "Tipo di autenticazione da utilizzare (Nessuna/HTTPBasic/SSL)"


Tracciato esiti spedizioni versione 1.0
~~~~~~~~~~~~

Per ciascuna notifica di pagamento inviata viene aggiunto un record con l'esito dell'operazione:

.. csv-table:: *Valori di esportazione*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "idDominio","Identificativo Ente Creditore"
   "iuv","Identificativo Univoco Pagamento"
   "cpp","Codice Contesto Pagamento/ReceiptID"
   "esito","OK|ERROR"
   "descrizioneEsito", "Descrizione errore spedizione"
