package it.govpay.pagamento.api.rs.v1.pagamenti;

import javax.ws.rs.Path;

import it.govpay.rs.v1.BaseRsServiceV1;

@Path("/psp")
public class Psp extends BaseRsServiceV1{
	
	
	public Psp() {
		super("psp");
	}

}
