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
package it.govpay.web.console.anagrafica.mbean;

import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.anagrafica.bean.CanaleBean;
import it.govpay.web.console.anagrafica.bean.PspBean;
import it.govpay.web.console.anagrafica.form.PspForm;
import it.govpay.web.console.anagrafica.form.PspSearchForm;
import it.govpay.web.console.anagrafica.iservice.IPspService;
import it.govpay.web.console.anagrafica.model.PspModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.BaseListView;
import org.openspcoop2.generic_project.web.impl.jsf1.utils.MessageUtils;
import org.openspcoop2.generic_project.web.iservice.IBaseService;


@Named("pspMBean") @ConversationScoped
public class PspMBean extends BaseListView<PspBean, Long, PspSearchForm,PspForm,PspModel> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private List<PspBean> listaPsp = null; 

	private @Inject Conversation conversation;

	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;

	private String selectedIdPsp = null;

	private CanaleMBean canaleMBean = null;
	
	public PspMBean(){
		super(Logger.getLogger(PspMBean.class)); 
	}

	@PostConstruct
	private void _PspMBean(){
		this.log.debug("Init PspMBean completato."); 

		this.selectedId = null;
		this.selectedIdPsp = null;
		this.canaleMBean = new CanaleMBean();
		this.canaleMBean.setPspService((IPspService)this.service);
	}

	@Override @Inject @Named("pspService")
	public void setService(IBaseService<PspBean, Long, PspSearchForm> service) {
		super.setService(service);
	}

	@Override @Inject @Named("pspSearchForm")
	public void setSearch(PspSearchForm search) {
		super.setSearch(search); 
	}; 

	@Override
	public void setSelectedElement(PspBean selectedElement) {
		super.setSelectedElement(selectedElement);

		if(selectedElement != null){
			this.selectedIdPsp  = selectedElement.getDTO().getRagioneSociale() + "";

			this.canaleMBean.setListaCanali(this.selectedElement.getListaCanali());
		}
	}

	public String menuAction(){
		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.GATEWAY_CID, this.conversation);
		this.search.reset();
		this.selectedId = null;
		this.selectedIdPsp = null;
		this.selectedElement = null;
		this.listaPsp = null;

		this.canaleMBean.setSelectedElement(null);
		this.canaleMBean.setListaCanali(new ArrayList<CanaleBean>());
		this.canaleMBean.setSelectedId(null);
		this.canaleMBean.setAzione(null);
		this.canaleMBean.setShowForm(false); 
		this.canaleMBean.setPspService((IPspService)this.service);

		return "listaPsp";
	}

	public String dettaglio(){
		return "listaPsp?faces-redirect=true";
	}

	public String showSearch(){
		return "listaPsp?faces-redirect=true";
	}

	// Action della pagina di ricerca
	public String filtra(){
		return "listaPsp?faces-redirect=true";
	}


	public String resetFiltro(){
		this.search.reset();
		return "listaPsp?faces-redirect=true";
	}

	public List<PspBean> getListaPsp(){
		try{
			if(this.listaPsp == null){
				this.listaPsp = new ArrayList<PspBean>();
				this.log.debug("Caricamento lista Psp in corso..."); 

				this.listaPsp = this.service.findAll();
				

				this.log.debug("Caricamento lista Psp completato.");
			}
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante il caricamento della lista Psp: " + e.getMessage(), e);
		}

		return this.listaPsp;
	}


	/**
	 * 
	 * chiama la procedura per l'aggioramento della lista Psp
	 * @return
	 */
	public String aggiorna(){
		try{
			this.log.debug("Aggiornamento lista Psp in corso..."); 

			((IPspService)this.service ).aggiornaListaPsp();

			this.log.debug("Aggiornamento lista Psp completato. ricarico la la lista...");

			this.listaPsp = new ArrayList<PspBean>();

			this.listaPsp = this.service.findAll();

			this.log.debug("Caricamento nuova lista Psp completato.");
			
			// Reset delle selezioni
			
			this.selectedId = null;
			this.selectedIdPsp = null;
			this.selectedElement = null;
			this.canaleMBean.setSelectedElement(null); 
			this.canaleMBean.setListaCanali(new ArrayList<CanaleBean>());
			this.canaleMBean.setSelectedId(null);
			this.canaleMBean.setAzione(null);
			this.canaleMBean.setShowForm(false); 
			
			
			MessageUtils.addInfoMsg("Aggiornamento della lista Psp completato con successo.");

		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante l'aggiornamento della lista Psp: " + e.getMessage(), e);
			MessageUtils.addErrorMsg("Impossibile completare l'aggiornamento della lista Psp. Riprovare piu' tardi");
		}

		return null;
	}

	public String getSelectedIdPsp() {
		return this.selectedIdPsp;
	}

	public void setSelectedIdPsp(String selectedIdPsp) {
		this.selectedIdPsp = selectedIdPsp;
	}

	public CanaleMBean getCanaleMBean() {
		return canaleMBean;
	}

	public void setCanaleMBean(CanaleMBean canaleMBean) {
		this.canaleMBean = canaleMBean;
	}

	


}
