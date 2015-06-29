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
package it.govpay.web.console.mbean;

import it.govpay.ejb.core.model.OperatoreModel;
import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.facade.LoginFacade;
import it.govpay.web.console.utils.GovpayWebConsoleConfiguration;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
//import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.LoginBean;

/***
 * 
 * Bean di sessione per la gestione della sessione utente.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 *
 */
@SessionScoped @Named("loginBean")  
public class LoginMBean extends LoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private OperatoreModel loggedOperatore = null;
	
	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;

	@Inject @Named("loginFacade")
	LoginFacade loginFacade;

	@Inject  
	Logger log;

	@PostConstruct 
	private void checkLoginMBean(){
		if(log!= null)
			log.debug("Init LoginBean completato."); 
	}

	@Override
	public String login() {
		if(null == this.getUsername() && this.getPassword() == null){		
			return "login";
		}

		log.debug("Login per l'utente ["+this.getUsername()+"] in corso..."); 

		try{
			this.loggedOperatore = this.loginFacade.loginUtente(this.getUsername(),this.getPassword());

			if(this.loggedOperatore != null){
				
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				HttpSession session = (HttpSession) ec.getSession(true);
				ec.getSessionMap().put("loginBean", this);
				session.setAttribute("logged", true);
				this.setIsLoggedIn(true);
				
				// Svuoto il gestore delle conversation
				this.conversationManager.clear();
				return "loginSuccess";
			}else{
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Errore: username o password non validi.",null));
			}
		}catch(ServiceException e){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Si e' verificato un errore durante l'esecuzione del Login: impossibile accedere all'applicazione.",null));
		}

		return "login";  
	}

	@Override
	public String logout(){

		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getExternalContext().getSessionMap().put("loginBean", null);
		HttpSession session = (HttpSession)fc.getExternalContext().getSession(false);
		session.setAttribute("logged", false);
		session.invalidate();
		this.setIsLoggedIn(false);
		return "logout";
	}



	public OperatoreModel getLoggedUtente() {
		return loggedOperatore;
	}

	public void setLoggedUtente(OperatoreModel loggedUtente) {
		this.loggedOperatore = loggedUtente;
	}
	
	public boolean isShowSimulazione() {
		return GovpayWebConsoleConfiguration.isShowSimulazione();
	}
}
