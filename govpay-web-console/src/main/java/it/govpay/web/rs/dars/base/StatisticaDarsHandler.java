package it.govpay.web.rs.dars.base;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;

public abstract class StatisticaDarsHandler<T> extends BaseDarsHandler<T> implements IStatisticaDarsHandler<T> {

	public StatisticaDarsHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
	}

	@Override
	public abstract PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException;
	
	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd ) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, true);
	}

	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, visualizzaRicerca, null);
	}

	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		return this.getInfoGrafico(uriInfo, bd, true, parameters);
	}

	@Override
	public abstract Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters ) throws ConsoleException;

}
