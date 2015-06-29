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
import it.govpay.web.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.DateTime;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class PspBean extends BaseBean<PspModel, Long> implements IBean<PspModel, Long>{ 

	private Text ragioneSociale= null;
	private Text informazioni= null;
	private DateTime inizioValidita= null;
	private DateTime fineValidita= null;

	private OutputGroup fieldsDatiGenerali = null;

	private List<CanaleBean> listaCanali = null;

	public PspBean(){

		try {
			this.ragioneSociale = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.ragioneSociale.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.ragioneSociale"));
			this.ragioneSociale.setName("ragioneSociale");

			this.informazioni = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.informazioni.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.informazioni"));
			this.informazioni.setName("informazioni");

			this.inizioValidita = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
			this.inizioValidita.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.inizioValidita"));
			this.inizioValidita.setName("inizioValidita");

			this.fineValidita = this.getWebGenericProjectFactory().getOutputFieldFactory().createDateTime();
			this.fineValidita.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.fineValidita"));
			this.fineValidita.setName("fineValidita");

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId("datiGenerali");
			this.fieldsDatiGenerali.setColumns(4);
			this.fieldsDatiGenerali.setRendered(true);
			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx,labelAllineataDx,valueAllineataSx");

			this.fieldsDatiGenerali.addField(this.ragioneSociale);
			this.fieldsDatiGenerali.addField(this.inizioValidita);

			this.fieldsDatiGenerali.addField(this.informazioni);
			this.fieldsDatiGenerali.addField(this.fineValidita);

			this.listaCanali = new ArrayList<CanaleBean>();
		} catch (FactoryException e) {
		}
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

	@Override
	public Long getId() {
		return null;
	}
	
	public Text getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(Text ragioneSociale) {
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

	public Text getInformazioni() {
		return informazioni;
	}

	public void setInformazioni(Text informazioni) {
		this.informazioni = informazioni;
	}

	public DateTime getInizioValidita() {
		return inizioValidita;
	}

	public void setInizioValidita(DateTime inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public DateTime getFineValidita() {
		return fineValidita;
	}

	public void setFineValidita(DateTime fineValidita) {
		this.fineValidita = fineValidita;
	}





}
