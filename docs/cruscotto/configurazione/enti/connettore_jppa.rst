.. _govpay_configurazione_connettori_jppa:

Connettore per l'esportazione dei pagamenti verso Maggioli (JPPA)
-----------------------------------------------------------------

Il servizio e' al momento rilasciato in versione beta.

Questo connettore consente di esportare i dati dei pagamenti gestiti da GovPay in formato compatibile con la piattaforma PagoPA Maggioli. Il batch di esportazione viene eseguito quotidianamente alle 3 di mattina. 

.. figure:: ../../_images/48ConnettoreMaggioliJPPA.png
   :align: center
   :name: 48ConnettoreMaggioliJPPA

   Configurazione del Connettore Maggioli (JPPA)

.. csv-table:: *Parametri di configurazione generale*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Versione CSV", "Versione del tracciato di esito"
   "Modalità di consegna", "Canale di trasmissione del CSV verso l'ente"
   "Tipi pendenza", "Elenco dei tipi pendenza oggetto di esportazione"
   "Email", "Specifica la lista degli indirizzi destinatari separati da virgola"
   "Oggetto", "Oggetto della email"
   "Invia come allegato", "Indica se il tracciato deve essere inserito in allegato o riferito con un link."
   "Base URL link download", "Indica la base URL del link di download. Deve puntare alla risorsa /tracciatiNotificaPagamenti delle API di backoffice"

.. csv-table:: *Parametri di configurazione servizio acquisizione ricevute*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "Principal", "Principal autenticato della chiamata da Maggioli"
   
.. csv-table:: *Parametri di configurazione servizio notifica pagamenti*
   :header: "Campo", "Descrizione"
   :widths: 40,60

   "URL", "URL dei servizi esposti da Maggioli"
   "Versione API", "Versione dei servizi da utilizzare"
   "Tipo Autenticazione", "Tipo di autenticazione da utilizzare (Nessuna/HTTPBasic/SSL)"


Il servizio di esportazione seleziona le transazioni di pagamento completate con successo ed invia le notifiche verso Maggioli attraverso l'operazione *InvioEsitoPagamento*, contestualmente raccoglie il risultato di ogni spedizione all'interno di un tracciato in formato csv.
Il tracciato verra' quindi inviato via email al termine dell'invio delle notifiche di pagamento.

Per completare il flusso operativo previsto, GovPay espone l'operazione *RecuperaRT* utilizzata dalla piattaforma Maggioli per recuperare il dettaglio di ogni ricevuta notificata nella fase precedente.


Versione 1.0
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

