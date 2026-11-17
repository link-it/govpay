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

-  **it.govpay.clusterId**: identificativo dell'istanza di GovPay, una stringa univoca tra le istanze. Serve a coordinare i batch interni del core tra i nodi: se la proprietà non è impostata il coordinamento è disattivato e ogni nodo esegue i batch per conto proprio.

Queste proprietà possono essere specificate sia nelle Java Options, dei processi Java associati agli application server, oppure nel file *govpay.properties* nella directory di lavoro di ogni nodo.

I componenti esterni (batch) possono girare in più istanze senza configurazione
aggiuntiva: si coordinano con i metadati Spring Batch sul database, usando come
identificativo del nodo il nome dell'host o, nelle immagini Docker, l'indirizzo
del container. Il dettaglio è nel README di ciascun componente.

