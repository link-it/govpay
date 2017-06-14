package it.govpay.web.rs.dars.caricamenti.tracciati;

import javax.ws.rs.Path;

import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;

@Path("/dars/caricamenti/tracciati")
public class Tracciati extends BaseDarsService {

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
		return "/dars/" + this.getNomeServizio();
	}
}
