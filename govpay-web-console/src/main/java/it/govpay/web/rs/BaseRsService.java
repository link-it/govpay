/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.web.rs;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;

@Path("/")
public abstract class BaseRsService {
	
	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	protected String codOperazione;
	
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}
		

	protected void initLogger(String cmd) {
		if(response != null){
			codOperazione = UUID.randomUUID().toString();
			ThreadContext.put("cmd", cmd);
			ThreadContext.put("op",  codOperazione);
			response.setHeader("X-GP-CMDID", cmd);
		}
	}

	@OPTIONS
	@Path("{path : .*}")
	public Response optionsAll(@PathParam("path") String path) {
		return Response.status(Response.Status.NO_CONTENT)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "OPTIONS, GET, PUT, POST")
				.build();
	}
	
	public void checkOperatoreAdmin(BasicBD bd) throws ServiceException,WebApplicationException{
		Operatore operatore = getOperatoreByPrincipal(bd);
		if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())) {
			   throw new WebApplicationException(getUnauthorizedResponse());
		}
	}
	
	protected String getPrincipal(){
		if(this.request.getUserPrincipal()!=null){
			return this.request.getUserPrincipal().getName();
		}
		return null;
	}
		
	public Operatore getOperatoreByPrincipal(BasicBD bd) throws ServiceException,WebApplicationException {
		Operatore operatore = getOperatoreByPrincipal(bd, getPrincipal());
		return operatore;
	}

	protected Operatore getOperatoreByPrincipal(BasicBD bd, String principal) throws ServiceException,WebApplicationException{
		if(principal == null)
			throw new WebApplicationException(getUnauthorizedResponse());
		
		Operatore operatore = null;
		try {
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			operatore = operatoriBD.getOperatore(principal);
			
			// Se l'utente non dispone di un profilo allora non e' autorizzato
			if(operatore.getProfilo() == null)
				throw new WebApplicationException(getUnauthorizedResponse());
			
			return operatore;
		} catch (ServiceException e) {
			throw e;
		} catch (NotFoundException e) {
			throw new WebApplicationException(getUnauthorizedResponse());
		} catch (MultipleResultException e) {
			throw new WebApplicationException(getUnauthorizedResponse());
		}	
	}
	
	protected Response getUnauthorizedResponse(){
		Response res =	Response.status(Response.Status.UNAUTHORIZED)
				// [TODO] controllare l'autorizzazione agli accessi quando si fondono la console e i servizi dars
				.header("Access-Control-Allow-Origin", "*")
				.build();
		
		return res;
	}

}
