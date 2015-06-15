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

import it.govpay.ejb.core.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.core.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.anagrafica.bean.IntermediarioNdpBean;
import it.govpay.web.console.anagrafica.form.IntermediarioNdpForm;
import it.govpay.web.console.anagrafica.form.IntermediarioNdpSearchForm;
import it.govpay.web.console.anagrafica.iservice.IIntermediarioNdpService;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.impl.SelectListImpl;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.BaseListView;
import org.openspcoop2.generic_project.web.impl.jsf1.utils.MessageUtils;
import org.openspcoop2.generic_project.web.iservice.IBaseService;

@Named("intNdpMBean") @ConversationScoped 
public class IntermediarioNdpMBean 
extends BaseListView<IntermediarioNdpBean, Long, IntermediarioNdpSearchForm,IntermediarioNdpForm,IntermediarioModel> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String azione = null;

	private boolean showForm = false;

	private String selectedIdIntNdp = null;
	
	private @Inject Conversation conversation;
	
	private StazioneMBean stazioneMBean;

	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;

	public IntermediarioNdpMBean() {
		super(Logger.getLogger(IntermediarioNdpMBean.class)); 
	}
	
	@PostConstruct
	private void _IntermediarioNdpMBean(){
		this.log.debug("Init IntermediarioNdpMBean completato."); 

		this.stazioneMBean = new StazioneMBean();
		this.stazioneMBean.setNdpService((IIntermediarioNdpService) this.service);
		this.form = new IntermediarioNdpForm();
		this.showForm = false;
		this.azione = null;
		this.selectedId = null;
		this.selectedIdIntNdp = null;
		this.form.setRendered(this.showForm);
		((SelectListImpl) this.form.getConnettore().getAutenticazione()).setElencoSelectItems(this.getListaTipiAutenticazione());
		((SelectListImpl) this.form.getConnettore().getTipoSsl()).setElencoSelectItems(this.getListaTipiSSL());
		this.form.reset(); 
	}

	@Override @Inject @Named("intNdpService")
	public void setService(
			IBaseService<IntermediarioNdpBean, Long, IntermediarioNdpSearchForm> service) {
		super.setService(service);
	}

	@Override @Inject @Named("intNdpSearchForm")
	public void setSearch(IntermediarioNdpSearchForm search) {
		super.setSearch(search);
	}
	
	@Override 	@Inject @Named("intNdpForm")
	public void setForm(IntermediarioNdpForm form) {
		super.setForm(form);
	}

	@Override
	public void setSelectedElement(IntermediarioNdpBean selectedElement) {
		super.setSelectedElement(selectedElement);
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
	}
	
	public String menuAction(){
		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.INTERMEDIARI_CID, this.conversation);
		this.search.reset();
		
		this.showForm = false;
		this.azione = null;
		this.selectedId = null;
		this.selectedIdIntNdp = null;
		this.form.setRendered(this.showForm);
		
		this.stazioneMBean.setSelectedIntermediario(this.selectedElement);
		this.stazioneMBean.setSelectedElement(null);
		this.stazioneMBean.setSelectedId(null);
		this.stazioneMBean.setSelectedIdIntermediario(null);
		this.stazioneMBean.setAzione(null);
		this.stazioneMBean.setShowForm(false); 
		
		return "listaIntermediariNdp";
	}

	public String invia(){
		
		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("intermediario.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{
			String oldId = null;
			// Add
			if(!azione.equals("update")){
				IntermediarioNdpBean oldInt = ((IIntermediarioNdpService) this.service).findById(this.form.getIdIntermediarioPA().getValue());

				if(oldInt!= null){
					MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("intermediario.form.erroreValidazione") +
							": " +Utils.getInstance().getMessageWithParamsFromResourceBundle("intermediario.form.intermediarioEsistente",this.form.getIdIntermediarioPA().getValue()));
					return null;
				}
			} else {
				oldId = this.selectedElement.getDTO().getIdIntermediarioPA();
			}

			IntermediarioModel newDip = this.form.getIntermediario();
			IntermediarioNdpBean bean = new IntermediarioNdpBean();
			bean.setDTO(newDip);

			((IIntermediarioNdpService)this.service).store(oldId,bean);
			MessageUtils.addInfoMsg(Utils.getInstance().getMessageFromResourceBundle("intermediario.form.salvataggioOk"));
			
			this.setSelectedElement(bean);

//			return null;//"invia";
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'intermediario: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("intermediario.form.erroreGenerico"));
//			return null;
		}
		return "listaIntermediariNdp?faces-redirect=true";
	}
	
	public String dettaglio(){
		
		this.stazioneMBean.setSelectedElement(null);
		this.stazioneMBean.setSelectedId(null);
		this.stazioneMBean.setAzione(null);
		this.stazioneMBean.setShowForm(false); 
		
		return "listaIntermediariNdp?faces-redirect=true";
	}

	public String showSearch(){
		return "listaIntermediariNdp?faces-redirect=true";
	}
	
	// Action della pagina di ricerca
	public String filtra(){
		return "listaIntermediariNdp?faces-redirect=true";
	}


	public String resetFiltro(){
		this.search.reset();
		return "listaIntermediariNdp?faces-redirect=true";
	}

	public String annulla(){
		if(this.azione.equals("update")){
			this.showForm = false;
			this.azione = null;
		} 
		this.form.setRendered(this.showForm); 
		this.form.reset();
		
		return "listaIntermediariNdp?faces-redirect=true";
	}

	/**
	 * Listener eseguito prima di aggiungere un nuovo ricerca, setta a null il selectedElement
	 * in modo da "scordarsi" i valori gia' impostati.
	 * @param ae
	 */
	public void addNewListener(ActionEvent ae){
		super.addNewListener(ae);
		this.selectedElement = null;
		this.azione = "add";
		this.showForm = true;
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		
		this.stazioneMBean.setSelectedIntermediario(null);
		this.stazioneMBean.setSelectedElement(null);
		this.stazioneMBean.setSelectedId(null);
		this.stazioneMBean.setSelectedIdIntermediario(null);
		this.stazioneMBean.setAzione(null);
		this.stazioneMBean.setShowForm(false); 
		
		this.form.reset();
		
		
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	
	
	public String getSelectedIdIntNdp() {
		return selectedIdIntNdp;
	}

	public void setSelectedIdIntNdp(String selectedId) {
		this.selectedIdIntNdp = selectedId;

		if(this.selectedIdIntNdp != null){
			try {
				IntermediarioNdpBean findById = ((IIntermediarioNdpService)this.service).findById(selectedId);
				this.setSelectedElement(findById);
			} catch (ServiceException e) {
				this.log.error("Si e' verificato un errore durante la lettura dell'IntermediarioNDP: " + e.getMessage(), e); 
			}
		}
		
		this.stazioneMBean.setSelectedIntermediario(this.selectedElement);
	}

	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		
		
		this.stazioneMBean.setSelectedIntermediario(this.selectedElement);
		this.stazioneMBean.setSelectedElement(null);
		this.stazioneMBean.setSelectedId(null);
		this.stazioneMBean.setAzione(null);
		this.stazioneMBean.setShowForm(false); 
		
		// Carica valori tendine
		
		this.form.reset();

		return "listaIntermediariNdp?faces-redirect=true";
	}
	
	public String elimina(){

		return "listaIntermediariNdp?faces-redirect=true";
	}


	public List<SelectItem> getListaTipiAutenticazione(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						EnumAuthType.HTTPBasic.toString(),   ("connettore.autenticazione.http"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						EnumAuthType.SSL.toString(),   ("connettore.autenticazione.ssl"))));
		
		return lista;
	}
	
	public List<SelectItem> getListaTipiSSL(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)));
		
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						EnumSslType.CLIENT.toString(), ("connettore.autenticazione.ssl.tipoSsl.client"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						EnumSslType.SERVER.toString(),   ("connettore.autenticazione.ssl.tipoSsl.server"))));
		
		return lista;
	}

	public StazioneMBean getStazioneMBean() {
		return stazioneMBean;
	}

	public void setStazioneMBean(StazioneMBean stazioneMBean) {
		this.stazioneMBean = stazioneMBean;
	}

}
