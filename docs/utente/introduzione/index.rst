.. _utente_introduzione:

Introduzione
============

Questo documento ha lo scopo di fornire le istruzioni operative per procedere, dopo l'installazione di GovPay, alla configurazione e manutenzione dell'infrastruttura di pagamento.

Glossario
---------

.. glossary::

   NDP
      Nodo dei Pagamenti SPC. Piattaforma tecnologica per l'interconnessione e Phtteroperabilitit tra le Pubbliche Amministrazioni e i Prestatori di Servizi di Pagamento, di cui all'art. 5, comma 2 del CAD. architrave del sistema pagoPA PA Pubblica Amministrazione (Centrale e Locale).

   SPC
      Sistema Pubblico di Connettività: è una cornice nazionale di interoperabilità: definisce, cioè, le modalità preferenziali che i sistemi informativi delle pubbliche amministrazioni devono adottare per essere tra loro interoperabili.
      
   AgID
      Agenzia per l'Italia Digitale Ente istituito ai sensi del decreto legge n. 83 del 22 giugno 2012 convertito con legge n. 134 del 7 agosto 2012 (già DigitPA). Gestore del Nodo dei Pagamenti-SPC.

   RPT
      Richiesta di Pagamento Telematico Oggetto informatico inviato dall'Ente Creditore al Prestatore Servizi di Pagamento attraverso il Nodo dei Pagamenti-SPC al fine di richiedere l'esecuzione di un pagamento.
      
   RT
      Ricevuta Telematica Oggetto informatico inviato dal Prestatore Servizi di Pagamento all'Ente Creditore attraverso il Nodo dei Pagamenti-SPC in risposta ad una Richiesta di Pagamento Telematico effettuata da un Ente Creditore. 
      
   IUV
      Identificativo Unico Pagamento.
      
   CCP
      Codice Contesto Pagamento.   
      
   PSP
      Prestatori Servizi Pagamento.    



Storia delle modifiche del documento
------------------------------------

.. csv-table:: 
  :header: "Data","Versione", "Modifiche", "Note"
  :widths: 30,30,30,10
  
  "2019-07-17", "3.1.1","Stesura iniziale del documento", ""
  "2019-07-24", "3.1.2","Revisione del documento alla luce delle nuove funzionalità su Ruoli, Tipi Pendenza e Applicazioni", ""



Documentazione
--------------


.. csv-table:: 
  :header: "ID","Titolo", "Versione"
  :widths: 30,40,30
  
  "SANP", "Specifiche Attuative del Nodo dei Pagamenti-SPC", "v.2.2.4 – Luglio 2019"
  "SACIV", "Specifiche Attuative dei Codici Identificativi di Versamento, Riversamento e Rendicontazione", "v.1.3.1 – Gennaio 2018"
  "PEMP", "Pagamento Elettronico della Marca da Bollo digitale", "v.1.0 – Febbraio 2015"
  "MYBANK", "Transazioni MyBank attraverso il Nodo dei Pagamenti-SPC", "v.2.0 – Dicembre 2018"
  "GP-API", "GovPay – Manuale di Integrazione", "v.3.1 - Luglio 2019"
  "GP-INS", "GovPay – Manuale di Installazione", "v.3.1 - Luglio 2019"
  
  
Contesto
--------

Il contesto in cui si colloca GovPay è quello della riscossione dei
tributi da parte degli enti pubblici. Ciascun ente, che amministra nel
proprio dominio applicativo le pendenze dei cittadini ed i relativi
pagamenti, può avvalersi del servizio di mediazione offerto dal "Nodo
SPC" per interagire con i PSP secondo una piattaforma paritetica e
garantita da una governance pubblica. 

In tale scenario ciascun ente deve predisporre l'ambiente tecnologico
per far dialogare i propri sistemi, portale e sistema informativo per la
gestione dei pagamenti, con il Nodo SPC. GovPay si pone come mediatore
tra l'ambiente tecnologico dell'ente ed il Nodo SPC con l'obiettivo di
abbattere i tempi di realizzazione delle interfacce di colloquio tra
questi sistemi.

Vediamo quali sono gli attori che entrano in gioco nello scenario
complessivo:

-  **Soggetto Debitore** (nel seguito “Cittadino”): L’utilizzatore
   finale della piattaforma di pagamenti 
-  **Portale Pagamenti**: applicazione web offerta al cittadino con le
   funzionalità necessarie alla consultazione o predisposizione della
   propria posizione debitoria. 
-  **Gestionale Posizioni**: applicazioni dell'ente che gestiscono le
   posizioni debitorie dei cittadini.
-  **Sistema Amministrativo Contabile**: applicazioni dell'ente
   responsabili della riconciliazione delle riscossioni ricevute con i
   pagamenti di origine.
-  **GovPay**: gestore del protocollo di colloquio con il Nodo dei
   Pagamenti.
-  **GovPay Console**: applicazione web per la configurazione e il
   monitoraggio dell'operatività di GovPay. 
-  **PSP**: Prestatore di Servizi di Pagamento, soggetto abilitato alla
   riscossione dei pagamenti ed emissione di relativa ricevuta
   elettronica aderente alla piattaforma pagoPA.
-  **pagoPA - Nodo SPC**: la piattaforma AgID che intermedia i PSP.
-  **pagoPA - WISP**: il portale pagoPA che consente al debitore di
   selezionare il PSP per procedere con un pagamento.
   
Nelle sezioni successive si descrivono i passi necessari, una volta
terminata l'installazione di GovPay (si consulti il documento 
`GP-INS <#GPINS>`__ per i dettagli di installazione), per effettuare le
configurazioni necessarie alla messa in funzione dell'applicativo.
Infine verranno illustrate le funzionalità di monitoraggio dei pagamenti
per la conduzione ordinaria del sistema.
  
  
  
