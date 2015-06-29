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
package it.govpay.web.console.utils;

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.OperatoreModel;
import it.govpay.web.console.mbean.LoginMBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


public class Utils extends org.openspcoop2.generic_project.web.impl.jsf1.utils.Utils{

	public static OperatoreModel getLoggedUtente() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc!= null){
			ExternalContext ec = fc.getExternalContext();
			LoginMBean lb = (LoginMBean)ec.getSessionMap().get("loginBean");

			if(lb!= null && lb.getIsLoggedIn()){
				return lb.getLoggedUtente();
			}
		}
		return null;
	}
	
	public static LoginMBean getLoginBean() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc!= null){
			ExternalContext ec = fc.getExternalContext();
			LoginMBean lb = (LoginMBean)ec.getSessionMap().get("loginBean");

			return lb;
		}
		return null;
	}
	
	public static List<String> getListaIdEnteCreditoreGestitiDallOperatore() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc!= null){
			ExternalContext ec = fc.getExternalContext();
			LoginMBean lb = (LoginMBean)ec.getSessionMap().get("loginBean");

			if(lb!= null && lb.getIsLoggedIn()){
				OperatoreModel loggedUtente = lb.getLoggedUtente();
				List<String> identificativiEnteCreditore = new ArrayList<String>();
				if(loggedUtente.getEnti() != null && loggedUtente.getEnti().size() > 0 ){
					for (EnteCreditoreModel ente : loggedUtente.getEnti()) {
						identificativiEnteCreditore.add(ente.getIdEnteCreditore());
					}
				}
				return identificativiEnteCreditore;
			}
		}
		return null;
	}
	
	public static List<String> getListaIdFiscaliEnteCreditoreGestitiDallOperatore() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc!= null){
			ExternalContext ec = fc.getExternalContext();
			LoginMBean lb = (LoginMBean)ec.getSessionMap().get("loginBean");

			if(lb!= null && lb.getIsLoggedIn()){
				OperatoreModel loggedUtente = lb.getLoggedUtente();
				List<String> identificativiEnteCreditore = new ArrayList<String>();
				if(loggedUtente.getEnti() != null && loggedUtente.getEnti().size() > 0 ){
					for (EnteCreditoreModel ente : loggedUtente.getEnti()) {
						identificativiEnteCreditore.add(ente.getIdFiscale());
					}
				}
				return identificativiEnteCreditore;
			}
		}
		return null;
	}
	
	public static List<EnteCreditoreModel> getListaEntiCreditoreGestitiDallOperatore() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc!= null){
			ExternalContext ec = fc.getExternalContext();
			LoginMBean lb = (LoginMBean)ec.getSessionMap().get("loginBean");

			if(lb!= null && lb.getIsLoggedIn()){
				OperatoreModel loggedUtente = lb.getLoggedUtente();
				return loggedUtente.getEnti(); 
			}
		}
		return null;
	}
	
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
}
