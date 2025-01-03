/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRichiestaStornoRisposta;


public class Risposta  {

	private String esito;
	private FaultBean faultBean;
	private List<FaultBean> listaErroriRPT;
	private String url;
	
	public Risposta(NodoInviaRPTRisposta r) {
		this.esito = r.getEsito();
		this.url = r.getUrl();
		this.faultBean = r.getFault();
	}
	
	public Risposta(NodoInviaCarrelloRPTRisposta r) {
		this.esito = r.getEsitoComplessivoOperazione();
		this.url = r.getUrl();
		this.listaErroriRPT = r.getListaErroriRPT() != null ? r.getListaErroriRPT().getFault() : null;
		this.faultBean = r.getFault();
	}
	
	public Risposta(NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno) {
		this.esito = nodoInviaRichiestaStorno.getEsito();
		this.faultBean = nodoInviaRichiestaStorno.getFault();
	}

	public String getEsito() {
		return this.esito;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public FaultBean getFaultBean() {
		if(this.faultBean != null)
			return this.faultBean;
		
		if(this.listaErroriRPT != null && this.listaErroriRPT.size() > 0) {
			
			if(this.listaErroriRPT.size() == 1)
				return this.listaErroriRPT.get(0);
		
			Set<String> codici = new HashSet<String>();
			Set<String> ids = new HashSet<String>();
			Set<String> faultStrings = new HashSet<String>();
			Set<String> descrizioni = new HashSet<String>();
			String descrizione = "";
			
			for(FaultBean fb : this.listaErroriRPT) {
				codici.add(fb.getFaultCode());
				faultStrings.add(fb.getFaultString());
				ids.add(fb.getFaultString());
				descrizioni.add(fb.getDescription());
				descrizione += toString(fb);
			}
				
			FaultBean fbx = new FaultBean();
			
			if(ids.size() == 1)
				fbx.setId(codici.iterator().next());
			else 
				fbx.setId(this.listaErroriRPT.get(0).getId());
			
			if(codici.size() == 1)
				fbx.setFaultCode(codici.iterator().next());
			else
				fbx.setFaultCode("PPT_ERRORE_GENERICO");
			
			if(faultStrings.size() == 1)
				fbx.setFaultString(faultStrings.iterator().next());
			else
				fbx.setFaultString("RPT rifiutate per errori eterogenei");
			
			if(descrizioni.size() == 1)
				fbx.setDescription(descrizioni.iterator().next());
			else
				fbx.setDescription(descrizione);
			
			return fbx;
		}		
		
		return null;
	}
	
	public String getLog() {
		String log = "Ricevuto esito " + this.esito;
		
		if(this.faultBean != null || this.listaErroriRPT != null) {
			log += " con FaultBean ";
		}
		
		if(this.faultBean != null)
			log += "\n" + toString(this.faultBean);
		
		if(this.listaErroriRPT != null) {
			for(FaultBean fb : this.listaErroriRPT)
				log += "\n" + toString(fb);
		}
		return log;
	}
	
	private String toString(FaultBean faultBean) {
		StringBuffer sb = new StringBuffer();
		if(faultBean != null) {
			
			if(faultBean.getSerial() != null)
				sb.append("#" + faultBean.getSerial() + " ");
			
			sb.append("["+faultBean.getFaultCode()+"]");
			
			if(faultBean.getFaultString() != null)
				sb.append(" " + faultBean.getFaultString());
			
			if(faultBean.getDescription() != null)
				sb.append(": " +  faultBean.getDescription() + " ");
		}
		return sb.toString();
	}

	public List<FaultBean> getListaErroriRPT() {
		return listaErroriRPT;
	}

}
