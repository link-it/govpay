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

import it.govpay.ejb.model.TributoModel;
import it.govpay.web.console.anagrafica.bean.EnteBean;
import it.govpay.web.console.anagrafica.bean.ScadenzarioBean;
import it.govpay.web.console.anagrafica.bean.TributoBean;
import it.govpay.web.console.anagrafica.form.TributoForm;
import it.govpay.web.console.anagrafica.form.TributoSearchForm;
import it.govpay.web.console.anagrafica.iservice.IEnteService;
import it.govpay.web.console.anagrafica.iservice.IScadenzarioService;
import it.govpay.web.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.core.MessageUtils;
import org.openspcoop2.generic_project.web.mbean.BaseMBean;

public class TributoMBean extends BaseMBean<TributoBean, String, TributoSearchForm> { 

	private boolean showForm = false;

	private String selectedId = null;

	private String azione = null;

	private TributoForm form = null;

	private transient Logger log;

	private IEnteService enteService= null;

	private IScadenzarioService scadenzarioService = null;

	private List<TributoBean> listaTributi;

	private EnteBean selectedEnte = null;
	private EnteMBean enteMbean = null;
	
	private ScadenzarioBean selectedScadenzario = null;
	private ScadenzarioMBean scadenzarioMbean = null;
	
	public TributoMBean(){
		

		this.search = new TributoSearchForm();
		this.form = new TributoForm();
		this.showForm = false;
		this.azione = null;
		this.selectedId = null;
		this.selectedEnte = null;
		this.selectedScadenzario = null;
		this.form.setRendered(this.showForm);

	}

	public void setLogger(Logger log){
		this.log = log; 
	}

	@Override
	public void setSelectedElement(TributoBean selectedElement) {
		super.setSelectedElement(selectedElement);

		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;

	}



