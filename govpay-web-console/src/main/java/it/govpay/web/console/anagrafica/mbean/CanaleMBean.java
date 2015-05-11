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

import it.govpay.web.console.anagrafica.bean.CanaleBean;
import it.govpay.web.console.anagrafica.form.PspForm;
import it.govpay.web.console.anagrafica.form.TributoSearchForm;
import it.govpay.web.console.anagrafica.iservice.IPspService;
import it.govpay.web.console.utils.Utils;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.web.core.MessageUtils;
import org.openspcoop2.generic_project.web.mbean.BaseMBean;

public class CanaleMBean extends BaseMBean<CanaleBean, Long, TributoSearchForm>{

	private boolean showForm = false;

	private String selectedId = null;

	private String azione = null;
	
	private PspForm form = null;

	private transient Logger log;
	
	private IPspService pspService  = null;
	
	private List<CanaleBean> listaCanali = null;
	
	
	public CanaleMBean(){
		this.selectedId = null;
		this.form = new PspForm();
		this.showForm = false;
		this.azione = null;
		this.form.setRendered(this.showForm);
		this.form.reset();
	}
	
	@Override
	public void setSelectedElement(CanaleBean selectedElement) {
		super.setSelectedElement(selectedElement);
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
		
		if(selectedElement != null){
			this.selectedId = selectedElement.getDTO().getIdPsp() + "";
		}
	}
	
	public String invia(){
		
		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("psp.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{
			boolean value = this.form.getStato().getValue() != null ? this.form.getStato().getValue().booleanValue() : false;
			
			this.selectedElement.getDTO().setAbilitato(value);
			
			this.service.store(selectedElement);
			
			MessageUtils.addInfoMsg(Utils.getMessageFromResourceBundle("psp.form.salvataggioOk"));

//			return null;//"invia";
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dell'psp: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("psp.form.erroreGenerico"));
//			return null;
		}
		return "listaPsp?faces-redirect=true";
	}
	
	public String dettaglio(){
		return "listaPsp?faces-redirect=true";
	}
	
	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public PspForm getForm() {
		return form;
	}

	public void setForm(PspForm form) {
		this.form = form;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}
	
	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);
		
		// Carica valori tendine
		
		this.form.reset();

		return "listaPsp?faces-redirect=true";
	}
	
	public String annulla(){
		if(this.azione.equals("update")){
			this.showForm = false;
			this.azione = null;
		} 
		this.form.setRendered(this.showForm); 
		this.form.reset();
		this.selectedId = null;
		
		return "listaPsp?faces-redirect=true";
	}

	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public IPspService getPspService() {
		return pspService;
	}

	public void setPspService(IPspService pspService) {
		this.pspService = pspService;
	}

	public List<CanaleBean> getListaCanali() {
		return listaCanali;
	}

	public void setListaCanali(List<CanaleBean> listaCanali) {
		this.listaCanali = listaCanali;
	}
	
	
}
