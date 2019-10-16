.. _govpay_contesto:

========
Contesto
========

Il contesto in cui si colloca GovPay è quello della riscossione dei
tributi da parte degli enti pubblici. Ciascun ente, che amministra nel
proprio dominio applicativo le pendenze dei cittadini ed i relativi
pagamenti, può avvalersi del servizio di mediazione offerto dal "Nodo
SPC" per interagire con i PSP secondo una piattaforma paritetica e
garantita da una governance pubblica.

In tale scenario ciascun ente deve predisporre l'ambiente tecnologico
per far dialogare i propri sistemi, portale e sistema informativo per la
gestione dei pagamenti, con il Nodo SPC.

   .. figure:: _images/GovPay_scenario_principale.jpg
    :scale: 50%
    :align: center
    :name: VisioneInsieme_fig

    GovPay: visione d'insieme

Come illustrato in :numref:`VisioneInsieme_fig`, lo scenario complessivo si compone dei seguenti attori/elementi:

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

