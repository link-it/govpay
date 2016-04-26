package it.govpay.web.rs.dars.monitoraggio.versamenti;

import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;

@Path("/dars/singoliVersamenti")
public class SingoliVersamenti extends BaseDarsService {

	public SingoliVersamenti() {
		super();
	}
	
	Logger log = LogManager.getLogger();
	
	@Override
	public String getNomeServizio() {
		return "singoliVersamenti";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new SingoliVersamentiHandler(log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/" + getNomeServizio();
	}
}