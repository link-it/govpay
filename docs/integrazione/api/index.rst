.. _integrazione_api:

API di Integrazione
===================

La realizzazione degli scenari descritti nei capitoli successivi,
prevede l'utilizzo di servizi di integrazione, esposti da GovPay come
API REST, finalizzati all'integrazione dei sistemi verticali dell'Ente
Creditore. Nella tabella seguente sono indicati sia gli indirizzi di
base di erogazione di ciascun servizio, sia il riferimento alla
definizione OpenAPI per la documentazione di dettaglio.

Ogni API è un war distinto e il base path è il nome del war (ad esempio
``/govpay-api-pendenze``).

.. list-table::
   :widths: 20 30 50
   :header-rows: 1

   * - API
     - Base path
     - Specifica
   * - Pendenze
     - /govpay-api-pendenze
     - `v1 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-pendenze/src/main/webapp/v1/govpay-api-pendenze-v1.yaml>`__ `v2 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml>`__
   * - Ragioneria
     - /govpay-api-ragioneria
     - `v1 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-ragioneria/src/main/webapp/v1/govpay-api-ragioneria-v1.yaml>`__ `v2 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml>`__ `v3 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-ragioneria/src/main/webapp/v3/govpay-api-ragioneria.yaml>`__
   * - Backoffice
     - /govpay-api-backoffice
     - `v1 <https://github.com/link-it/govpay/blob/3.10.x/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml>`__
   * - pagoPA
     - /govpay-api-pagopa
     - servizi SOAP del modello unico: `WSDL <https://github.com/link-it/govpay/blob/3.10.x/jars/pagopa-beans/src/main/resources/wsdl/PaPerNodoPagamentoPsp.wsdl>`__
   * - Ente Creditore
     - definita dall'ente creditore
     - `v1 <https://github.com/link-it/govpay/blob/3.10.x/jars/client-api-ente/src/main/resources/govpay-api-ec-v1.yaml>`__ `v2 <https://github.com/link-it/govpay/blob/3.10.x/jars/client-api-ente/src/main/resources/v2/govpay-api-ec.yaml>`__

L'API pagoPA è invocata dal Nodo dei Pagamenti e non dai sistemi
dell'Ente. L'API Ente Creditore è invece esposta dall'Ente e invocata da
GovPay e dai suoi componenti: la versione 2 comprende anche la notifica
delle rendicontazioni senza pagamento (``POST /rendicontazioni``), usata
da govpay-notify-batch.

I componenti distribuiti separatamente (ad esempio govpay-portal-api,
govpay-gde-api, govpay-stampe-api e le API REST di avvio dei batch)
espongono API proprie, descritte nel README di ciascun repository (vedi
:ref:`inst_componenti_aggiuntivi`).

.. toctree::
        :maxdepth: 2
	:hidden:
        :caption: Argomenti trattati:

	autenticazione/index
        autorizzazione/index
