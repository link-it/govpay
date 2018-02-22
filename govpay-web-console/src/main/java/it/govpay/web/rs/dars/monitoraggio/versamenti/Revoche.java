package it.govpay.web.rs.dars.monitoraggio.versamenti;

import javax.ws.rs.Path;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.model.Acl.Servizio;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.handler.IDarsHandler;

@Path("/dars/rr")
public class Revoche extends DarsService {

	public Revoche() {
		super();
	}
	
	Logger log = LoggerWrapperFactory.getLogger(Revoche.class);
	
	@Override
	public String getNomeServizio() {
		return "rr";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new RevocheHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/" + this.getNomeServizio();
	}
	
	@Override
	public Servizio getFunzionalita() {
		return Servizio.Gestione_Pagamenti;
	}
}