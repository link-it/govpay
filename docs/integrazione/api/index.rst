.. _integrazione_api:

API di Integrazione
===================

La realizzazione degli scenari descritti nei capitoli successivi,
prevede l'utilizzo di servizi di integrazione, esposti da GovPay come
API REST, finalizzati all'integrazione dei sistemi verticali dell'Ente
Creditore. Nella tabella seguente sono indicati sia gli indirizzi di
base di erogazione di ciascun servizio, sia il riferimento alla
definizione OpenAPI per la documentazione di dettaglio.

+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Pagamento           | base url: /govpay/frontend/api/pagamento      | `v1 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-pagamento/src/main/webapp/v1/govpay-api-pagamento-v1.yaml>`_ `v2 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-pagamento/src/main/webapp/v2/govpay-api-pagamento-v2.yaml>`_   |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Pendenze            | base url: /govpay/backend/api/pendenze        | `v1 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-pendenze/src/main/webapp/v1/govpay-api-pendenze-v1.yaml>`_ `v2 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml>`_     |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Riconciliazione     | base url: /govpay/backend/api/riconciliazione | `v1 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-ragioneria/src/main/webapp/v1/govpay-api-ragioneria-v1.yaml>`_ `v2 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml>`_ `v3 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-ragioneria/src/main/webapp/v3/govpay-api-ragioneria.yaml>`_ |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Backoffice          | base url: /govpay/backend/api/backoffice      | `v1 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml>`_ |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Verifica e notifica | base url: definita dall'ente creditore        | `v1 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/jars/client-api-ente/src/main/resources/govpay-api-ec-v1.yaml>`_ `v2 <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/3.5.x/jars/client-api-ente/src/main/resources/v2/govpay-api-ec.yaml>`_        |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+

.. toctree::
        :maxdepth: 2
	:hidden:
        :caption: Argomenti trattati:

	autenticazione/index
        autorizzazione/index
