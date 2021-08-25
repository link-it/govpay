.. _howto_bollo:

Marca da Bollo Telematica
==========================

La piattaforma pagoPA supporta la specifica @e.bollo per il pagamento della Marca da Bollo Telematica. In GovPay
questo servizio è realizzabile in maniera analoga ad un qualsiasi pagamento ad iniziativa ente, compilando opportunamente 
la richiesta di pagamento.

Configurazione
~~~~~~~~~~~~~~

Per autorizzare il pagamento della Marca da Bollo Telematica in GovPay è sufficiente abilitare il tipo tributo "Marca da Bollo" nella
configurazione dell'Ente Creditore che ne richiede il pagamento.

Esecuzione del pagamento
~~~~~~~~~~~~~~~~~~~~~~~~

Per avviare il pagamento di una Marca da Bollo Telematica è sufficiente valorizzare opportunamente
una delle voci della pendenza dell'elemento **voci**:

.. code-block:: json
    :caption: Esempio di pagamento Imposta di bollo
    
    {
      ...
      "voci":
      [
         {
         "idVocePendenza": "12345_1",
         "importo": 16.00,
         "descrizione": "MBT per certificato anagrafico prot 123456",
         "tipoBollo": "Imposta di bollo",
         "hashDocumento": "a/CWqtFtCEyA/ymBySahGSaqKMiak5mlX3BoX0jupy8=",
         "provinciaResidenza": "RO"
         }
      ]
      ...
    }

Di seguito alcune informazioni aggiuntive per la valorizzazione dei campi:

  - **importo**: importo della marca da bollo. Al momento della scrittura, pagoPA supporta solo il taglio da 16€
  - **hashDocumento**: da specifiche pagoPA "Contiene l’impronta informatica (digest) del documento informatico cui è associata la marca da bollo digitale. L’algoritmo di hash da utilizzare è SHA-256. La stringa di 256 bit (32 ottetti) risultato di tale algoritmo deve essere convertito in base64"
  - **provinciaResidenza**: da specifiche pagoPA "Sigla automobilistica della provincia di residenza del soggetto pagatore."

In caso di successo, la Ricevuta Telematica inviata da pagoPA ha allegato il tracciato XML della Marca da Bollo acquistata,
contenente l'impronta del documento associato firmata digitalmente per eventuali verifiche.

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

Al momento della scrittura nessun PSP supporta il pagamento ad iniziativa PSP della Marca da Bollo,
pertanto un Avviso pagoPA associato ad una Marca da Bollo Telematica risulterà non pagabile. 

