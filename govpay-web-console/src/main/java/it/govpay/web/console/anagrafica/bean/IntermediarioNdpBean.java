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

import it.govpay.ndp.model.IntermediarioModel;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class IntermediarioNdpBean extends BaseBean<IntermediarioModel, String>  {

	
	private OutputGroup fieldsDatiGenerali = null;
	private OutputField<String> nomeSoggettoSPC;
	private OutputField<String> idIntermediarioPA;
	
	private ConnettoreBean connettore = null;
	
	private Long id = null;
	
	public IntermediarioNdpBean(){
		
		this.nomeSoggettoSPC = new OutputText();
		this.nomeSoggettoSPC.setLabel(Utils.getMessageFromResourceBundle("intermediariNdp.nomeSoggettoSPC"));
		this.nomeSoggettoSPC.setName("nomeSoggettoSPC");
		
		this.idIntermediarioPA = new OutputText();
		this.idIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("intermediariNdp.idIntermediarioPA"));
		this.idIntermediarioPA.setName("idIntermediarioPA");
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");
		
		this.fieldsDatiGenerali.addField(this.nomeSoggettoSPC);
		this.fieldsDatiGenerali.addField(this.idIntermediarioPA);
		
		this.connettore = new ConnettoreBean();
		this.connettore.setConnettorePdd(true); 
	}
	
	@Override
	public void setDTO(IntermediarioModel dto) {
		super.setDTO(dto);
		
		this.idIntermediarioPA.setValue(this.getDTO().getIdIntermediarioPA());
		this.nomeSoggettoSPC.setValue(this.getDTO().getNomeSoggettoSPC());
		//this.password.setValue(this.getDTO().getPassword());
		this.connettore.setDTO(this.getDTO().getConnettoreServizioRPT());
	}


	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}


	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getNomeSoggettoSPC() {
		return nomeSoggettoSPC;
	}


	public void setNomeSoggettoSPC(OutputField<String> nomeSoggettoSPC) {
		this.nomeSoggettoSPC = nomeSoggettoSPC;
	}


	public OutputField<String> getIdIntermediarioPA() {
		return idIntermediarioPA;
	}


	public void setIdIntermediarioPA(OutputField<String> idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public ConnettoreBean getConnettore() {
		return connettore;
	}


	public void setConnettore(ConnettoreBean connettore) {
		this.connettore = connettore;
	}


 
	
	
}
