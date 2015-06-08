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
package it.govpay.web.console.pagamenti.mbean;

import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.pagamenti.bean.DistintaBean;
import it.govpay.web.console.pagamenti.bean.PagamentoBean;
import it.govpay.web.console.pagamenti.form.DistintaSearchForm;
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

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.iservice.IBaseService;
import org.openspcoop2.generic_project.web.mbean.BaseMBean;

@Named("distintaMBean") @ConversationScoped
public class DistintaMBean extends BaseMBean<DistintaBean, Long, DistintaSearchForm> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject  
	private transient Logger log;

	// Usato per visualizzare il form di invio
	private boolean showForm = false;

	private Long selectedId = null;

	private String selectedPagamentoId = null;

	private PagamentoBean selectedPagamento = null;

	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;

	@Inject
	Conversation conversation;

	private PagamentoBean metadataPagamento = null;

	// Stato Distinta
	private List<SelectItem> listaStatoDistinta = null;
	// Periodi temporali
	private List<SelectItem> listaPeriodoTemporale = null;


	@PostConstruct
	private void _DistintaMBean(){
		log.debug("Init DistintaMBean completato. ID["+this.toString()+"]"); 

		this.metadataPagamento = new PagamentoBean();
		this.showForm = false;
		this.selectedId = null;
		this.selectedPagamentoId = null;
		this.selectedPagamento = null;

	}

	@Override @Inject @Named("distintaService")
	public void setService(
			IBaseService<DistintaBean, Long, DistintaSearchForm> service) {
		super.setService(service);
	}

	@Override @Inject @Named("distintaSearchForm")
	public void setSearch(DistintaSearchForm search) {
		this.search = search;
		this.search.setmBean(this);
		this.search.getStatoDistinta().setElencoSelectItems(this.getListaStatoDistinta());
		this.search.getDataPeriodo().setElencoSelectItems(this.getListaPeriodoTemporale());

		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.DISTINTA_CID, this.conversation);
		//this.search.reset();
		this.selectedElement = null;
		this.selectedPagamento = null;
		log.debug("Set distintaSearchForm completato. IDForm["+search.hashCode()+"]"); 
	}

	public String filtra(){
		return "listaDistinte?faces-redirect=true";
	}

	public String menuAction(){
		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.DISTINTA_CID, this.conversation);

		this.search.reset();
		
		this.showForm = false;
		this.selectedId = null;
		this.selectedPagamentoId = null;
		this.selectedPagamento = null;
		this.selectedElement = null;

		return "listaDistinte";
	}

	public String restoreSearch(){
		this.search.setRestoreSearch(true); 

		return "listaDistinte";
	}


	public String resetFiltro(){
		this.search.reset();

		return "listaDistinte?faces-redirect=true";
	}

	@Override
	public void setSelectedElement(DistintaBean selectedElement) {
		super.setSelectedElement(selectedElement);
		this.showForm = false;
		this.selectedPagamento = null;
		this.selectedPagamentoId = null;
	}

	public String dettaglio(){
		if(this.selectedId != null){
			try {
				DistintaBean findById =  this.service.findById(selectedId);
				this.setSelectedElement(findById);
			} catch (ServiceException e) {
				this.log.error("Si e' verificato un errore durante la lettura della distinta: " + e.getMessage(), e); 
			}
		}

		return "listaDistinte?faces-redirect=true";
	}

	public String dettaglioPagamento(){
		if(this.selectedPagamentoId != null){
			try {
				if(this.selectedElement.getListaPagamenti() != null && this.selectedElement.getListaPagamenti().size() > 0){

					for (PagamentoBean pag : this.selectedElement.getListaPagamenti()) {
						if(pag.getDTO().getIdentificativo().equals(this.selectedPagamentoId)){
							this.selectedPagamento = pag;
							break;
						}
					}
				}
			} catch (Exception e) {
				this.log.error("Si e' verificato un errore durante la lettura del singolo pagamento: " + e.getMessage(), e); 
			}
		}

		return "listaDistinte?faces-redirect=true";
	}


	/**
	 * Listener eseguito prima di aggiungere un nuovo ricerca, setta a null il selectedElement
	 * in modo da "scordarsi" i valori gia' impostati.a
	 * @param ae
	 */
	public void addNewListener(ActionEvent ae){
		super.addNewListener(ae);
		this.selectedElement = null;
		this.selectedPagamento = null;
		this.selectedId = null;
		this.selectedPagamentoId = null;
		this.showForm = true;
	}



	public List<SelectItem> getListaPeriodoTemporale() {
		if (this.listaPeriodoTemporale == null) {
			this.listaPeriodoTemporale = new ArrayList<SelectItem>();

			this.listaPeriodoTemporale.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(DistintaSearchForm.DATA_PERIODO_ULTIMA_SETTIMANA,Utils.getMessageFromResourceBundle("distinta.search.data.ultimaSettimana"))));
			this.listaPeriodoTemporale.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(DistintaSearchForm.DATA_PERIODO_ULTIMO_MESE,Utils.getMessageFromResourceBundle("distinta.search.data.ultimoMese"))));
			this.listaPeriodoTemporale.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(DistintaSearchForm.DATA_PERIODO_ULTIMI_TRE_MESI,Utils.getMessageFromResourceBundle("distinta.search.data.ultimiTreMesi"))));
			this.listaPeriodoTemporale.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(DistintaSearchForm.DATA_PERIODO_PERSONALIZZATO,Utils.getMessageFromResourceBundle("distinta.search.data.personalizzato"))));

		}

		return this.listaPeriodoTemporale;
	}

	public List<SelectItem> getListaStatoDistinta() {
		if(this.listaStatoDistinta == null){
			this.listaStatoDistinta = new ArrayList<SelectItem>();


			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(CostantiForm.ALL,"[Qualsiasi]")));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.ESEGUITO.getChiave(), EnumStatoDistinta.ESEGUITO.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.ESEGUITO_SBF.getChiave(), EnumStatoDistinta.ESEGUITO_SBF.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.ANNULLATO.getChiave(), EnumStatoDistinta.ANNULLATO.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.ANNULLATO_OPE.getChiave(), EnumStatoDistinta.ANNULLATO_OPE.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.IN_CORSO.getChiave(), EnumStatoDistinta.IN_CORSO.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.IN_ERRORE.getChiave(), EnumStatoDistinta.IN_ERRORE.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.NON_ESEGUITO.getChiave(), EnumStatoDistinta.NON_ESEGUITO.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.PARZIALMENTE_ESEGUITO.getChiave(), EnumStatoDistinta.PARZIALMENTE_ESEGUITO.getDescrizione())));
			this.listaStatoDistinta.add(new SelectItem(
					new org.openspcoop2.generic_project.web.form.field.SelectItem(EnumStatoDistinta.STORNATO.getChiave(), EnumStatoDistinta.STORNATO.getDescrizione())));
		}

		return listaStatoDistinta; 
	}

	public void setListaStatoDistinta(List<SelectItem> listaStatoDistinta) {
		this.listaStatoDistinta = listaStatoDistinta;
	}

	public List<org.openspcoop2.generic_project.web.form.field.SelectItem> soggettoVersanteAutoComplete(Object val) {
		List<org.openspcoop2.generic_project.web.form.field.SelectItem> lst = new ArrayList<org.openspcoop2.generic_project.web.form.field.SelectItem>();

		org.openspcoop2.generic_project.web.form.field.SelectItem item0 = 
				new  org.openspcoop2.generic_project.web.form.field.SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO);

		//		try{
		//			if(val==null || StringUtils.isEmpty((String)val) || ((String)val).equals(BaseForm.NON_SELEZIONATO))
		//				lst = new ArrayList<org.openspcoop2.generic_project.web.form.field.SelectItem>();
		//			else{
		//				List<String> listaMittenti = ((IFatturaElettronicaService)this.service).getMittenteAutoComplete((String)val);
		//
		//				if(listaMittenti != null && listaMittenti.size() > 0){
		//					for (String string : listaMittenti) {
		//						lst.add(new  org.openspcoop2.generic_project.web.form.field.SelectItem(string,string));
		//					}
		//				}
		//			}
		//
		//		}catch(Exception e ){
		//
		//		}
		// Inserisco l'elemento nullo in cima
		lst.add(0, item0);

		return lst;
	}

	public Long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	public PagamentoBean getMetadataPagamento() {
		return metadataPagamento;
	}

	public void setMetadataPagamento(PagamentoBean metadataPagamento) {
		this.metadataPagamento = metadataPagamento;
	}

	public String getSelectedPagamentoId() {
		return selectedPagamentoId;
	}

	public void setSelectedPagamentoId(String selectedPagamentoId) {
		this.selectedPagamentoId = selectedPagamentoId;
	}

	public PagamentoBean getSelectedPagamento() {
		return selectedPagamento;
	}

	public void setSelectedPagamento(PagamentoBean selectedPagamento) {
		this.selectedPagamento = selectedPagamento;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}
}
