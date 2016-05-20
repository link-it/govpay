package it.govpay.web.rs.dars.manutenzione.strumenti;

import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;

@Path("/dars/strumenti")
public class Strumenti extends BaseDarsService {

	public Strumenti() {
		super();
	}
	
	Logger log = LogManager.getLogger();
	
	@Override
	public String getNomeServizio() {
		return "strumenti";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new StrumentiHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}
}