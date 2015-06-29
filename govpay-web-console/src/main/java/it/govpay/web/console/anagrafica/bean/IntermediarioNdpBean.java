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

import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.web.console.utils.Utils;

public class IntermediarioNdpBean extends BaseBean<IntermediarioModel, Long>  implements IBean<IntermediarioModel, Long>{ 


	private OutputGroup fieldsDatiGenerali = null;
	private Text nomeSoggettoSPC;
	private Text idIntermediarioPA;
	private ConnettoreBean connettore = null;
	public IntermediarioNdpBean(){

		try {
			this.nomeSoggettoSPC = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.nomeSoggettoSPC.setLabel(Utils.getInstance().getMessageFromResourceBundle("intermediariNdp.nomeSoggettoSPC"));
			this.nomeSoggettoSPC.setName("nomeSoggettoSPC");

			this.idIntermediarioPA = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.idIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("intermediariNdp.idIntermediarioPA"));
			this.idIntermediarioPA.setName("idIntermediarioPA");

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId("datiGenerali");
			this.fieldsDatiGenerali.setColumns(2);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");

			this.fieldsDatiGenerali.addField(this.nomeSoggettoSPC);
			this.fieldsDatiGenerali.addField(this.idIntermediarioPA);

			this.connettore = new ConnettoreBean();
			this.connettore.setConnettorePdd(true); 
		} catch (FactoryException e) {
		}
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

	public Text getNomeSoggettoSPC() {
		return nomeSoggettoSPC;
	}


	public void setNomeSoggettoSPC(Text nomeSoggettoSPC) {
		this.nomeSoggettoSPC = nomeSoggettoSPC;
	}


	public Text getIdIntermediarioPA() {
		return idIntermediarioPA;
	}


	public void setIdIntermediarioPA(Text idIntermediarioPA) {
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
