package it.govpay.core.utils.appio.client;

import org.glassfish.jersey.client.ClientConfig;
import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;

import it.govpay.core.utils.appio.impl.ApiClient;

public class AppIoAPIClient extends ApiClient {

	
	@Override
	protected void performAdditionalClientConfiguration(ClientConfig clientConfig) {
		clientConfig.register(JacksonJsonProvider.class);
	}
}
