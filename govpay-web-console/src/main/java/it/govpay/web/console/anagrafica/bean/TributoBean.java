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

import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.core.model.TributoModel.EnumStatoTributo;
import it.govpay.web.console.utils.Utils;

public class TributoBean extends BaseBean<TributoModel, Long> implements IBean<TributoModel, Long>{ 

	/**
	 * Identificativo della categoria del tributo
	 * (e.g. PrestazioniSanitarie)
	 */
	private Text idTributo; 

	/**
	 * Chiave fisica dell'ente creditore che gestisce il tributo
	 */
	private Text idEnteCreditore;

	/**
	 * Codice del sistema informativo locale dell'ente creditore, applicazione o sottosistema che gestisce il tributo.
	 */
	private Text idSistema; 

	/**
	 * Codice specifico del tipo debito noto all'ente creditore:
	  e.g. TICKET_SANITARIO_CUP
	 */
	private Text codiceTributo; 

	private Text descrizione;

	private Text stato;
	
	private Text ibanAccredito;

	private OutputGroup fieldsDatiGenerali = null;
	private Long id = null;

	public TributoBean (){
		try {
			init();
		} catch (FactoryException e) {
		}
	}


	private void init() throws FactoryException{
		this.idEnteCreditore = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idEnteCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.idEnteCreditore"));
		this.idEnteCreditore.setName("tr_idEnteCreditore");

		this.idTributo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idTributo.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.idTributo"));
		this.idTributo.setName("tr_idTributo");

		this.codiceTributo = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.codiceTributo.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.codice"));
		this.codiceTributo.setName("tr_codiceTributo");

		this.descrizione = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.descrizione.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.descrizione"));
		this.descrizione.setName("tr_descrizione");

		this.idSistema = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.idSistema.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.idSistema"));
		this.idSistema.setName("tr_idSistema");
		
		this.ibanAccredito = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.ibanAccredito.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.ibanAccredito"));
		this.ibanAccredito.setName("tr_ibanAccredito");

		this.stato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.stato"));
		this.stato.setName("tr_stato");

		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId("datiGeneraliTributo");
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
		
		String stato = this.getDTO().getStato().equals(EnumStatoTributo.A) ? Utils.getInstance().getMessageFromResourceBundle("commons.label.attivo") : 
			Utils.getInstance().getMessageFromResourceBundle("commons.label.nonAttivo");
		this.stato.setValue(stato);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Text getIdTributo() {
		return idTributo;
	}


	public void setIdTributo(Text idTributo) {
		this.idTributo = idTributo;
	}


	public Text getIdEnteCreditore() {
		return idEnteCreditore;
	}


	public void setIdEnteCreditore(Text idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}


	public Text getIdSistema() {
		return idSistema;
	}


	public void setIdSistema(Text idSistema) {
		this.idSistema = idSistema;
	}


	public Text getCodiceTributo() {
		return codiceTributo;
	}


	public void setCodiceTributo(Text codiceTributo) {
		this.codiceTributo = codiceTributo;
	}


	public Text getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(Text descrizione) {
		this.descrizione = descrizione;
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


	public Text getIbanAccredito() {
		return ibanAccredito;
	}


	public void setIbanAccredito(Text ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	
	
}
