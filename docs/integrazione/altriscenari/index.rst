.. _integrazione_altriscenari:

Altri scenari di integrazione
=============================

In questo capitolo si analizzano scenari meno usuali che l’Ente Creditore può realizzare e come GovPay ne supporti la realizzazione tramite i servizi di integrazione.

Scelta del modello di integrazione al WISP
------------------------------------------

La piattaforma pagoPA predispone versioni diverse del WISP che realizzano workflow di pagamento non compatibili tra loro. Il modello di intermediazione realizzato da GovPay rende trasparente al Portale di Pagamento il workflow utilizzato, lasciando comunque la possibilità di scelta in fase di avvio del pagamento.

La versione del workflow di pagamento realizzato da GovPay viene gestito a livello di configurazione, che per default è impostato alla versione 2.0. Il portale può comunque imporre la versione da implementare passando nella query string della richiesta di pagamento il parametro *versioneInterfacciaWISP* con uno dei valori possibili (1.3 o 2.0).

Redirezione con più portali di pagamento
----------------------------------------

La piattaforma pagoPA consente di configurare una url per Ente Creditore a cui i Soggetti Pagatori vengono rediretti al temine del processo di pagamento ad iniziativa Ente. Questa URL è la pagina del Portale di Pagamento dove il pagatore visualizza l’esito della transazione.

Nel caso in cui l’Ente Creditore disponga di più Portali di Pagamento, può gestire la redirezione tramite l’ausilio del componente Web Connector di GovPay. Il ritorno del Soggetto Pagatore può essere gestito specificando la URL di ritorno nella richiesta di pagamento.

Il Web Connecctor di GovPay si farà carico di redirigere il navigatore al corretto Portale di Pagamento includendo nella URL i parametri di *esito* e *idSession* previsti dalla specifica.
