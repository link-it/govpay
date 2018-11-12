package it.govpay.pagamento.api.rs.v2.pagamenti;

import javax.ws.rs.Path;

import org.openspcoop2.generic_project.exception.ServiceException;


@Path("/v2/pagamenti")

public class Pagamenti extends it.govpay.pagamento.api.rs.v1.pagamenti.Pagamenti{

	public Pagamenti() throws ServiceException {
		super();
	}
}


