package it.govpay.web.rs.dars.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;

public interface IStatisticaDarsHandler<T> extends IBaseDarsHandler<T> {

	public PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException;
}
