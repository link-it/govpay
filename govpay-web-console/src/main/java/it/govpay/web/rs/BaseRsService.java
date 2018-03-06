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
package it.govpay.web.rs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.model.Operatore;
import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.AclEngine;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

@Path("/")
public abstract class BaseRsService {

	public static List<String> datePatterns = null;
	static {

		datePatterns = new ArrayList<String>();
		datePatterns.add(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern());
		datePatterns.add(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
	}

	public static final String ERRORE_INTERNO = "Errore Interno";
	public static final String PARAMETER_LINGUA = "lang";
	private AclCache aclCache = null;
	
	public BaseRsService() {
		this.aclCache = AclCache.getInstance();
	}

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	protected String codOperazione;

	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	protected void initLogger(String cmd) {
		if(this.response != null){
			this.codOperazione = UUID.randomUUID().toString();
			MDC.put("cmd", cmd);
			MDC.put("op",  this.codOperazione);
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

	public void checkDirittiServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isOperatoreAdminServizio(bd, servizio))
			if(!this.checkDirittiServizioOperatore(bd, servizio))
				throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isOperatoreAdminServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		return true;
//		return AclEngine.isAdminDirittiOperatore(getOperatoreByPrincipal(bd).getUtenza(), servizio);
	}

	public boolean checkDirittiServizioOperatore(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.LETTURA);
		listaDiritti.add(Diritti.SCRITTURA);
		listaDiritti.add(Diritti.ESECUZIONE);
		return true;
//		return AclEngine.isAuthorized(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
//		return topDirittiOperatore > Acl.NO_DIRITTI;
	}

	public void checkDirittiServizioLettura(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isServizioAbilitatoLettura(bd, servizio))
			throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isServizioAbilitatoLettura(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.LETTURA);
		return true;
	//	return AclEngine.isAuthorized(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
//		return topDirittiOperatore >= Acl.DIRITTI_LETTURA;
	}

	public void checkDirittiServizioScrittura(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isServizioAbilitatoScrittura(bd, servizio))
			throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isServizioAbilitatoScrittura(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.SCRITTURA);
		listaDiritti.add(Diritti.ESECUZIONE);
		return true;
		//	return AclEngine.isAuthorized(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
//		return topDirittiOperatore == Acl.DIRITTI_SCRITTURA;
	}

	public List<String> getDominiAbilitatiLetturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.LETTURA);
		return AclEngine.getDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		return AclEngine.getDominiAutorizzati(this.getOperatoreByPrincipal(bd).getUtenza(), servizio, Acl.DIRITTI_LETTURA);
	}

	public List<Long> getIdDominiAbilitatiLetturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.LETTURA);
		return AclEngine.getIdDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		Set<Long> idDominiAutorizzati = AclEngine.getIdDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, Acl.DIRITTI_LETTURA);
//		if(idDominiAutorizzati == null) {
//			idDominiAutorizzati = new HashSet<Long>();
//			idDominiAutorizzati.add(-1L);
//		}
//		return idDominiAutorizzati;
	}

	public List<String> getDominiAbilitatiScritturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.SCRITTURA);
		return AclEngine.getDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		return AclEngine.getDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, Acl.DIRITTI_SCRITTURA);
	}

	public List<Long> getIdDominiAbilitatiScritturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(Diritti.SCRITTURA);
		return AclEngine.getIdDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, listaDiritti);
//		Set<Long> idDominiAutorizzati = AclEngine.getIdDominiAutorizzati(getOperatoreByPrincipal(bd).getUtenza(), servizio, Acl.DIRITTI_SCRITTURA);
//		if(idDominiAutorizzati == null) {
//			idDominiAutorizzati = new HashSet<Long>();
//			idDominiAutorizzati.add(-1L);
//		}
//		return idDominiAutorizzati;
	}

//	private int getTopDirittiServizioOperatore(BasicBD bd, Acl.Servizio servizio) throws ServiceException {
//		int topDirittiOperatore = AclEngine.getTopDirittiOperatore(getOperatoreByPrincipal(bd).getUtenza(), servizio);
//		return topDirittiOperatore;
//	}


	public List<String> getRuoliOperatore(BasicBD bd) throws ServiceException {
		Operatore operatore = this.getOperatoreByPrincipal(bd);
		return this.getRuoliOperatore(bd, operatore);
	}
	
//	public List<String> getRuoliOperatore(BasicBD bd, it.govpay.model.Operatore operatore) throws ServiceException {
//		List<Ruolo> ruoliUtente = new ArrayList<Ruolo>();
//		List<String> ruoliOp = operatore.getRuoli();
//		List<Ruolo> listaRuoliRegistrati = getListaRuoliRegistrati(bd);
//
//		for (String codRuolo : ruoliOp) {
//			if(codRuolo.equals(Operatore.RUOLO_SYSTEM)){
//				for (Ruolo ruolo : listaRuoliRegistrati) {
//					if(this.request.isUserInRole(ruolo.getCodRuolo())){
//						ruoliUtente.add(ruolo);
//					}
//				}
//			} else {
//				for (Ruolo ruolo : listaRuoliRegistrati) {
//					if(codRuolo.equals(ruolo.getCodRuolo())){
//						ruoliUtente.add(ruolo);
//					}
//				}
//			}
//		}
//		return ruoliUtente;
//	}
	
	protected List<String> getRuoliOperatore(BasicBD bd, it.govpay.model.Operatore operatore){
		List<String> listaRuoliPosseduti = new ArrayList<String>();
		// caricamento dei ruoli ricevuti nella richiesta http
//		for (String chiaveRuolo : this.aclCache.getChiaviRuoli()) {
//			if(this.request.isUserInRole(chiaveRuolo)){
//				listaRuoliPosseduti.add(this.aclCache.getRuolo(chiaveRuolo));
//			}
//		}
		return listaRuoliPosseduti;
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
			boolean controlloPrincipalCaseInsensitive = ConsoleProperties.getInstance().isAbilitaControlloPrincipalCaseInsensitive();
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			operatore = operatoriBD.getOperatore(principal,controlloPrincipalCaseInsensitive);

			// Se l'utente non dispone di almeno un ruolo allora non e' autorizzato
//			if(operatore.getRuoli() == null || operatore.getRuoli().size() == 0){
//				this.invalidateSession(null);
//				throw new WebApplicationException(this.getUnauthorizedResponse());
//			}

			// controllo che tra tutti i ruoli associati all'utente ci sia almeno un acl 
			List<String> ruoliOperatore = getRuoliOperatore(bd,operatore);

			boolean find = true; // TODO sbloccare dopo insert ruoli  
//			for(Ruolo ruolo : ruoliOperatore) {
//				if(ruolo.getAcls() != null && ruolo.getAcls().size() > 0){
				if(ruoliOperatore != null && ruoliOperatore.size() > 0){
					find = true;
//					break;
				}
//			}
			
			if(!find){
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

	public static Date convertJsonStringToDate(String dateJson) throws Exception{
		if(StringUtils.isNotEmpty(dateJson)){
			String []datPat = datePatterns.toArray(new String[datePatterns.size()]);
			return DateUtils.parseDate(dateJson, datPat);
		}
		return null;
	}

	public Locale getLanguage(){
		Locale locale = null;

		String lingua =  this.request != null ? this.request.getParameter(BaseRsService.PARAMETER_LINGUA) : null;
		locale = Utils.getInstance(lingua).getLocale();

		return locale;
	}
}
