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

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.web.console.utils.Utils;

public class StazioneBean extends BaseBean<StazioneModel, Long> implements IBean<StazioneModel, Long> {  


	private Text password;
	private OutputGroup fieldsDatiGenerali = null;
	private Text idIntermediarioPA;
	private Text idStazioneIntermediarioPA;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public StazioneBean() {
		try {
			this.password = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();

			this.password.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.password"));
			this.password.setName("staz_password");

			this.idIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.idIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.idIntermediarioPA"));
			this.idIntermediarioPA.setName("staz_idIntermediarioPA");

			this.idStazioneIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.idStazioneIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.idStazioneIntermediarioPA"));
			this.idStazioneIntermediarioPA.setName("staz_idStazioneIntermediarioPA");

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId ("staz_datiGenerali");
			this.fieldsDatiGenerali.setColumns(2);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");


			this.fieldsDatiGenerali.addField(this.idIntermediarioPA);
			this.fieldsDatiGenerali.addField(this.idStazioneIntermediarioPA);
			this.fieldsDatiGenerali.addField(this.password);
		} catch (FactoryException e) {
		}
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

	public Text getIdIntermediarioPA() {
		return idIntermediarioPA;
	}


	public void setIdIntermediarioPA(Text idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public Text getIdStazioneIntermediarioPA() {
		return idStazioneIntermediarioPA;
	}

	public void setIdStazioneIntermediarioPA(
			Text idStazioneIntermediarioPA) {
		this.idStazioneIntermediarioPA = idStazioneIntermediarioPA;
	}

	public Text getPassword() {
		return password;
	}


	public void setPassword(Text password) {
		this.password = password;
	}

}