	public String invia(){

		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("tributo.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{
			String oldId = null;
			// Add
			if(!azione.equals("update")){
				TributoBean oldInt = ((IEnteService) this.enteService).findTributoById(this.selectedEnte.getIdEnteCreditore().getValue(),this.form.getCodice().getValue());

				if(oldInt!= null){
					MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("tributo.form.erroreValidazione") +
							": " +Utils.getMessageWithParamsFromResourceBundle("tributo.form.tributoEsistente",this.form.getIdEnteCreditore().getValue()));
					return null;
				}
			} else {
				oldId = this.selectedElement.getDTO().getCodiceTributo();
			}

			TributoModel newEnte = this.form.getTributo();
			newEnte.setIdEnteCreditore(this.selectedEnte.getIdEnteCreditore().getValue());
			
			TributoBean bean = new TributoBean();
			bean.setDTO(newEnte);

			((IEnteService)this.enteService).storeTributo(newEnte.getIdEnteCreditore(),oldId,bean);
			MessageUtils.addInfoMsg(Utils.getMessageFromResourceBundle("tributo.form.salvataggioOk"));
			
			// Aggiorno la tabella dei tributi
			this.enteMbean.setSelectedId(this.selectedEnte.getDTO().getIdEnteCreditore());
			
			this.setSelectedScadenzario(this.getSelectedScadenzario()); 
			this.setSelectedElement(bean);

			return this.dettaglio();
			//			return null;//"invia";
			
			
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio tributo: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("tributo.form.erroreGenerico"));
			//			return null;
		}
		return "listaEnti?faces-redirect=true";
	}

	public String dettaglio(){
		if(this.selectedId != null && this.selectedEnte != null){

			TributoBean findById = null;
			try {
				if(this.listaTributi != null && this.listaTributi.size() > 0){
					for (TributoBean tr : this.listaTributi) {
						if(tr.getIdEnteCreditore().getValue().equals(this.selectedEnte.getDTO().getIdEnteCreditore()) &&
								tr.getCodiceTributo().getValue().equals(this.selectedId))
						{
							findById = tr;
							break;
						}
					}
				}

				if(findById == null)
					findById = ((IEnteService) this.enteService).findTributoById(this.form.getIdEnteCreditore().getValue(),this.form.getCodice().getValue());

				this.setSelectedElement(findById);
			} catch (ServiceException e) {
				this.log.error("Si e' verificato un errore durante la lettura dell'IntermediarioNDP: " + e.getMessage(), e); 
			}
		}

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
		this.azione = "add";
		this.showForm = true;
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		
		
		org.openspcoop2.generic_project.web.form.field.SelectItem defEnte = 
				new org.openspcoop2.generic_project.web.form.field.SelectItem(this.selectedScadenzario.getIdSistemaEnte().getIdSystem(),this.selectedScadenzario.getIdSistemaEnte().getIdSystem());
		this.form.getScadenzario().setDefaultValue(defEnte);

		ArrayList<SelectItem> elencoSelectItems = new ArrayList<SelectItem>();
		elencoSelectItems.add(new SelectItem(defEnte));
		
		this.form.getScadenzario().setElencoSelectItems(elencoSelectItems);
		
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

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);

		// Carica valori tendine
		org.openspcoop2.generic_project.web.form.field.SelectItem defEnte = 
				new org.openspcoop2.generic_project.web.form.field.SelectItem(this.selectedScadenzario.getIdSistemaEnte().getIdSystem(),this.selectedScadenzario.getIdSistemaEnte().getIdSystem());
		this.form.getScadenzario().setDefaultValue(defEnte);

		ArrayList<SelectItem> elencoSelectItems = new ArrayList<SelectItem>();
		elencoSelectItems.add(new SelectItem(defEnte));
		
		this.form.getScadenzario().setElencoSelectItems(elencoSelectItems);

		this.form.reset();

		return "listaEnti?faces-redirect=true";
	}

	public String elimina(){

		return "listaEnti?faces-redirect=true";
	}

	public TributoForm getForm() {
		return form;
	}

	public void setForm(TributoForm form) {
		this.form = form;
	}

	public List<TributoBean> getListaTributi() {
		return listaTributi;
	}

	public void setListaTributi(List<TributoBean> listaTributi) {
		this.listaTributi = listaTributi;
	}
	public EnteBean getSelectedEnte() {
		return selectedEnte;
	}

	public void setSelectedEnte(EnteBean selectedEnte) {
		this.selectedEnte = selectedEnte;
	}

	public IScadenzarioService getScadenzarioService() {
		return scadenzarioService;
	}

	public void setScadenzarioService(IScadenzarioService scadenzarioService) {
		this.scadenzarioService = scadenzarioService;
	}

	public IEnteService getEnteService() {
		return enteService;
	}

	public void setEnteService(IEnteService enteService) {
		this.enteService = enteService;
	}

	public EnteMBean getEnteMbean() {
		return enteMbean;
	}

	public void setEnteMbean(EnteMBean enteMbean) {
		this.enteMbean = enteMbean;
	}

	public ScadenzarioBean getSelectedScadenzario() {
		return selectedScadenzario;
	}

	public void setSelectedScadenzario(ScadenzarioBean selectedScadenzario) {
		this.selectedScadenzario = selectedScadenzario;
		
		if(this.selectedScadenzario != null ){
			try {
				this.listaTributi = this.enteService.getTributi(this.selectedEnte.getIdEnteCreditore().getValue(),this.selectedScadenzario.getIdSistemaEnte().getIdSystem());
			} catch (ServiceException e) {
				log.error("Si e' verificato un errore durante la lettura della lista tributi: "  + e.getMessage(), e);
				this.listaTributi = new ArrayList<TributoBean>();
			}
		
		} else {
			this.listaTributi = new ArrayList<TributoBean>();
		}
	}

	public ScadenzarioMBean getScadenzarioMbean() {
		return scadenzarioMbean;
	}

	public void setScadenzarioMbean(ScadenzarioMBean scadenzarioMbean) {
		this.scadenzarioMbean = scadenzarioMbean;
	}

	
	

}
