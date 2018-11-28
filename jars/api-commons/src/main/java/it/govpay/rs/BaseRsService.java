/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.rs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.ws.rs.OPTIONS;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.SimpleDateFormatUtils;

public abstract class BaseRsService {
	
	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;

	protected String nomeServizio;
	protected Logger log;

	protected String codOperazione;
	protected AclCache aclCache = null; 

	public BaseRsService() throws ServiceException{
		this.log = LoggerWrapperFactory.getLogger(BaseRsService.class);
		this.aclCache = AclCache.getInstance();
	}

	public BaseRsService(String nomeServizio) throws ServiceException{
		this();
		this.nomeServizio = nomeServizio;

	}

	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

//	protected List<String> getListaRuoli(HttpServletCredential credential){
//		List<String> listaRuoliPosseduti = new ArrayList<>();
//		// caricamento dei ruoli ricevuti nella richiesta http
//		for (String chiaveRuolo : this.aclCache.getChiaviRuoli()) {
//			if(credential.isUserInRole(chiaveRuolo)){
//				listaRuoliPosseduti.add(this.aclCache.getRuolo(chiaveRuolo));
//			}
//		}
//		return listaRuoliPosseduti;
//	}
	
	protected Authentication getUser() {
		return SecurityContextHolder.getContext().getAuthentication();
//		HttpServletCredential credential = new HttpServletCredential(this.request, this.log);
//		Utenza user = CredentialUtils.getUser(this.request, this.log);
//		
//		user.setRuoli(this.getListaRuoli(credential));
//		List<Acl> aclDaRuoliContainer = new ArrayList<>();
//		for (String ruolo : user.getRuoli()) {
//			aclDaRuoliContainer.addAll(this.aclCache.getAclsRuolo(ruolo));
//		}
//		user.setAclRuoli(aclDaRuoliContainer);
//		return user;
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

	public static boolean isEmpty(List<?> lista){
		if(lista == null)
			return true;

		return lista.isEmpty();
	}

	public abstract int getVersione();
}

