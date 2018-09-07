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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.AnagraficaManagerNoCache;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Operatore;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;

public class BaseDAO {
	
	protected boolean useCacheData;
	
	public BaseDAO() {
		this(true);
	}
	
	public BaseDAO(boolean useCacheData) {
		this.useCacheData = useCacheData;
	}

	public String populateUser(IAutorizzato user, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		if(user == null || user.getPrincipal() == null)
			throw AclEngine.toNotAuthenticatedException(user);

		try {
			Applicazione applicazione = this.getApplicazioneFromUser(user, bd);
			user.merge(applicazione.getUtenza());
			return applicazione.getPrincipal();
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) { 
			try {
				Operatore operatore = this.getOperatoreFromUser(user, bd);
				user.merge(operatore.getUtenza());
				return operatore.getNome();
			} catch (org.openspcoop2.generic_project.exception.NotFoundException ex) {
				throw AclEngine.toNotAuthorizedException(user);					
			}
		}
	}

	public Applicazione getApplicazioneFromUser(IAutorizzato user, BasicBD bd) throws ServiceException, NotFoundException {
		if(this.useCacheData) {
			return user.isCheckSubject() ? AnagraficaManager.getApplicazioneBySubject(bd, user.getPrincipal())
					: AnagraficaManager.getApplicazioneByPrincipal(bd, user.getPrincipal());
		} else {
			return user.isCheckSubject() ? AnagraficaManagerNoCache.getApplicazioneBySubject(bd, user.getPrincipal())
					: AnagraficaManagerNoCache.getApplicazioneByPrincipal(bd, user.getPrincipal());
		}
	}
	
	public Operatore getOperatoreFromUser(IAutorizzato user, BasicBD bd) throws ServiceException, NotFoundException {
		if(this.useCacheData) {
			return user.isCheckSubject() ? AnagraficaManager.getOperatoreBySubject(bd, user.getPrincipal())
				: AnagraficaManager.getOperatoreByPrincipal(bd, user.getPrincipal());
		} else {
			return user.isCheckSubject() ? AnagraficaManagerNoCache.getOperatoreBySubject(bd, user.getPrincipal())
					: AnagraficaManagerNoCache.getOperatoreByPrincipal(bd, user.getPrincipal());
		}
	}

	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, Diritti diritti) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti);
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, Diritti diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, bd);
	}

	public void autorizzaRichiesta(IAutorizzato user, List<Servizio> servizio, Diritti diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, bd);
	}

	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, List<Diritti> diritti) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(user, servizio, diritti, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, List<Diritti> diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. integro le informazioni dell'utente
		this.populateUser(user, bd);

		// 2. invocazione acl engine
		boolean authorized = AclEngine.isAuthorized(user, servizio, diritti);

		if(!authorized)
			throw AclEngine.toNotAuthorizedException(user, servizio, diritti);
	}

	public void autorizzaRichiesta(IAutorizzato user, List<Servizio> servizi, List<Diritti> diritti, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. integro le informazioni dell'utente
		this.populateUser(user, bd);

		// 2. invocazione acl engine
		boolean authorized = false;
		for(Servizio servizio : servizi) {
			authorized = AclEngine.isAuthorized(user, servizio, diritti);
			if(authorized)
				break;
		}

		if(!authorized)
			throw AclEngine.toNotAuthorizedException(user, servizi, diritti);
	}



	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, Diritti diritti, String codDominio, String codTributo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, codDominio, codTributo);
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, Diritti diritti, String codDominio, String codTributo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritti);
		this.autorizzaRichiesta(user, servizio, listaDiritti, codDominio, codTributo, bd); 
	}


	public void autorizzaRichiesta(IAutorizzato user,Servizio servizio, List<Diritti> diritti, String codDominio, String codTributo) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(user, servizio, diritti, codDominio, codTributo, bd); 
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public void autorizzaRichiesta(IAutorizzato user, Servizio servizio, List<Diritti> diritti, String codDominio, String codTributo, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException{
		// 1. integro le informazioni dell'utente
		this.populateUser(user, bd);

		// 2. invocazione acl engine
		boolean authorized = AclEngine.isAuthorized(user, servizio, codDominio, codTributo, diritti);

		if(!authorized)
			throw AclEngine.toNotAuthorizedException(user, servizio, diritti, codDominio, codTributo);
	}
}
