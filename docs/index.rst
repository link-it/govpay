.. _Govpay:

GovPay
======

GovPay è una piattaforma open source (GPL v3), che implementa il protocollo di dialogo tra Enti, Intermediari o Partner Tecnologici con il Nodo dei Pagamementi del progetto pagoPA.

GovPay gestisce in autonomia la comunicazione con il Nodo dei Pagamenti, sollevando da questo compito gli applicativi verso i quali sono esposte apposite API di integrazione; nel contempo supporta la gestione dell’intero ciclo di vita della Posizione Debitoria e dell'Archivio dei Pagamenti in Attesa.

GovPay, in qualità di nodo accentratore dei flussi pagoPA dei domini gestiti, consente l'accesso agli operatori dell'ente per la configurazione e il monitoraggio tramite un cruscotto di gestione web-based.

GovPay supporta anche la fase di riconciliazione dei versamenti ricevuti dalla Banca Tesoriera con le pendenze che hanno originato i relativi pagamenti.

La documentazione di GovPay consente agli utenti di affrontare le diverse fasi del ciclo di vita del prodotto ed è suddivisa nelle seguenti sezioni:

- :ref:`govpay_contesto` - Un inquadramento generale del contesto di attuazione

- :ref:`govpay_installazione` - Il processo di installazione e dispiegamento del prodotto nell'ambiente dell'ente

- :ref:`govpay_console` - Le funzionalità disponibili nel cruscotto grafico govpayConsole, ripartite in:

    * :ref:`govpay_configurazione` - Operazioni di configurazione del prodotto a carico degli amministratori del sistema

    * :ref:`govpay_conduzione` - Gestione delle pendenze, monitoraggio e riconciliazione

- :ref:`govpay_integrazione` - La documentazione tecnica delle interfacce applicative (API) per l'integrazione dei sistemi verticali in adozione nell'ambiente tecnologico dell'ente

- :ref:`govpay_scenari` - Una presentazione degli scenari tipici per l'utilizzo di GovPay

- :ref:`govpay_howto` - Una raccolta di best practices per affrontare problematiche di utilizzo comuni

.. toctree::
   :maxdepth: 2
   :hidden:

   Contesto Generale <contesto>

.. toctree::
   :maxdepth: 2
   :hidden:

   installazione/index

.. toctree::
   :maxdepth: 2
   :hidden:

   cruscotto/index

.. toctree::
   :maxdepth: 2
   :hidden:

   integrazione/index

.. toctree::
   :maxdepth: 2
   :hidden:

   scenari/index

.. toctree::
   :maxdepth: 2
   :hidden:

   howto/index
