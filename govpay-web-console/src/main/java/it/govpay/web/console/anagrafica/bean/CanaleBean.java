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

import it.govpay.web.console.anagrafica.model.PspModel.CanaleModel;
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class CanaleBean extends BaseBean<CanaleModel, String> implements IBean<CanaleModel, String>{ 

	private Text descrizione= null;
	private Text tipoVersamento= null;
	private Text disponibilita= null;
	private Text modelloVersamento= null;
	private Text infoUrl= null;
	private Text stornoGestito= null;
	private Text idPsp= null;
	private Text stato = null;

	private OutputGroup fieldsDatiGenerali = null;

	public CanaleBean(){

		try{
			this.descrizione = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.descrizione.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.descrizione"));
			this.descrizione.setName("can_descrizione");

			this.tipoVersamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.tipoVersamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.tipoVersamento"));
			this.tipoVersamento.setName("can_tipoVersamento");

			this.disponibilita = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.disponibilita.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.disponibilita"));
			this.disponibilita.setName("can_disponibilita");

			this.modelloVersamento = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.modelloVersamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.modelloVersamento"));
			this.modelloVersamento.setName("can_modelloVersamento");

			this.stornoGestito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.stornoGestito.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.stornoGestito"));
			this.stornoGestito.setName("can_stornoGestito");

			this.infoUrl = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.infoUrl.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.infoUrl"));
			this.infoUrl.setName("can_infoUrl");

			this.idPsp = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.idPsp.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.idPsp"));
			this.idPsp.setName("can_idPsp");

			this.stato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
			this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("psp.stato"));
			this.stato.setName("can_stato");

			this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
			this.fieldsDatiGenerali.setId("can_datiGenerali");
			this.fieldsDatiGenerali.setColumns(2);
			this.fieldsDatiGenerali.setRendered(true);

			this.fieldsDatiGenerali.addField(this.stato);
			this.fieldsDatiGenerali.addField(this.disponibilita);
			this.fieldsDatiGenerali.addField(this.tipoVersamento);
			this.fieldsDatiGenerali.addField(this.modelloVersamento);
			this.fieldsDatiGenerali.addField(this.infoUrl);
			this.fieldsDatiGenerali.addField(this.stornoGestito);
			this.fieldsDatiGenerali.addField(this.descrizione);


			this.fieldsDatiGenerali.setStyleClass("beanTable"); 
			this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");
		}catch(Exception e){

		}
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setDTO(CanaleModel dto) {
		super.setDTO(dto);

		// [TODO] Check
		this.id = this.getDTO().getIdPsp() + "";

		this.descrizione.setValue(this.getDTO().getDescrizione());
		String tV = this.getDTO().getTipoVersamento().name();
		this.tipoVersamento.setValue(Utils.getInstance().getMessageFromResourceBundle("psp.tipoVersamento." + tV));
		this.disponibilita.setValue(this.getDTO().getDisponibilita());
		String mV= this.getDTO().getModelloVersamento().name();
		this.modelloVersamento.setValue(Utils.getInstance().getMessageFromResourceBundle("psp.modelloVersamento." + mV));
		this.infoUrl.setValue("");
		String storno = this.getDTO().isStornoGestito() ? Utils.getInstance().getMessageFromResourceBundle("commons.label.SI") :
			Utils.getInstance().getMessageFromResourceBundle("commons.label.NO");
		this.stornoGestito.setValue(storno);
		this.idPsp.setValue(this.getDTO().getIdPsp() + "");

		String stato = this.getDTO().isAbilitato() ? Utils.getInstance().getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getInstance().getMessageFromResourceBundle("commons.label.nonAttivo");

		this.stato.setValue(stato);
	}

	public Text getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(Text descrizione) {
		this.descrizione = descrizione;
	}

	public Text getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(Text tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public Text getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(Text disponibilita) {
		this.disponibilita = disponibilita;
	}

	public Text getModelloVersamento() {
		return modelloVersamento;
	}

	public void setModelloVersamento(Text modelloVersamento) {
		this.modelloVersamento = modelloVersamento;
	}

	public Text getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(Text infoUrl) {
		this.infoUrl = infoUrl;
	}

	public Text getStornoGestito() {
		return stornoGestito;
	}

	public void setStornoGestito(Text stornoGestito) {
		this.stornoGestito = stornoGestito;
	}

	public Text getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(Text idPsp) {
		this.idPsp = idPsp;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public Text getStato() {
		return stato;
	}

	public void setStato(Text stato) {
		this.stato = stato;
	}


}
