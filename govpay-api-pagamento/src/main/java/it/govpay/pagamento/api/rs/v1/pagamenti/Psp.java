package it.govpay.pagamento.api.rs.v1.pagamenti;

import javax.ws.rs.Path;

import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.PendenzeController;
import it.govpay.rs.v1.controllers.base.PspController;

@Path("/psp")
public class Psp extends BaseRsServiceV1{
	
	private PspController controller = null;
	
	public Psp() {
		super("psp");
		this.controller = new PspController(this.nomeServizio,this.log);
	}

}
