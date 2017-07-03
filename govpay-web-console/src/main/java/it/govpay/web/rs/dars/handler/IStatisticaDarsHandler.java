package it.govpay.web.rs.dars.handler;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;

public interface IStatisticaDarsHandler<T> extends IBaseDarsHandler<T> {

	public PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
	
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd) throws ConsoleException;
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException;
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters) throws ConsoleException;
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String, String> parameters) throws ConsoleException;
}
