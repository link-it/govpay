/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.console.facade;


import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.model.OperatoreModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;

/***
 * 
 * Classe per il supporto al login dell'utente
 * 
 * @author Giuliano Pintori (pintori@link.it)
 *
 */
@Stateless @Named("loginFacade")
public class LoginFacadeImpl implements LoginFacade {
	
	@Inject  
	Logger log;
//	private Logger log = LogManager.getLogger(LoginFacadeImpl.class);

	@EJB
	AnagraficaEJB anagraficaEjb; 
	
	@PostConstruct 
	private void checkLoginFacadeImpl(){
		if(log!= null)
			log.debug("Init LoginFacadeImpl completato."); 
	}
	
	@Override
	public OperatoreModel loginUtente(String username, String password)
			throws ServiceException {
		
		try{
			log.debug("Autenticazione utente ["+username+"]...");
			return anagraficaEjb.getOperatore(username, password);
		}catch(Exception e){
			log.error("Si e' verificato un errore durante l'autenticazione dell'utente ["+username+"]", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean login(String username, String password) throws ServiceException {
		try{
			log.debug("Autenticazione utente ["+username+"]...");
			return anagraficaEjb.getOperatore(username, password) != null;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante l'autenticazione dell'utente ["+username+"]", e);
			throw new ServiceException(e);
		}
	}
}
