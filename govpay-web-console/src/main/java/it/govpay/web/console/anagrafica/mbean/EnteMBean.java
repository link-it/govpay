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

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.EnteCreditoreModel.EnumStato;
import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.anagrafica.bean.EnteBean;
import it.govpay.web.console.anagrafica.bean.IntermediarioNdpBean;
import it.govpay.web.console.anagrafica.form.EnteCRUDForm;
import it.govpay.web.console.anagrafica.form.EnteSearchForm;
import it.govpay.web.console.anagrafica.iservice.IEnteService;
import it.govpay.web.console.anagrafica.iservice.IIntermediarioNdpService;
import it.govpay.web.console.anagrafica.iservice.IScadenzarioService;
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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.BaseListView;
import org.openspcoop2.generic_project.web.impl.jsf1.utils.MessageUtils;
import org.openspcoop2.generic_project.web.iservice.IBaseService;

@Named("enteMBean")  @ConversationScoped
public class EnteMBean extends BaseListView<EnteBean, Long, EnteSearchForm,EnteCRUDForm,EnteCreditoreModel> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//	private OperatoreModel loggedUtente = null;

	@Inject @Named("intNdpService")
	private IIntermediarioNdpService intNdpService;

	@Inject @Named("scadenzarioService")
	private IScadenzarioService scadenzarioService;

	private boolean showForm = false;

	private String selectedIdEnte = null;

	private ScadenzarioMBean scadenzarioMBean = null;

	private @Inject Conversation conversation;

	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;

	private String azione = null;

	private List<SelectItem> listaStati;
	
	public EnteMBean(){
		super(org.apache.log4j.Logger.getLogger(EnteMBean.class));
	}

	@PostConstruct
	private void _EnteMBean(){
		
		this.log.debug("Init EnteMBean completato."); 

		//		this.loggedUtente = Utils.getLoggedUtente();

		this.scadenzarioMBean = new ScadenzarioMBean();
	//	this.scadenzarioMBean.setLog(this.log);
		this.scadenzarioMBean.setService(this.scadenzarioService);
		this.scadenzarioMBean.setEnteService((IEnteService) this.service);
		this.scadenzarioMBean.setNdpService(this.intNdpService); 
		this.scadenzarioMBean.setEnteMbean(this); 

		this.form = new EnteCRUDForm();
		this.showForm = false;
		this.azione = null;
		this.selectedId = null;
		this.selectedIdEnte = null;
		this.form.setRendered(this.showForm);

	}



	@Override @Inject @Named("enteService")
	public void setService(
			IBaseService<EnteBean, Long, EnteSearchForm> service) {
		super.setService(service);
	}

	@Override @Inject @Named("enteSearchForm")
	public void setSearch(EnteSearchForm search) {
		super.setSearch(search);
	}

	@Override
	public void setSelectedElement(EnteBean selectedElement) {
		super.setSelectedElement(selectedElement);
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
	}
	
	@Override @Inject @Named("enteForm")
	public void setForm(EnteCRUDForm form) {
		this.form = form;
	}

	public String menuAction(){
		this.log.debug("Click Menu' Enti");

		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.INTERMEDIARI_CID, this.conversation);
		this.showForm = false;
		this.azione = null;
		this.selectedId = null;
		this.selectedIdEnte = null;
		this.setSelectedElement(null); 
		this.form.setRendered(this.showForm);

		this.search.reset();

		this.scadenzarioMBean.setSelectedEnte(this.selectedElement);
		this.scadenzarioMBean.setSelectedElement(null);
		this.scadenzarioMBean.setSelectedId(null);
		this.scadenzarioMBean.setSelectedIdSystem(null);
		this.scadenzarioMBean.setAzione(null);
		this.scadenzarioMBean.setShowForm(false); 

		return "listaEnti";
	}

	public String invia(){

		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("ente.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{
			String oldId = null;
			List<TributoModel> listaTributi = new ArrayList<TributoModel>();

			DominioEnteModel dominioEnte = null;
			// Add
			if(!azione.equals("update")){
				EnteBean oldInt = ((IEnteService) this.service).findByIdFiscale(this.form.getIdFiscale().getValue());

				if(oldInt!= null){
					MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("ente.form.erroreValidazione") +
							": " +Utils.getInstance().getMessageWithParamsFromResourceBundle("ente.form.enteEsistente",this.form.getIdEnteCreditore().getValue()));
					return null;
				}


			} else {
				oldId = this.selectedElement.getDTO().getIdEnteCreditore();
				// la lista tributi non viene modificata
				listaTributi = this.selectedElement.getDTO().getTributiGestiti();
				// prendo se presente il dominio ente
				dominioEnte = this.selectedElement.getDominioEnte();
			}

			EnteCreditoreModel newEnte = this.form.getEnteCreditore();
			newEnte.setIdEnteCreditore(oldId); 


			if(dominioEnte == null && this.form.getAbilitaNodoPagamento().getValue()){
				dominioEnte = new DominioEnteModel();
				dominioEnte.setIdDominio(this.form.getIdFiscale().getValue());
			} 

			newEnte.setTributiGestiti(listaTributi); 
			EnteBean bean = new EnteBean();
			bean.setDTO(newEnte);

			((IEnteService)this.service).store(oldId,bean, dominioEnte);
			MessageUtils.addInfoMsg(Utils.getInstance().getMessageFromResourceBundle("ente.form.salvataggioOk"));
			this.setSelectedIdEnte(bean.getIdEnteCreditore().getValue()); 
			this.setSelectedElement(bean);

			//			return null;//"invia";
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'ente: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getInstance().getMessageFromResourceBundle("ente.form.erroreGenerico"));
			return null;
		}
		return "listaEnti?faces-redirect=true";
	}

	public String dettaglio(){
		this.scadenzarioMBean.setSelectedElement(null);
		this.scadenzarioMBean.setSelectedId(null);
		this.scadenzarioMBean.setAzione(null);
		this.scadenzarioMBean.setShowForm(false); 

		return "listaEnti?faces-redirect=true";
	}

	public String showSearch(){
		return "listaEnti?faces-redirect=true";
	}

	// Action della pagina di ricerca
	public String filtra(){
		return "listaEnti?faces-redirect=true";
	}


	public String resetFiltro(){
		this.search.reset();
		return "listaEnti?faces-redirect=true";
	}

	public String annulla(){
		if(this.azione.equals("update")){
			this.showForm = false;
			this.azione = null;
		} 
		this.form.setRendered(this.showForm); 
		this.form.reset();

		return "listaEnti?faces-redirect=true";
	}

	/**
	 * Listener eseguito prima di aggiungere un nuovo ricerca, setta a null il selectedElement
	 * in modo da "scordarsi" i valori gia' impostati.
	 * @param ae
	 */
	public void addNewListener(ActionEvent ae){
		super.addNewListener(ae);
		this.selectedElement = null;
		this.selectedId = null;
		this.selectedIdEnte = null;
		this.azione = "add";
		this.showForm = true;
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		this.form.reset();

		this.scadenzarioMBean.setSelectedEnte(null);
		this.scadenzarioMBean.setSelectedElement(null);
		this.scadenzarioMBean.setSelectedId(null);
		this.scadenzarioMBean.setAzione(null);
		this.scadenzarioMBean.setShowForm(false); 
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



	public String getSelectedIdEnte() {
		return selectedIdEnte;
	}

	public void setSelectedIdEnte(String selectedId) {
		this.selectedIdEnte = selectedId;

		if(this.selectedIdEnte != null){
			try {
				EnteBean findById = ((IEnteService)this.service).findById(selectedId);
				this.setSelectedElement(findById);

				if(this.selectedElement != null){
					DominioEnteModel dominioEnte = ((IEnteService)this.service).getDominioEnte(this.selectedElement.getIdEnteCreditore().getValue());
					this.selectedElement.setDominioEnte(dominioEnte);
				}
			} catch (ServiceException e) {
				this.log.error("Si e' verificato un errore durante la lettura dell'IntermediarioNDP: " + e.getMessage(), e); 
			}
		}

		this.scadenzarioMBean.setSelectedEnte(this.selectedElement);
	}

	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);

		// Carica valori tendine
		this.form.reset();

		this.scadenzarioMBean.setSelectedEnte(this.selectedElement);
		this.scadenzarioMBean.setSelectedElement(null);
		this.scadenzarioMBean.setSelectedId(null);
		this.scadenzarioMBean.setAzione(null);
		this.scadenzarioMBean.setShowForm(false); 

		return "listaEnti?faces-redirect=true";
	}

	public String elimina(){

		return "listaEnti?faces-redirect=true";
	}

	public List<SelectItem> getListaStati(){
		if(this.listaStati == null){
			this.listaStati = new ArrayList<SelectItem>();
			this.listaStati.add(new SelectItem(
					new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(EnumStato.A.name(),
							 Utils.getInstance().getMessageFromResourceBundle("commons.label.attivo"))));
			this.listaStati.add(new SelectItem(
					new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(EnumStato.D.name(), Utils.getInstance().getMessageFromResourceBundle("commons.label.nonAttivo"))));
		}

		return this.listaStati;
	}

	public List<SelectItem> getListaIdIntermediari(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
						CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)));

		try {
			List<IntermediarioNdpBean> findAll = this.intNdpService.findAll();

			if(findAll  != null && findAll.size() > 0){
				for (IntermediarioNdpBean intermediarioNdpBean : findAll) {
					String id = intermediarioNdpBean.getIdIntermediarioPA().getValue();
					String label = intermediarioNdpBean.getNomeSoggettoSPC().getValue();
					lista.add(new SelectItem(
							new org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(id,label + " (" + id + ")")));
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante la findAll intermediari:" + e.getMessage(),e);
		}

		return lista;
	}

	public IScadenzarioService getScadenzarioService() {
		return scadenzarioService;
	}
	public void setScadenzarioService(IScadenzarioService scadenzarioService) {
		this.scadenzarioService = scadenzarioService;
	}
	public ScadenzarioMBean getScadenzarioMBean() {
		return scadenzarioMBean;
	}
	public void setScadenzarioMBean(ScadenzarioMBean scadenzarioMBean) {
		this.scadenzarioMBean = scadenzarioMBean;
	}
}
