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

import it.govpay.ndp.model.StazioneModel;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class StazioneBean extends BaseBean<StazioneModel, String> { 


	private OutputField<String> password;
	private OutputGroup fieldsDatiGenerali = null;
	private OutputField<String> idIntermediarioPA;
	private OutputField<String> idStazioneIntermediarioPA;
	private Long id = null;

	public StazioneBean() {
		this.password = new OutputText();
		this.password.setLabel(Utils.getMessageFromResourceBundle("stazione.password"));
		this.password.setName("staz_password");

		this.idIntermediarioPA = new OutputText();
		this.idIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("stazione.idIntermediarioPA"));
		this.idIntermediarioPA.setName("staz_idIntermediarioPA");
		
		this.idStazioneIntermediarioPA = new OutputText();
		this.idStazioneIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("stazione.idStazioneIntermediarioPA"));
		this.idStazioneIntermediarioPA.setName("staz_idStazioneIntermediarioPA");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("staz_datiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");


		this.fieldsDatiGenerali.addField(this.idIntermediarioPA);
		this.fieldsDatiGenerali.addField(this.idStazioneIntermediarioPA);
		this.fieldsDatiGenerali.addField(this.password);
	}

	@Override
	public void setDTO(StazioneModel dto) {
		super.setDTO(dto);
		
		this.idStazioneIntermediarioPA.setValue(this.getDTO().getIdStazioneIntermediarioPA()); 
		this.idIntermediarioPA.setValue(this.getDTO().getIdIntermediarioPA());
		this.password.setValue(this.getDTO().getPassword());
	}
	
	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}


	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getIdIntermediarioPA() {
		return idIntermediarioPA;
	}


	public void setIdIntermediarioPA(OutputField<String> idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public OutputField<String> getIdStazioneIntermediarioPA() {
		return idStazioneIntermediarioPA;
	}

	public void setIdStazioneIntermediarioPA(
			OutputField<String> idStazioneIntermediarioPA) {
		this.idStazioneIntermediarioPA = idStazioneIntermediarioPA;
	}

	public OutputField<String> getPassword() {
		return password;
	}


	public void setPassword(OutputField<String> password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
}
