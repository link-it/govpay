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
package it.govpay.web.listener;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

public class HttpSessionCheckListener implements HttpSessionListener {

//	private static Logger log = LoggerManager.getGUILogger();
	
	Logger log = LoggerWrapperFactory.getLogger(HttpSessionCheckListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent e) {
		this.log.debug("session "+e.getSession().getId()+" created.");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		this.log.debug("session "+e.getSession().getId()+" destroyed.");
		try{
			HttpSession session = e.getSession();
			
			if(session != null)
				session.invalidate();
			
			
		}catch (Exception ex) {
			this.log.error("errore durante le operazioni di clean-up della sessione: ",ex);
		}
	}
	
}
