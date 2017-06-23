package it.govpay.web.rs.dars.base;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.utils.Utils;

public abstract class StatisticaDarsHandler<T> extends BaseDarsHandler<T> implements IStatisticaDarsHandler<T> {

	public StatisticaDarsHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
	}

	@Override
	public abstract PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException;
	
	@Override
	public InfoForm getInfoGrafico(UriInfo uriInfo, BasicBD bd ) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, true);
	}

	@Override
	public InfoForm getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, visualizzaRicerca, null);
	}

	@Override
	public InfoForm getInfoGrafico(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		return this.getInfoGrafico(uriInfo, bd, true, parameters);
	}

	@Override
	public abstract InfoForm getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters ) throws ConsoleException;

	@Override
	public URI getUriGrafico(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		try{
			URI uri = new URI(this.pathServizio);
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public URI getUriGrafico(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException {
		return Utils.creaUriConParametri(this.pathServizio,parameters);
	}
}
