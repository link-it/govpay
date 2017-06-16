package it.govpay.web.rs.dars.caricamenti.tracciati;

import javax.ws.rs.Path;

import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.handler.IDarsHandler;

@Path("/dars/caricamenti/tracciati")
public class Tracciati extends DarsService {

	public Tracciati() {
		super();
	}
	
	@Override
	public String getNomeServizio() {
		return "caricamentoTracciati";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new TracciatiHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/caricamenti/tracciati";// + this.getNomeServizio();
	}
}
