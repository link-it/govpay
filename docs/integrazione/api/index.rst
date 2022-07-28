.. _integrazione_api:

API di Integrazione
===================

La realizzazione degli scenari descritti nei capitoli successivi,
prevede l'utilizzo di servizi di integrazione, esposti da GovPay come
API REST, finalizzati all'integrazione dei sistemi verticali dell'Ente
Creditore. Nella tabella seguente sono indicati sia gli indirizzi di
base di erogazione di ciascun servizio, sia il riferimento alla
definizione OpenAPI per la documentazione di dettaglio.

.. list-table:: 
   :widths: 25 50 25
   :header-rows: 1

   * - API
     - Base path
     - OpenAPI
   * - Pagamento
     - /govpay/frontend/api/pagamento
     - `v1 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pagamento/src/main/webapp/v1/govpay-api-pagamento-v1.yaml&nocors>`_ `v2 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pagamento/src/main/webapp/v2/govpay-api-pagamento-v2.yaml&nocors>`_
   * - Pendenze
     - /govpay/backend/api/pendenze 
     - `v1 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pendenze/src/main/webapp/v1/govpay-api-pendenze-v1.yaml&nocors>`_ `v2 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml&nocors>`_ 
   * - Riconciliazione
     - /govpay/backend/api/ragioneria
     - `v1 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v1/govpay-api-ragioneria-v1.yaml&nocors>`_ `v2 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml&nocors>`_ `v3 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v3/govpay-api-ragioneria.yaml&nocors>`_
   * - Backoffice
     - /govpay/backend/api/backoffice
     - `v1 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml&nocors>`_
   * - Ente Creditore
     - definita dall'ente creditore
     - `v1 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/jars/client-api-ente/src/main/resources/govpay-api-ec-v1.yaml&nocors>`_ `v2 <https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/link-it/govpay/master/jars/client-api-ente/src/main/resources/v2/govpay-api-ec.yaml&nocors>`_

.. toctree::
        :maxdepth: 2
	:hidden:
        :caption: Argomenti trattati:

	autenticazione/index
        autorizzazione/index
