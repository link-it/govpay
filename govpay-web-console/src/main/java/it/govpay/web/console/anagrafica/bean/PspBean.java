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
package it.govpay.web.console.anagrafica.bean;

import it.govpay.web.console.anagrafica.model.PspModel;
import it.govpay.web.console.anagrafica.model.PspModel.CanaleModel;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputDate;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class PspBean extends BaseBean<PspModel, Long>{

	private OutputField<String> ragioneSociale= null;
	private OutputField<String> informazioni= null;
	private OutputDate inizioValidita= null;
	private OutputDate fineValidita= null;

	private OutputGroup fieldsDatiGenerali = null;
	
	private List<CanaleBean> listaCanali = null;

	public PspBean(){
		
		this.ragioneSociale = new OutputText();
		this.ragioneSociale.setLabel(Utils.getMessageFromResourceBundle("psp.ragioneSociale"));
		this.ragioneSociale.setName("ragioneSociale");
		
		this.informazioni = new OutputText();
		this.informazioni.setLabel(Utils.getMessageFromResourceBundle("psp.informazioni"));
		this.informazioni.setName("informazioni");
		
		this.inizioValidita = new OutputDate();
		this.inizioValidita.setLabel(Utils.getMessageFromResourceBundle("psp.inizioValidita"));
		this.inizioValidita.setName("inizioValidita");
		
		this.fineValidita = new OutputDate();
		this.fineValidita.setLabel(Utils.getMessageFromResourceBundle("psp.fineValidita"));
		this.fineValidita.setName("fineValidita");
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGenerali");
		this.fieldsDatiGenerali.setColumns(4);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx,labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.ragioneSociale);
		this.fieldsDatiGenerali.addField(this.inizioValidita);
		
		this.fieldsDatiGenerali.addField(this.informazioni);
		this.fieldsDatiGenerali.addField(this.fineValidita);
		
		this.listaCanali = new ArrayList<CanaleBean>();
	 	
	}

	@Override
	public void setDTO(PspModel dto) {
		super.setDTO(dto);

		this.ragioneSociale.setValue(this.getDTO().getRagioneSociale());
		this.informazioni.setValue(this.getDTO().getInformazioni());
		this.inizioValidita.setValue(this.getDTO().getInizioValidita());
		this.fineValidita.setValue(this.getDTO().getFineValidita());
		
		for (CanaleModel canaleModel : this.getDTO().getCanali()) {
			CanaleBean bean = new CanaleBean();
			bean.setDTO(canaleModel);
			
			this.listaCanali.add(bean);
		} 
	}

	public OutputField<String> getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(OutputField<String> ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public List<CanaleBean> getListaCanali() {
		return listaCanali;
	}

	public void setListaCanali(List<CanaleBean> listaCanali) {
		this.listaCanali = listaCanali;
	}

	public OutputField<String> getInformazioni() {
		return informazioni;
	}

	public void setInformazioni(OutputField<String> informazioni) {
		this.informazioni = informazioni;
	}

	public OutputDate getInizioValidita() {
		return inizioValidita;
	}

	public void setInizioValidita(OutputDate inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public OutputDate getFineValidita() {
		return fineValidita;
	}

	public void setFineValidita(OutputDate fineValidita) {
		this.fineValidita = fineValidita;
	}
	
	
	
	
	
}
