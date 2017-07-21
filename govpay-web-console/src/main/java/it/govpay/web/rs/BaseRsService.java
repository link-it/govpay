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
import java.util.Set;
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.core.utils.AclEngine;
import it.govpay.model.Acl;
import it.govpay.model.Operatore;
import it.govpay.model.Ruolo;
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

	public void checkDirittiServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isOperatoreAdminServizio(bd, servizio))
			if(!this.checkDirittiServizioOperatore(bd, servizio))
				throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isOperatoreAdminServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		return AclEngine.isAdminDirittiOperatore(ruoliUtente, servizio);
	}

	public boolean checkDirittiServizioOperatore(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
		return topDirittiOperatore > Ruolo.NO_DIRITTI;
	}

	public void checkDirittiServizioLettura(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isServizioAbilitatoLettura(bd, servizio))
			throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isServizioAbilitatoLettura(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
		return topDirittiOperatore >= Ruolo.DIRITTI_LETTURA;
	}

	public void checkDirittiServizioScrittura(BasicBD bd, Acl.Servizio servizio) throws ServiceException,WebApplicationException{
		if(!this.isServizioAbilitatoScrittura(bd, servizio))
			throw new WebApplicationException(this.getUnauthorizedResponse());
	}

	public boolean isServizioAbilitatoScrittura(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		int topDirittiOperatore = getTopDirittiServizioOperatore(bd, servizio);
		return topDirittiOperatore == Ruolo.DIRITTI_SCRITTURA;
	}

	public Set<String> getDominiAbilitatiLetturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		return AclEngine.getDominiAutorizzati(ruoliUtente, servizio, Ruolo.DIRITTI_LETTURA);
	}

	public Set<Long> getIdDominiAbilitatiLetturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		return AclEngine.getIdDominiAutorizzati(ruoliUtente, servizio, Ruolo.DIRITTI_LETTURA);
	}

	public Set<String> getDominiAbilitatiScritturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		return AclEngine.getDominiAutorizzati(ruoliUtente, servizio, Ruolo.DIRITTI_SCRITTURA);
	}

	public Set<Long> getIdDominiAbilitatiScritturaServizio(BasicBD bd, Acl.Servizio servizio) throws ServiceException{
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		return AclEngine.getIdDominiAutorizzati(ruoliUtente, servizio, Ruolo.DIRITTI_SCRITTURA);
	}

	private int getTopDirittiServizioOperatore(BasicBD bd, Acl.Servizio servizio) throws ServiceException {
		List<Ruolo> ruoliUtente = getRuoliOperatore(bd);
		int topDirittiOperatore = AclEngine.getTopDirittiOperatore(ruoliUtente, servizio);
		return topDirittiOperatore;
	}


	public List<Ruolo> getRuoliOperatore(BasicBD bd) throws ServiceException {
		Operatore operatore = this.getOperatoreByPrincipal(bd);
		return this.getRuoliOperatore(bd, operatore);
	}
	
	public List<Ruolo> getRuoliOperatore(BasicBD bd,Operatore operatore) throws ServiceException {
		List<Ruolo> ruoliUtente = new ArrayList<Ruolo>();
		List<String> ruoliOp = operatore.getRuoli();
		List<Ruolo> listaRuoliRegistrati = getListaRuoliRegistrati(bd);

		for (String codRuolo : ruoliOp) {
			if(codRuolo.equals(Operatore.RUOLO_SYSTEM)){
				for (Ruolo ruolo : listaRuoliRegistrati) {
					if(this.request.isUserInRole(ruolo.getCodRuolo())){
						ruoliUtente.add(ruolo);
					}
				}
			} else {
				for (Ruolo ruolo : listaRuoliRegistrati) {
					if(codRuolo.equals(ruolo.getCodRuolo())){
						ruoliUtente.add(ruolo);
					}
				}
			}
		}
		return ruoliUtente;
	}

	protected String getPrincipal(){
//				return "amministratore";
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
			if(operatore.getRuoli() == null || operatore.getRuoli().size() == 0){
				this.invalidateSession(null);
				throw new WebApplicationException(this.getUnauthorizedResponse());
			}

			// controllo che tra tutti i ruoli associati all'utente ci sia almeno un acl 
			List<Ruolo> ruoliOperatore = getRuoliOperatore(bd,operatore);

			boolean find = false;
			for(Ruolo ruolo : ruoliOperatore) {
				if(ruolo.getAcls() != null && ruolo.getAcls().size() > 0){
					find = true;
					break;
				}
			}
			
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

	protected List<it.govpay.model.Ruolo> getListaRuoliRegistrati(BasicBD bd) throws ServiceException{
		List<it.govpay.model.Ruolo> lst = new ArrayList<it.govpay.model.Ruolo>();
		try {
			RuoliBD ruoliBD = new RuoliBD(bd);
			RuoloFilter ruoliFilter = ruoliBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Ruolo.model().COD_RUOLO);
			fsw.setSortOrder(SortOrder.ASC);
			ruoliFilter.getFilterSortList().add(fsw);

			lst = ruoliBD.findAll(ruoliFilter);
			return lst;
		} catch (ServiceException e) {
			throw e;
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
