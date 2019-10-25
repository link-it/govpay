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
| API Pagamento           | base url: /govpay/frontend/api/pagamento      | `Interfaccia <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pagamento/src/main/webapp/v2/govpay-api-pagamento-v2.yaml>`_   |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Pendenze            | base url: /govpay/backend/api/pendenze        | `Interfaccia <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-pendenze/src/main/webapp/v2/govpay-api-pendenze-v2.yaml>`_     |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Riconciliazione     | base url: /govpay/backend/api/riconciliazione | `Interfaccia <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-ragioneria/src/main/webapp/v2/govpay-api-ragioneria-v2.yaml>`_ |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Backoffice          | base url: /govpay/backend/api/backoffice      | `Interfaccia <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/wars/api-backoffice/src/main/webapp/v1/govpay-api-backoffice-v1.yaml>`_ |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| API Verifica e notifica | base url: definita dall'ente creditore        | `Interfaccia <https://generator.swagger.io/?url=https://raw.githubusercontent.com/link-it/govpay/master/jars/client-api-ente/src/main/resources/govpay-api-ec-v1.yaml>`_        |
+-------------------------+-----------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+

