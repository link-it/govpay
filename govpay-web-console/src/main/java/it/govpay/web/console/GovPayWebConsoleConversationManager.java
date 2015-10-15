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
package it.govpay.web.console;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;

@Named("govpayConversationManager")
@SessionScoped
public class GovPayWebConsoleConversationManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String GDE_CID = "0";
	public static final String NUOVO_PAGAMENTO_CID = "1";
	public static final String DISTINTA_CID = "2";
	public static final String INTERMEDIARI_CID = "3";
	public static final String ENTI_CID = "4";
	public static final String GATEWAY_CID = "5";
	public static final String CONNETTORE_CID = "6";
	public static final String ESITO_CID = "7";
	
	public static final long TIMEOUT_CONVERSATION = 600000;// 86400000;

	@Inject  
	private transient Logger log;	
	
	@PostConstruct
	private void init(){
		this.conversations = new ArrayList<String>();
	}

	private List<String> conversations = null;

	public List<String> getConversations() {
		return conversations;
	}

	public void clear(){
		this.conversations.clear();
	}

	public void addConversation(String cid){
		this.log.debug("Aggiunta Conversation Id["+cid+"]");

		this.conversations.add(cid);
	}
	
	/***
	 * 
	 * Chiude tutte le conversazioni ed inizializza quella corrente
	 * 
	 * @param cid
	 * @param conversation
	 */
	public void startConversation(String cid, Conversation conversation){
		log.debug("Start Conversation ["+cid+"], controllo stato Conversation ["+conversation.getId()+"]");
		
		conversation.setTimeout(TIMEOUT_CONVERSATION); 
		log.debug("Conversation Timeout["+conversation.getTimeout()+"]");
		
		List<String> idToRemove = new ArrayList<String>();
		
		for (int i = 0 ; i <  this.conversations.size() ; i++) {
			String conv = this.conversations.get(i);
			if(conv.equals(conversation.getId())){
				if(!conversation.isTransient()){
					conversation.end();
				}
			}
			idToRemove.add(0, conv);
		}	
		
		if(idToRemove.size() > 0 ){
			for (String conv : idToRemove) {
				this.removeConversation(conv);
			}
		}
				
		if(conversation.isTransient()){
			conversation.begin();
		
//			conversation.setTimeout(TIMEOUT_CONVERSATION);
			String id = conversation.getId();
			this.addConversation(id); 
		}
	}
	
	/***
	 * 
	 * Chiude la conversazione corrente.
	 * 
	 * @param cid
	 * @param conversation
	 */
	public void endConversation(String cid, Conversation conversation){
		
		log.debug("End Conversation ["+cid+"], Check Conversation ["+conversation.getId()+"]");
		List<String> idToRemove = new ArrayList<String>();
		
		for (int i = 0 ; i <  this.conversations.size() ; i++) {
			String conv = this.conversations.get(i);
			if(conv.equals(conversation.getId())){
				if(!conversation.isTransient()){
					conversation.end();
					
				}
			}
			idToRemove.add(0, conv);
		}	
		
		if(idToRemove.size() > 0 ){
			for (String conv : idToRemove) {
				this.removeConversation(conv);
			}
		}
	}

	public void removeConversation(String cid){
		this.log.debug("Rimossa Conversation Id["+cid+"]");

		this.conversations.remove(cid);
	}

	public boolean exists(String cid){
		boolean exists = false;

		if(cid == null) 
			exists = false;
		else		
			exists =  this.conversations.contains(cid);
		
		this.log.debug("Check Conversation Id["+cid+"] -> Exists["+exists+"]");

		return exists;
	}
}
