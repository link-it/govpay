package it.govpay.netpay.v1.verifica;

import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.netpay.v1.api.VerificaPendenzeApi;
import it.govpay.netpay.v1.api.impl.ApiClient;

public class VerificaClient {

	private ConnettoreNotificaPagamenti connettore;
	private ApiClient apiClient;
	private VerificaPendenzeApi verificaPendenzeApi;
	
	public VerificaClient(ConnettoreNotificaPagamenti connettore) {
		this.connettore = connettore;
		
		// configurazione del client verso Net@Pay
		this.apiClient = new ApiClient();
		this.apiClient.setUsername(this.connettore.getNetPayUsername());
		this.apiClient.setPassword(this.connettore.getNetPayPassword());
		this.apiClient.setBasePath(this.connettore.getNetPayURL());
		
		this.verificaPendenzeApi = new VerificaPendenzeApi(this.apiClient);
	}
}
