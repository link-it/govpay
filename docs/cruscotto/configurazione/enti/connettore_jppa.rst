.. _govpay_configurazione_connettori_jppa:

Connettore per l'esportazione dei pagamenti verso Maggioli (JPPA)
-----------------------------------------------------------------

Il servizio e' al momento rilasciato in versione beta.

Questo connettore consente di esportare i dati dei pagamenti gestiti da GovPay in formato compatibile con la piattaforma PagoPA Maggioli. Il batch di esportazione viene eseguito quotidianamente alle 3 di mattina. 

GovPay seleziona le notifiche di pagamento da inviare verso Maggioli attraverso l'operazione InvioEsitoPagamento e raccoglie il risultato delle spedizioni all'interno di un tracciato in formato csv.

Infine GovPay espone un servizio che implementa l'operazione RecuperaRT utilizzato dalla piattaforma Maggioli per recuperare il dettaglio di una ricevuta.

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
   "Email", "Elenco degli indirizzi email a cui spedire il tracciato di esito delle spedizioni"
   "Oggetto", "Oggetto della email"
   "Invia come allegato", "Indica se il tracciato viene spedito come allegato alla mail"
   "Base URL link download", "Indirizzo dal quale scaricare il tracciato di esito"

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

