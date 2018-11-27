package it.govpay.core.utils.client.handler;

import it.govpay.core.exceptions.GovPayException;

public interface IntegrationOutHandler {

	public void invoke(IntegrationContext ic) throws GovPayException;

}
