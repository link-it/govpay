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
package it.govpay.web.console.anagrafica.form;

import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.core.model.TributoModel.EnumStatoTributo;
import it.govpay.web.console.anagrafica.bean.TributoBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.Form;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem;
import org.openspcoop2.generic_project.web.input.BooleanCheckBox;
import org.openspcoop2.generic_project.web.input.SelectList;
import org.openspcoop2.generic_project.web.input.Text;

public class TributoForm extends BaseForm implements Form,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private Text idEnteCreditore;
	private Text idTributo; 
	private Text codice;
	private Text ibanAccredito;
	private Text descrizione;
	private BooleanCheckBox stato;
	private SelectList<SelectItem> scadenzario; 


	public TributoForm(){
		try {
			init();
		} catch (FactoryException e) {
		}
	}
	@Override
	public void init() throws FactoryException{
		this.setClosable(false);
		this.setId("formTributo");
		this.setNomeForm(null); 

		this.idEnteCreditore = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idEnteCreditore.setRequired(true);
		this.idEnteCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.idEnteCreditore"));
		this.idEnteCreditore.setName("tr_idEnteCreditore");
		this.idEnteCreditore.setValue(null);

		this.idTributo = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idTributo.setRequired(true);
		this.idTributo.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.idTributo"));
		this.idTributo.setName("tr_idTributo");
		this.idTributo.setValue(null);

		this.codice = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.codice.setRequired(true);
		this.codice.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.codice"));
		this.codice.setName("tr_codice");
		this.codice.setValue(null);
		
		this.ibanAccredito = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
//		this.ibanAccredito.setRequired(true);
		this.ibanAccredito.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.ibanAccredito"));
		this.ibanAccredito.setName("tr_ibanAccredito");
		this.ibanAccredito.setValue(null);

		this.descrizione = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		//		this.descrizione.setRequired(true);
		this.descrizione.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.descrizione"));
		this.descrizione.setName("tr_descrizione");
		this.descrizione.setValue(null);

		this.stato = this.getWebGenericProjectFactory().getInputFieldFactory().createBooleanCheckBox();
		this.stato.setRequired(false);
		this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.attivo"));
		this.stato.setName("tr_statoTributo");
		this.stato.setValue(null);

		this.scadenzario = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.scadenzario.setRequired(true);
		this.scadenzario.setLabel(Utils.getInstance().getMessageFromResourceBundle("tributo.scadenzario"));
		this.scadenzario.setName("tr_scadenzario");
		this.scadenzario.setValue(null);
	}

	@Override
	public void reset() {
		this.idEnteCreditore.reset();
		this.codice.reset();
		this.stato.reset();
		this.idTributo.reset();
		this.scadenzario.reset();
		this.ibanAccredito.reset();
		this.descrizione.reset();
	}

	public Text getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(Text descrizione) {
		this.descrizione = descrizione;
	}
	public SelectList<SelectItem> getScadenzario() {
		return scadenzario;
	}
	public void setScadenzario(SelectList<SelectItem> scadenzario) {
		this.scadenzario = scadenzario;
	}
	public Text getCodice() {
		return codice;
	}

	public void setCodice(Text codice) {
		this.codice = codice;
	}

	public Text getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(Text idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public String valida (){ 
		String _codice = this.codice.getValue();
		if(StringUtils.isEmpty(_codice))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.codice.getLabel());

		SelectItem idSistema = this.scadenzario.getValue();
		if(idSistema!= null){
			String _idSistema = idSistema.getValue();

			if(_idSistema.equals(CostantiForm.NON_SELEZIONATO))
				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.scadenzario.getLabel());
		}

		return null;

	}

	public TributoModel getTributo(){
		TributoModel tributo = new TributoModel();

		tributo.setIdTributo(this.idTributo.getValue());
		tributo.setCodiceTributo(this.codice.getValue());
		tributo.setDescrizione(this.descrizione.getValue());
		tributo.setIdSistema(this.scadenzario.getValue().getValue());
		tributo.setIbanAccredito(this.ibanAccredito.getValue());

		boolean attivo = this.stato.getValue() != null ? this.stato.getValue().booleanValue() : false;

		if(attivo)
			tributo.setStato(EnumStatoTributo.A);
		else 
			tributo.setStato(EnumStatoTributo.D);


		return tributo;
	}

	/**
	 * Inizializza la form con i valori dell'elemento selezionato.
	 * 
	 * @param bean
	 */
	public void setValues(TributoBean bean){
		if(bean != null){
			this.codice.setDefaultValue(bean.getDTO().getCodiceTributo());
			this.codice.setDisabled(true);
			this.idTributo.setDefaultValue(bean.getDTO().getIdTributo());

			if(bean.getDTO().getStato() != null){
				if(bean.getDTO().getStato().equals(EnumStatoTributo.A))
					this.stato.setDefaultValue(true);
				else
					this.stato.setDefaultValue(false);
			}else 
				this.stato.setDefaultValue(false);
			this.descrizione.setDefaultValue(bean.getDTO().getDescrizione());
			this.scadenzario.setDefaultValue(new SelectItem(bean.getDTO().getIdSistema(), bean.getDTO().getIdSistema()));
			this.ibanAccredito.setDefaultValue(bean.getDTO().getIbanAccredito());
		} else {
			this.codice.setDefaultValue(null);
			this.idTributo.setDefaultValue(null);
			this.stato.setDefaultValue(true);
			this.descrizione.setDefaultValue(null);
			this.scadenzario.setDefaultValue(null);
			this.ibanAccredito.setDefaultValue(null);
			this.codice.setDisabled(false);
		}

		this.reset();
	}

	public BooleanCheckBox getStato() {
		return stato;
	}

	public void setStato(BooleanCheckBox stato) {
		this.stato = stato;
	}
	public Text getIdTributo() {
		return idTributo;
	}
	public void setIdTributo(Text idTributo) {
		this.idTributo = idTributo;
	}
	public Text getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(Text ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	
	
}
