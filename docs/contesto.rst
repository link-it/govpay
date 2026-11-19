.. _govpay_contesto:

========
Contesto
========

Il contesto in cui si colloca GovPay è quello della riscossione dei
tributi da parte degli enti pubblici. Ciascun ente, che amministra nel
proprio dominio applicativo le pendenze dei cittadini ed i relativi
pagamenti, può avvalersi del servizio di mediazione offerto dal Nodo dei
Pagamenti (pagoPA) per interagire con i PSP secondo una piattaforma paritetica e
garantita da una governance pubblica.

In tale scenario ciascun ente deve predisporre l'ambiente tecnologico
per far dialogare i propri sistemi, portale e sistema informativo per la
gestione dei pagamenti, con il Nodo dei Pagamenti (pagoPA).

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
-  **Componenti GovPay**: servizi distribuiti separatamente dal core,
   ciascuno con il proprio rilascio e con configurazione ed esecuzione
   descritte nel README del rispettivo repository (vedi
   :ref:`inst_componenti_aggiuntivi`):

   -  `govpay-aca-batch <https://github.com/link-it/govpay-aca-batch>`__:
      pubblicazione delle posizioni debitorie nell'Archivio Centralizzato
      degli Avvisi (ACA) di pagoPA tramite il servizio GPD;
   -  `govpay-rt-batch <https://github.com/link-it/govpay-rt-batch>`__:
      recupero da pagoPA delle ricevute di pagamento mancanti;
   -  `govpay-fdr-batch <https://github.com/link-it/govpay-fdr-batch>`__:
      acquisizione dei flussi di rendicontazione da pagoPA;
   -  `govpay-iban-batch <https://github.com/link-it/govpay-iban-batch>`__:
      confronto degli IBAN e dell'anagrafica degli enti registrati su
      pagoPA con quelli censiti in GovPay;
   -  `govpay-notify-batch <https://github.com/link-it/govpay-notify-batch>`__:
      notifica alle applicazioni dell'ente delle rendicontazioni senza
      pagamento e, se abilitata, delle ricevute;
   -  `govpay-maggioli-jppa <https://github.com/link-it/govpay-maggioli-jppa>`__:
      notifica delle ricevute di pagamento a un gateway pagamenti JPPA;
   -  `govpay-console <https://github.com/link-it/govpay-console>`__ e
      `govpay-console-api <https://github.com/link-it/govpay-console-api>`__:
      nuova console di backoffice e relativo servizio REST;
   -  `govpay-gde-api <https://github.com/link-it/govpay-gde-api>`__:
      registrazione e consultazione del Giornale degli Eventi;
   -  `govpay-stampe-api <https://github.com/link-it/govpay-stampe-api>`__:
      generazione in PDF degli avvisi e delle ricevute di pagamento;
   -  `govpay-portal <https://github.com/link-it/govpay-portal>`__ e
      `govpay-portal-api <https://github.com/link-it/govpay-portal-api>`__:
      portale dei pagamenti per il cittadino e relativo servizio REST.

-  **PSP**: Prestatore di Servizi di Pagamento, soggetto abilitato alla
   riscossione dei pagamenti ed emissione di relativa ricevuta
   elettronica aderente alla piattaforma pagoPA.
-  **pagoPA - Nodo dei Pagamenti**: la piattaforma gestita da PagoPA
   S.p.A. che intermedia i PSP.
-  **pagoPA - Checkout**: il servizio pagoPA che consente al debitore di
   scegliere il PSP e pagare online. In alternativa il debitore può
   pagare l'avviso presso i canali fisici e digitali dei PSP (sportelli,
   home banking, app), utilizzando il codice avviso.

