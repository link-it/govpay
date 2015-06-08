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

import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.core.model.TributoModel.EnumStatoTributo;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class TributoBean extends BaseBean<TributoModel, Long>{

	/**
	 * Identificativo della categoria del tributo
	 * (e.g. PrestazioniSanitarie)
	 */
	private OutputField<String> idTributo; 

	/**
	 * Chiave fisica dell'ente creditore che gestisce il tributo
	 */
	private OutputField<String> idEnteCreditore;

	/**
	 * Codice del sistema informativo locale dell'ente creditore, applicazione o sottosistema che gestisce il tributo.
	 */
	private OutputField<String> idSistema; 

	/**
	 * Codice specifico del tipo debito noto all'ente creditore:
	  e.g. TICKET_SANITARIO_CUP
	 */
	private OutputField<String> codiceTributo; 

	private OutputField<String> descrizione;

	private OutputField<String> stato;
	
	private OutputField<String> ibanAccredito;

	private OutputGroup fieldsDatiGenerali = null;
	private Long id = null;

	public TributoBean (){
		init();
	}


	private void init(){
		this.idEnteCreditore = new OutputText();
		this.idEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("tributo.idEnteCreditore"));
		this.idEnteCreditore.setName("tr_idEnteCreditore");

		this.idTributo = new OutputText();
		this.idTributo.setLabel(Utils.getMessageFromResourceBundle("tributo.idTributo"));
		this.idTributo.setName("tr_idTributo");

		this.codiceTributo = new OutputText();
		this.codiceTributo.setLabel(Utils.getMessageFromResourceBundle("tributo.codice"));
		this.codiceTributo.setName("tr_codiceTributo");

		this.descrizione = new OutputText();
		this.descrizione.setLabel(Utils.getMessageFromResourceBundle("tributo.descrizione"));
		this.descrizione.setName("tr_descrizione");

		this.idSistema = new OutputText();
		this.idSistema.setLabel(Utils.getMessageFromResourceBundle("tributo.idSistema"));
		this.idSistema.setName("tr_idSistema");
		
		this.ibanAccredito = new OutputText();
		this.ibanAccredito.setLabel(Utils.getMessageFromResourceBundle("tributo.ibanAccredito"));
		this.ibanAccredito.setName("tr_ibanAccredito");

		this.stato = new OutputText();
		this.stato.setLabel(Utils.getMessageFromResourceBundle("tributo.stato"));
		this.stato.setName("tr_stato");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup("datiGeneraliTributo");
		this.fieldsDatiGenerali.setColumns(4);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx,labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.stato);
		this.fieldsDatiGenerali.addField(this.descrizione);
		
		this.fieldsDatiGenerali.addField(this.codiceTributo);
		this.fieldsDatiGenerali.addField(this.ibanAccredito);
		
	}

	@Override
	public void setDTO(TributoModel dto) {
		super.setDTO(dto);

		this.idEnteCreditore.setValue(this.getDTO().getIdEnteCreditore());
		this.idTributo.setValue(this.getDTO().getIdTributo());
		this.codiceTributo.setValue(this.getDTO().getCodiceTributo());
		this.descrizione.setValue(this.getDTO().getDescrizione()); 
		this.idSistema.setValue(this.getDTO().getIdSistema());
		this.ibanAccredito.setValue(this.getDTO().getIbanAccredito());
		
		String stato = this.getDTO().getStato().equals(EnumStatoTributo.A) ? Utils.getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getMessageFromResourceBundle("commons.label.nonAttivo");
		this.stato.setValue(stato);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public OutputField<String> getIdTributo() {
		return idTributo;
	}


	public void setIdTributo(OutputField<String> idTributo) {
		this.idTributo = idTributo;
	}


	public OutputField<String> getIdEnteCreditore() {
		return idEnteCreditore;
	}


	public void setIdEnteCreditore(OutputField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}


	public OutputField<String> getIdSistema() {
		return idSistema;
	}


	public void setIdSistema(OutputField<String> idSistema) {
		this.idSistema = idSistema;
	}


	public OutputField<String> getCodiceTributo() {
		return codiceTributo;
	}


	public void setCodiceTributo(OutputField<String> codiceTributo) {
		this.codiceTributo = codiceTributo;
	}


	public OutputField<String> getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(OutputField<String> descrizione) {
		this.descrizione = descrizione;
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


	public OutputField<String> getIbanAccredito() {
		return ibanAccredito;
	}


	public void setIbanAccredito(OutputField<String> ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	
	
}
