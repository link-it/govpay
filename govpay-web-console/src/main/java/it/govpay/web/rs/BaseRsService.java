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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.web.utils.ConsoleProperties;

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
		if(this.response != null){
			this.codOperazione = UUID.randomUUID().toString();
			ThreadContext.put("cmd", cmd);
			ThreadContext.put("op",  this.codOperazione);
			this.response.setHeader("X-GP-CMDID", cmd);
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
		Operatore operatore = this.getOperatoreByPrincipal(bd);
		if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())) {
			   throw new WebApplicationException(this.getUnauthorizedResponse());
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
		if(principal == null){
			this.invalidateSession(null);
			throw new WebApplicationException(this.getForbiddenResponse());
		}
		
		Operatore operatore = null;
		try {
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			operatore = operatoriBD.getOperatore(principal);
			
			// Se l'utente non dispone di un profilo allora non e' autorizzato
			if(operatore.getProfilo() == null){
				this.invalidateSession(null);
				throw new WebApplicationException(this.getUnauthorizedResponse());
			}
			
			return operatore;
		} catch (ServiceException e) {
			throw e;
		} catch (NotFoundException e) {
			this.invalidateSession(null);
			throw new WebApplicationException(this.getUnauthorizedResponse());
		} catch (MultipleResultException e) {
			this.invalidateSession(null);
			throw new WebApplicationException(this.getUnauthorizedResponse());
		}	
	}
	
	protected Response getUnauthorizedResponse(){
		Response res =	Response.status(Response.Status.UNAUTHORIZED)
				.header("Access-Control-Allow-Origin", "*")
				.build();
		
		return res;
	}
	
	protected Response getForbiddenResponse(){
		Response res =	Response.status(Response.Status.FORBIDDEN)
				.header("Access-Control-Allow-Origin", "*")
				.build();
		
		return res;
	}

	public void invalidateSession(Logger log){
		if(log!= null)
			log.info("Invalidate Session in corso...");
		
		HttpSession session = this.request.getSession(false);
		if(session != null){
			session.invalidate();
		}
		
		if(log!= null)
			log.info("Invalidate Session completata.");
	}
	
	public static UriBuilder checkDarsURI(UriInfo uriInfo) throws MalformedURLException{
		 String urlDarsString = ConsoleProperties.getInstance().getUrlDARS();
		 if(urlDarsString != null){
			 try {
				URL urlDARS = new URL(urlDarsString);
				return uriInfo.getBaseUriBuilder()
						.scheme(urlDARS.getProtocol())
						.host(urlDARS.getHost())
						.port(urlDARS.getPort());
			} catch (MalformedURLException e) {
				throw e;
			}
		 }
		 
		 return uriInfo.getBaseUriBuilder();
	}
}
