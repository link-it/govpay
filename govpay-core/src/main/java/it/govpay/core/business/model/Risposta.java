/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.model;

import java.util.HashMap;
import java.util.Map;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStornoRisposta;

public class Risposta  {

	private String esito;
	private Map<Integer, FaultBean> faultMap;
	private FaultBean faultBean;
	private String url;
	
	public Risposta(NodoInviaRPTRisposta r) {
		this.esito = r.getEsito();
		this.url = r.getUrl();
		
		if(r.getFault() != null) {
			this.esito = "KO";
			this.faultBean = r.getFault();
		}
	}
	
	public Risposta(NodoInviaCarrelloRPTRisposta r) {
		this.esito = r.getEsitoComplessivoOperazione();
		this.url = r.getUrl();
		
		if(!this.esito.equals("OK")) {
			if(r.getFault() != null) {
				this.faultBean = r.getFault();
			} 
				
			if(r.getListaErroriRPT() != null) {
				faultMap = new HashMap<Integer, FaultBean>();
				for(FaultBean fb : r.getListaErroriRPT().getFault()) {
					faultMap.put(fb.getSerial(), fb);
				}
			}
		}
	}
	
	public Risposta(NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno) {
		this.esito = nodoInviaRichiestaStorno.getEsito();
		
		if(nodoInviaRichiestaStorno.getFault() != null) {
			this.esito = "KO";
			this.faultBean = nodoInviaRichiestaStorno.getFault();
		}
	}

	public String getEsito() {
		return this.esito;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public FaultBean getFaultBean(int pos) {
		if(this.faultBean != null)
			return this.faultBean;
		
		if(this.faultMap != null && this.faultMap.get(new Integer(pos)) != null)
			return this.faultMap.get(new Integer(pos));
			
		return null;
	}
	
	public String getFaultBeanString(int pos) {
		if(this.faultBean != null)
			return toString(this.faultBean);
		
		if(this.faultMap != null && this.faultMap.get(new Integer(pos)) != null)
			return toString(this.faultMap.get(new Integer(pos)));
			
		return null;
	}

	public String getLog() {
		String log = "Ricevuto esito " + this.esito;
		if(this.faultBean != null) log += " con FaultBean " + toString(this.faultBean);
		if(this.faultMap != null) {
			for(FaultBean fb : this.faultMap.values())
				log += "\nFaultBean " + toString(fb);
		}
		return log;
	}
	
	private String toString(FaultBean faultBean) {
		StringBuffer sb = new StringBuffer();
		if(faultBean != null) {
			sb.append(this.faultBean.getFaultCode());
			if(this.faultBean.getFaultString() != null)
				sb.append(": " + this.faultBean.getFaultString());
			
			if(this.faultBean.getSerial() != null)
				sb.append(" per l'elemento " + this.faultBean.getSerial());
			
			if(this.faultBean.getDescription() != null)
				sb.append(" \"" +  this.faultBean.getDescription() + "\"");
		}
		if(sb.length() >= 500)
			return sb.substring(0, 500);
		else
			return sb.toString();
	}
	
}
