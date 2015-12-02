 /*
 * OpenSPCoop v2 - Customizable SOAP Message Broker 
 * http://www.openspcoop2.org
 * 
 * Copyright (c) 2005-2015 Link.it srl (http://link.it).
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
package it.govpay.web.listener;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpSessionCheckListener implements HttpSessionListener {

//	private static Logger log = LoggerManager.getGUILogger();
	
	Logger log = LogManager.getLogger();
	
	@Override
	public void sessionCreated(HttpSessionEvent e) {
		log.debug("session "+e.getSession().getId()+" created.");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		log.debug("session "+e.getSession().getId()+" destroyed.");
		try{
			HttpSession session = e.getSession();
			
			if(session != null)
				session.invalidate();
		}catch (Exception ex) {
			log.error("errore durante le operazioni di clean-up della sessione: ",ex);
		}
	}
	
}
