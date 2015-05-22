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

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class CanaleBean extends BaseBean<CanaleModel, String>{

	private OutputField<String> descrizione= null;
	private OutputField<String> tipoVersamento= null;
	private OutputField<String> disponibilita= null;
	private OutputField<String> modelloVersamento= null;
	private OutputField<String> infoUrl= null;
	private OutputField<String> stornoGestito= null;
	private OutputField<String> idPsp= null;
	private OutputField<String> stato = null;

	private OutputGroup fieldsDatiGenerali = null;
	
	public CanaleBean(){
		
		this.descrizione = new OutputText();
		this.descrizione.setLabel(Utils.getMessageFromResourceBundle("psp.descrizione"));
		this.descrizione.setName("can_descrizione");
		
		this.tipoVersamento = new OutputText();
		this.tipoVersamento.setLabel(Utils.getMessageFromResourceBundle("psp.tipoVersamento"));
		this.tipoVersamento.setName("can_tipoVersamento");
		
		this.disponibilita = new OutputText();
		this.disponibilita.setLabel(Utils.getMessageFromResourceBundle("psp.disponibilita"));
		this.disponibilita.setName("can_disponibilita");
		
		this.modelloVersamento = new OutputText();
		this.modelloVersamento.setLabel(Utils.getMessageFromResourceBundle("psp.modelloVersamento"));
		this.modelloVersamento.setName("can_modelloVersamento");
		
		this.stornoGestito = new OutputText();
		this.stornoGestito.setLabel(Utils.getMessageFromResourceBundle("psp.stornoGestito"));
		this.stornoGestito.setName("can_stornoGestito");
		
		this.infoUrl = new OutputText();
		this.infoUrl.setLabel(Utils.getMessageFromResourceBundle("psp.infoUrl"));
		this.infoUrl.setName("can_infoUrl");
		
		this.idPsp = new OutputText();
		this.idPsp.setLabel(Utils.getMessageFromResourceBundle("psp.idPsp"));
		this.idPsp.setName("can_idPsp");
		
		this.stato = new OutputText();
		this.stato.setLabel(Utils.getMessageFromResourceBundle("psp.stato"));
		this.stato.setName("can_stato");
		
		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("can_datiGenerali");
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
	}

	@Override
	public void setDTO(CanaleModel dto) {
		super.setDTO(dto);

		this.descrizione.setValue(this.getDTO().getDescrizione());
		String tV = this.getDTO().getTipoVersamento().name();
		this.tipoVersamento.setValue(Utils.getMessageFromResourceBundle("psp.tipoVersamento." + tV));
		this.disponibilita.setValue(this.getDTO().getDisponibilita());
		String mV= this.getDTO().getModelloVersamento().name();
		this.modelloVersamento.setValue(Utils.getMessageFromResourceBundle("psp.modelloVersamento." + mV));
		this.infoUrl.setValue("");
		String storno = this.getDTO().isStornoGestito() ? Utils.getMessageFromResourceBundle("commons.label.SI") :
			Utils.getMessageFromResourceBundle("commons.label.NO");
		this.stornoGestito.setValue(storno);
		this.idPsp.setValue(this.getDTO().getIdPsp() + "");
		
		String stato = this.getDTO().isAbilitato() ? Utils.getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getMessageFromResourceBundle("commons.label.nonAttivo");
		
		this.stato.setValue(stato);
	}
	
	public OutputField<String> getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(OutputField<String> descrizione) {
		this.descrizione = descrizione;
	}

	public OutputField<String> getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(OutputField<String> tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public OutputField<String> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(OutputField<String> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public OutputField<String> getModelloVersamento() {
		return modelloVersamento;
	}

	public void setModelloVersamento(OutputField<String> modelloVersamento) {
		this.modelloVersamento = modelloVersamento;
	}

	public OutputField<String> getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(OutputField<String> infoUrl) {
		this.infoUrl = infoUrl;
	}

	public OutputField<String> getStornoGestito() {
		return stornoGestito;
	}

	public void setStornoGestito(OutputField<String> stornoGestito) {
		this.stornoGestito = stornoGestito;
	}

	public OutputField<String> getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(OutputField<String> idPsp) {
		this.idPsp = idPsp;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputField<String> getStato() {
		return stato;
	}

	public void setStato(OutputField<String> stato) {
		this.stato = stato;
	}
	
	
}
