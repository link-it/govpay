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
package it.govpay.web.console.listener;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.LoginBean;

public class HttpSessionCheckListener implements HttpSessionListener {

	@Inject  
	Logger log;


	@Override
	public void sessionCreated(HttpSessionEvent e) {
		log.debug("session "+e.getSession().getId()+" created.");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		log.debug("session "+e.getSession().getId()+" destroyed.");
		try{
			cleanUp(e.getSession());
		}catch (Exception ex) {
			log.error("errore durante le operazioni di clean-up",ex);
		}
	}

	private void cleanUp(HttpSession session) throws Exception{
		//recuper utente dalla sessione se esiste
		LoginBean lb = (LoginBean)session.getAttribute("loginBean");

		if(lb!=null){
			log.debug("remove user "+lb.getUsername()+" from session");
			session.setAttribute("loginBean", null);
		}else{
			log.debug("no login info found in session, nothing to do.");
		}

	}


}
