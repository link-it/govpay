package it.govpay.pagamento.api.rs.v2.pagamenti;

import javax.ws.rs.Path;

import org.openspcoop2.generic_project.exception.ServiceException;

@Path("/v2/domini")
public class Domini extends it.govpay.pagamento.api.rs.v1.pagamenti.Domini{

	public Domini() throws ServiceException {
		super();
	}
}


