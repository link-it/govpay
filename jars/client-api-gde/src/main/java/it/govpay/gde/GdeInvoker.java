package it.govpay.gde;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import it.govpay.gde.v1.api.GdeControllerApi;
import it.govpay.gde.v1.api.impl.ApiClient;
import it.govpay.gde.v1.model.NuovoEvento;

public class GdeInvoker {
	
	private static Logger logger = LoggerFactory.getLogger(GdeInvoker.class);

	private GdeControllerApi gdeApi;
	
	public GdeInvoker(String gdeUrl){
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(gdeUrl);
		gdeApi = new GdeControllerApi(apiClient );
	}

	public void salvaEvento(NuovoEvento nuovoEvento) {

		try {
			logger.debug("AAAAAA Spedizione Evento in modalita' asincrona");
			this.gdeApi.addEvento(nuovoEvento )
			.subscribe(response -> logger.debug("AAAAAA Response: " + response));
			
			logger.debug("AAAAAA Spedizione Evento in modalita' asincrona, ok");
		}catch (WebClientResponseException e) {
			logger.error("AAAAAA Errore durante l'invocazione GDE: " + e.getMessage(),e);
		}
	}
}
