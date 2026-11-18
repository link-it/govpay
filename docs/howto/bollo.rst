.. _howto_bollo:

Marca da Bollo Telematica
==========================

La piattaforma pagoPA supporta la specifica @e.bollo per il pagamento della Marca da Bollo Telematica. In GovPay
questo servizio si realizza caricando una pendenza che contiene una voce di tipo bollo (ad esempio con l'operazione
*PUT /pendenze/{idA2A}/{idPendenza}* delle API Pendenze v2) e facendola pagare dal portale dei pagamenti,
come un qualsiasi pagamento ad iniziativa ente tramite il Checkout di pagoPA.

Configurazione
~~~~~~~~~~~~~~

Per autorizzare il pagamento della Marca da Bollo Telematica in GovPay è sufficiente abilitare, tra i *Tipi Entrata*
dell'Ente Creditore che ne richiede il pagamento, l'entrata *Marca da Bollo Telematica* (codice ``BOLLOT``), che GovPay
crea disabilitata alla registrazione di un ente intermediato (vedi :ref:`govpay_configurazione_enti`).

Il bollo viene trasmesso a pagoPA solo con la primitiva *paGetPaymentV2*: la stazione dell'ente deve quindi essere
configurata su pagoPA per operare con la versione 2 dei servizi. Con la primitiva *paGetPayment* GovPay rifiuta
il pagamento di una pendenza che contiene una marca da bollo.

Esecuzione del pagamento
~~~~~~~~~~~~~~~~~~~~~~~~

Per predisporre il pagamento di una Marca da Bollo Telematica è sufficiente valorizzare opportunamente
una delle voci della pendenza dell'elemento **voci**:

.. code-block:: json
    :caption: Esempio di pagamento Imposta di bollo
    
    {
      "voci":
      [
         {
         "idVocePendenza": "12345_1",
         "importo": 16.00,
         "descrizione": "MBT per certificato anagrafico prot 123456",
         "tipoBollo": "01",
         "hashDocumento": "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=",
         "provinciaResidenza": "RO"
         }
      ]
    }

Di seguito alcune informazioni aggiuntive per la valorizzazione dei campi:

  - **importo**: importo della marca da bollo. Al momento della scrittura, pagoPA supporta solo il taglio da 16€
  - **tipoBollo**: tipologia del bollo digitale; nelle API Pendenze v2 l'unico valore ammesso è ``01`` (imposta di bollo),
    mentre nelle API Pendenze v1 e nelle API Backoffice il valore è ``Imposta di bollo``
  - **hashDocumento**: da specifiche pagoPA "Contiene l’impronta informatica (digest) del documento informatico cui è associata la marca da bollo digitale. L’algoritmo di hash da utilizzare è SHA-256. La stringa di 256 bit (32 ottetti) risultato di tale algoritmo deve essere convertito in base64"
  - **provinciaResidenza**: da specifiche pagoPA "Sigla automobilistica della provincia di residenza del soggetto pagatore."

In caso di successo, la ricevuta inviata da pagoPA riporta, per la voce di bollo, il tracciato XML della Marca da Bollo acquistata,
contenente l'impronta del documento associato firmata digitalmente per eventuali verifiche. Nelle API GovPay la riscossione
della voce è di tipo ``MBT`` e il tracciato è riportato nel suo **allegato**.

.. code-block:: xml
    :caption: Esempio di Marca da Bollo
    
	<?xml version="1.0" encoding="UTF-8"?>
	<marcaDaBollo xmlns="http://www.agenziaentrate.gov.it/2014/MarcaDaBollo" xmlns:ns2="http://www.w3.org/2000/09/xmldsig#">
	   <PSP>
	      <CodiceFiscale>99999999999</CodiceFiscale>
	      <Denominazione>Banco di Ponzi S.p.A.</Denominazione>
	   </PSP>
	   <IUBD>5079389115013</IUBD>
	   <OraAcquisto>2021-07-27T11:48:02+0200</OraAcquisto>
	   <Importo>16.0</Importo>
	   <TipoBollo>01</TipoBollo>
	   <ImprontaDocumento>
	      <DigestMethod Algorithm="http://www.w3.org/2001/04/xmlenc#sha256" />
	      <ns2:DigestValue>a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=</ns2:DigestValue>
	   </ImprontaDocumento>
	   <Signature xmlns="http://www.w3.org/2000/09/xmldsig#">
	      ....
	   </Signature>
	</marcaDaBollo>

Informazioni aggiuntive
~~~~~~~~~~~~~~~~~~~~~~~

Gli importi riscossi per il pagamento delle Marche da Bollo non saranno accreditati su un conto
dell'Ente Creditore, ma su un conto dell'Agenzia delle Entrate pertanto non saranno soggetti a riconciliazione.

GovPay non produce l'Avviso pagoPA per le pendenze che contengono una Marca da Bollo Telematica: la stampa
dell'avviso viene rifiutata e queste pendenze vanno pagate dal portale dei pagamenti.

