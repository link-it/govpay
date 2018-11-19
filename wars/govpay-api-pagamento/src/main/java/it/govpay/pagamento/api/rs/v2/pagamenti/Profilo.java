package it.govpay.pagamento.api.rs.v2.pagamenti;

import javax.ws.rs.Path;

import org.openspcoop2.generic_project.exception.ServiceException;


@Path("/v2/profilo")

public class Profilo extends it.govpay.pagamento.api.rs.v1.pagamenti.Profilo{

	public Profilo() throws ServiceException {
		super();
	}
}


