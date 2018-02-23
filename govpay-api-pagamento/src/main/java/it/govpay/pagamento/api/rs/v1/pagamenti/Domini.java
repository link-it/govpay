package it.govpay.pagamento.api.rs.v1.pagamenti;

import javax.ws.rs.Path;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.DominiController;

@Path("/domini")
public class Domini extends BaseRsServiceV1{
	
	private DominiController controller = null;
	
	public Domini() {
		super("domini");
		this.controller = new DominiController(this.nomeServizio,this.log);
	}

}
