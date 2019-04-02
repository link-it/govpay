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
package it.govpay.core.dao.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BasicBD;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class BaseDAO {
	
	protected boolean useCacheData;
	protected Logger log = LoggerWrapperFactory.getLogger(BaseDAO.class);
	
	public BaseDAO() {
		this(true);
	}
	
	public BaseDAO(boolean useCacheData) {
		this.useCacheData = useCacheData;
	}
	
	public void autorizzaRichiesta(Authentication authentication,Servizio servizio, Diritti diritti) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		this.autorizzaRichiesta(authentication, servizio, diritti, false); 
	}

	public void autorizzaRichiesta(Authentication authentication,Servizio servizio, Diritti diritti, boolean accessoAnonimo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizio, listaDiritti, accessoAnonimo);
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, Diritti diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		this.autorizzaRichiesta(authentication, servizio, diritti,false,bd);
	}	

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, Diritti diritti, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
	List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizio, listaDiritti, accessoAnonimo, bd);
	}

	public void autorizzaRichiesta(Authentication authentication, List<Servizio> servizio, Diritti diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		this.autorizzaRichiesta(authentication, servizio, diritti,false,bd); 
	}

	public void autorizzaRichiesta(Authentication authentication, List<Servizio> servizio, Diritti diritti, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizio, listaDiritti, accessoAnonimo, bd);
	}

	public void autorizzaRichiesta(Authentication authentication,Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(authentication, servizio, diritti, accessoAnonimo, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, List<Diritti> diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		this.autorizzaRichiesta(authentication, servizio, diritti,false,bd);
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. invocazione AuthorizationManager
		boolean authorized = AuthorizationManager.isAuthorized(authentication, servizio, diritti, accessoAnonimo);

		if(!authorized)
			throw AuthorizationManager.toNotAuthorizedException(authentication, servizio, diritti, accessoAnonimo);
	}

	public void autorizzaRichiesta(Authentication authentication, List<Servizio> servizi, List<Diritti> diritti, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. invocazione AuthorizationManager
		boolean authorized = false;
		for(Servizio servizio : servizi) {
			authorized = AuthorizationManager.isAuthorized(authentication, servizio, diritti, accessoAnonimo);
			if(authorized)
				break;
		}

		if(!authorized)
			throw AuthorizationManager.toNotAuthorizedException(authentication, servizi, diritti, accessoAnonimo);
	}

	public void autorizzaRichiesta(Authentication authentication, List<Servizio> servizi, Diritti diritti, String codDominio, String codTipoVersamento, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		autorizzaRichiesta(authentication, servizi, diritti, codDominio, codTipoVersamento, false, bd);
	}
	
	public void autorizzaRichiesta(Authentication authentication, List<Servizio> servizi, Diritti diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizi, listaDiritti, codDominio, codTipoVersamento, accessoAnonimo, bd); 
	}

	public void autorizzaRichiesta(Authentication authentication,Servizio servizio, Diritti diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizio, listaDiritti, codDominio, codTipoVersamento, accessoAnonimo);
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, Diritti diritti, String codDominio, String codTipoVersamento, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		autorizzaRichiesta(authentication, servizio, diritti, codDominio, codTipoVersamento, false, bd);
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, Diritti diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(authentication, servizio, listaDiritti, codDominio, codTipoVersamento, accessoAnonimo, bd); 
	}
	
	public void autorizzaRichiesta(Authentication authentication,List<Servizio> servizi, List<Diritti> diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo,BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		// 1. invocazione AuthorizationManager
		boolean authorized = false;
		for(Servizio servizio : servizi) {
			authorized = AuthorizationManager.isAuthorized(authentication, servizio, codDominio, codTipoVersamento, diritti, accessoAnonimo);
			if(authorized)
				break;
		}

		if(!authorized)
			throw AuthorizationManager.toNotAuthorizedException(authentication, servizi, diritti, accessoAnonimo);
	}


	public void autorizzaRichiesta(Authentication authentication,Servizio servizio, List<Diritti> diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(authentication, servizio, diritti, codDominio, codTipoVersamento, accessoAnonimo, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(Authentication authentication, Servizio servizio, List<Diritti> diritti, String codDominio, String codTipoVersamento, boolean accessoAnonimo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. invocazione AuthorizationManager
		boolean authorized = AuthorizationManager.isAuthorized(authentication, servizio, codDominio, codTipoVersamento, diritti, accessoAnonimo);

		if(!authorized)
			throw AuthorizationManager.toNotAuthorizedException(authentication, servizio, diritti, accessoAnonimo, codDominio, codTipoVersamento);
	}
	
	public void autorizzaAccessoAnonimoVersamento(Authentication authentication, Servizio servizio, Diritti diritti, boolean accessoAnonimo, String cfToCheck, String idDebitore) throws NotAuthorizedException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaAccessoAnonimoVersamento(authentication, servizio, listaDiritti, accessoAnonimo, cfToCheck, idDebitore);
	}
	
	public void autorizzaAccessoAnonimoVersamento(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo, String cfToCheck, String idDebitore) throws NotAuthorizedException{
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
			if(GovpayConfig.getInstance().isCheckCfDebitore()) {
				if(StringUtils.isEmpty(cfToCheck) || !cfToCheck.equalsIgnoreCase(idDebitore)) {
					throw AuthorizationManager.toNotAuthorizedException(authentication, servizio, diritti, accessoAnonimo);
				}
			}
		}
	}
}
