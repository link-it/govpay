package it.govpay.web.rs.dars.caricamenti.tracciati;

import javax.ws.rs.Path;

import it.govpay.model.Acl.Servizio;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.handler.IDarsHandler;

@Path("/dars/caricamenti/operazioni")
public class Operazioni extends DarsService {

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
	
	@Override
	public Servizio getFunzionalita() {
		return Servizio.PAGAMENTI_E_PENDENZE;
	}
}
