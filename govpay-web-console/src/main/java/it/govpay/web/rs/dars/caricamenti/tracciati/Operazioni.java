package it.govpay.web.rs.dars.caricamenti.tracciati;

import javax.ws.rs.Path;

import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;

@Path("/dars/caricamenti/operazioni")
public class Operazioni extends BaseDarsService {

	public Operazioni() {
		super();
	}
	
	@Override
	public String getNomeServizio() {
		return "caricamentoOperazioni";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new OperazioniHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/caricamenti/operazioni";// + this.getNomeServizio();
	}
}
