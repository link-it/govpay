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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.transport.http.HttpServletCredential;
import org.slf4j.Logger;

import it.govpay.bd.model.Utenza;
import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.CredentialUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import net.sf.json.JSONObject;

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

//	@OPTIONS
//	@Path("{path : .*}")
//	public Response optionsAll(@PathParam("path") String path) {
//		return Response.status(Response.Status.NO_CONTENT)
//				.header("Access-Control-Allow-Origin", "*")
//				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//				.header("Access-Control-Allow-Credentials", "true")
//				.header("Access-Control-Allow-Methods", "OPTIONS, GET, PUT, POST")
//				.build();
//	}

	protected List<String> getListaRuoli(HttpServletCredential credential){
		List<String> listaRuoliPosseduti = new ArrayList<String>();
		// caricamento dei ruoli ricevuti nella richiesta http
		for (String chiaveRuolo : this.aclCache.getChiaviRuoli()) {
			if(credential.isUserInRole(chiaveRuolo)){
				listaRuoliPosseduti.add(this.aclCache.getRuolo(chiaveRuolo));
			}
		}
		return listaRuoliPosseduti;
	}
	
	protected IAutorizzato getUser() {
		HttpServletCredential credential = new HttpServletCredential(this.request, this.log);
		Utenza user = CredentialUtils.getUser(credential);
		
		user.setRuoli(this.getListaRuoli(credential));
		List<Acl> aclDaRuoliContainer = new ArrayList<Acl>();
		for (String ruolo : user.getRuoli()) {
			aclDaRuoliContainer.addAll(this.aclCache.getAclsRuolo(ruolo));
		}
		user.setAclRuoli(aclDaRuoliContainer);
		return user;
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

	public static Response getBadRequestResponse(String msg){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("esito", "KO");
		jsonObject.put("descrizione", msg);

		ByteArrayInputStream bais = new ByteArrayInputStream(jsonObject.toString().getBytes());
		try{
			BaseRsService.copy(bais, baos);

			baos.flush();
			baos.close();
		}catch(Exception e){}


		Response res =	Response.status(Response.Status.BAD_REQUEST)
				.header("Access-Control-Allow-Origin", "*")
				.entity(baos.toString())
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

	// copy method from From E.R. Harold's book "Java I/O"
	public static void copy(InputStream in, OutputStream out) 
			throws IOException {

		// do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place

		synchronized (in) {
			synchronized (out) {

				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) break;
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}
	
	public int getVersione() {
		return 1;
	}
	
	public static Date convertJsonStringToDate(String dateJson) throws Exception{
		if(StringUtils.isNotEmpty(dateJson)){
			String []datPat = SimpleDateFormatUtils.datePatterns.toArray(new String[SimpleDateFormatUtils.datePatterns.size()]);
			return DateUtils.parseDate(dateJson, datPat);
		}
		return null;
	}
}

