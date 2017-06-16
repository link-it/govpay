package it.govpay.web.rs.dars.monitoraggio.versamenti;

import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.handler.IDarsHandler;

@Path("/dars/transazioni")
public class Transazioni extends DarsService {

	public Transazioni() {
		super();
	}
	
	Logger log = LogManager.getLogger();
	
	@Override
	public String getNomeServizio() {
		return "transazioni";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new TransazioniHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}
}