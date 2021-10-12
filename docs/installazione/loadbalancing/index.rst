.. _inst_loadbalancing:

Configurazione in Load Balancing
================================

Per realizzare un’installazione in load balancing è necessario
predisporre più istanze dell’Application Server, ognuna con una propria
installazione di GovPay. Sarà inoltre necessario:

1. Che tutte le istanze di GovPay siano configurate per condividere lo stesso DB.
2. Che esista un Load Balancer in grado di bilanciare il flusso di richieste in arrivo sulle varie istanze di application server ospitanti GovPay.
3. Che GovPay sia opportunamente configurato con un identificatore unico che contraddistingua lo specifico nodo.

Le proprietà per la configurazione del singolo nodo sono le seguenti:

-  **it.govpay.clusterId**: identificativo dell'istanza di GovPay. Deve essere un numero univoco tra le istanze.

Queste proprietà possono essere specificate sia nelle Java Options, dei processi Java associati agli application server, oppure nel file *govpay.properties* nella directory di lavoro di ogni nodo.

