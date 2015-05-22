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

import it.govpay.ndp.model.StazioneModel;
import it.govpay.web.console.anagrafica.bean.IntermediarioNdpBean;
import it.govpay.web.console.anagrafica.bean.StazioneBean;
import it.govpay.web.console.anagrafica.form.StazioneForm;
import it.govpay.web.console.anagrafica.form.StazioneSearchForm;
import it.govpay.web.console.anagrafica.iservice.IIntermediarioNdpService;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.core.MessageUtils;
import org.openspcoop2.generic_project.web.mbean.BaseMBean;

public class StazioneMBean extends BaseMBean<StazioneBean, String, StazioneSearchForm>  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log;

	private String azione = null;
	private StazioneForm form = null;

	private IIntermediarioNdpService ndpService ;

	private IntermediarioNdpBean selectedIntermediario = null;

	private boolean showForm = false;

	private String selectedIdIntermediario = null;

	private String selectedId = null;

	private List<StazioneBean> listaStazioni;

	private IntermediarioNdpMBean intNdpMBean = null;

	public StazioneMBean(){

		this.search = new StazioneSearchForm();
		this.search.reset();
		this.form = new StazioneForm();
		this.form.setMbean(this); 
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
		this.selectedIntermediario= null;
		this.selectedIdIntermediario= null; 
		this.selectedId = null;
		this.form.setRendered(this.showForm);
		this.form.reset();

	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public StazioneForm getForm() {
		return form;
	}

	public void setForm(StazioneForm form) {
		this.form = form;
	}

	public IIntermediarioNdpService getNdpService() {
		return ndpService;
	}

	public void setNdpService(IIntermediarioNdpService ndpService) {
		this.ndpService = ndpService;
	}

	public IntermediarioNdpBean getSelectedIntermediario() {
		return selectedIntermediario;
	}

	public void setSelectedIntermediario(IntermediarioNdpBean selectedIntermediario) {
		this.selectedIntermediario = selectedIntermediario;
		if(this.selectedIntermediario !=  null && this.selectedIntermediario.getDTO().getIdIntermediarioPA() != null){
			try {
				this.listaStazioni =  this.ndpService.findAllStazioni(this.selectedIntermediario.getDTO().getIdIntermediarioPA()); 
			} catch (ServiceException e) {
				log.error("Errore durante il caricamento della lista stazioni");
				this.listaStazioni = new ArrayList<StazioneBean>();
			}
		} else {
			this.listaStazioni = new ArrayList<StazioneBean>();
		}
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public String getSelectedIdIntermediario() {
		return selectedIdIntermediario;
	}

	public void setSelectedIdIntermediario(String selectedIdIntermediario) {
		this.selectedIdIntermediario = selectedIdIntermediario;
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public List<StazioneBean> getListaStazioni() {
		return listaStazioni;
	}

	public void setListaStazioni(List<StazioneBean> listaStazioni) {
		this.listaStazioni = listaStazioni;
	}

	public IntermediarioNdpMBean getIntNdpMBean() {
		return intNdpMBean;
	}

	public void setIntNdpMBean(IntermediarioNdpMBean intNdpMBean) {
		this.intNdpMBean = intNdpMBean;
	}

	@Override
	public void setSelectedElement(StazioneBean selectedElement) {
		super.setSelectedElement(selectedElement);

		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
	}

	public String invia(){

		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("stazione.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{

			boolean isAdd = !this.azione.equals("update");
			StazioneModel dto = this.form.getStazione();

			// Add
			if(isAdd){
				StazioneBean oldInt = this.ndpService.findStazioneById(dto.getIdStazioneIntermediarioPA());

				if(oldInt!= null){
					MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("stazione.form.erroreValidazione") +
							": " +Utils.getMessageWithParamsFromResourceBundle("stazione.form.stazioneEsistente",this.form.getCodice().getValue()));
					return null;
				}
			}

			StazioneBean bean =  new StazioneBean();
			bean.setDTO(dto);

			this.ndpService.salvaStazione(bean, isAdd);
			MessageUtils.addInfoMsg(Utils.getMessageFromResourceBundle("stazione.form.salvataggioOk"));

			this.setSelectedIntermediario(this.getSelectedIntermediario()); 
			this.setSelectedId(bean.getDTO().getIdStazioneIntermediarioPA());
			this.setSelectedElement(bean);

			return this.dettaglio();

		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio stazione: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("stazione.form.erroreGenerico"));
			//			return null;
		}
		return "listaIntermediariNdp?faces-redirect=true";
	}

	public String dettaglio(){
		if(this.selectedId != null && this.selectedIntermediario != null){

			StazioneBean findById = null;
			try {
				if(this.listaStazioni != null && this.listaStazioni.size() > 0){
					for (StazioneBean stazione : this.listaStazioni) {
						if(stazione.getIdIntermediarioPA().getValue().equals(this.selectedIntermediario.getDTO().getIdIntermediarioPA()) &&
								stazione.getIdStazioneIntermediarioPA().getValue().equals(this.selectedId))
						{
							findById = stazione;
							break;
						}
					}
				}

				if(findById == null)
					findById = ( this.ndpService).findStazioneById(this.selectedId);

				this.setSelectedElement(findById);
			} catch (Exception e) {
				this.log.error("Si e' verificato un errore durante la lettura della Stazione: " + e.getMessage(), e); 
			}
		}

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

		this.form.getIdIntermediarioPA().setDefaultValue(this.getSelectedIntermediario().getDTO().getIdIntermediarioPA());
		this.form.reset();


	}

	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		this.form.reset();

		return "listaIntermediariNdp?faces-redirect=true";
	}

	public String elimina(){

		return "listaIntermediariNdp?faces-redirect=true";
	}

}
