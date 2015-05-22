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

import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.model.TributoModel.EnumStatoTributo;
import it.govpay.web.console.anagrafica.bean.TributoBean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseForm;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.field.BooleanField;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.SelectItem;
import org.openspcoop2.generic_project.web.form.field.SelectListField;
import org.openspcoop2.generic_project.web.form.field.TextField;

public class TributoForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private FormField<String> idEnteCreditore;
	private FormField<String> idTributo; 
	private FormField<String> codice;
	private FormField<String> ibanAccredito;
	private FormField<String> descrizione;
	private BooleanField stato;
	private FormField<SelectItem> scadenzario;


	public TributoForm(){
		init();
	}
	@Override
	protected void init() {
		this.setClosable(false);
		this.setIdForm("formTributo");
		this.setNomeForm(null); 

		this.idEnteCreditore = new TextField();
		this.idEnteCreditore.setRequired(true);
		this.idEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("tributo.idEnteCreditore"));
		this.idEnteCreditore.setName("tr_idEnteCreditore");
		this.idEnteCreditore.setValue(null);

		this.idTributo = new TextField();
		this.idTributo.setRequired(true);
		this.idTributo.setLabel(Utils.getMessageFromResourceBundle("tributo.idTributo"));
		this.idTributo.setName("tr_idTributo");
		this.idTributo.setValue(null);

		this.codice = new TextField();
		this.codice.setRequired(true);
		this.codice.setLabel(Utils.getMessageFromResourceBundle("tributo.codice"));
		this.codice.setName("tr_codice");
		this.codice.setValue(null);
		
		this.ibanAccredito = new TextField();
//		this.ibanAccredito.setRequired(true);
		this.ibanAccredito.setLabel(Utils.getMessageFromResourceBundle("tributo.ibanAccredito"));
		this.ibanAccredito.setName("tr_ibanAccredito");
		this.ibanAccredito.setValue(null);

		this.descrizione = new TextField();
		//		this.descrizione.setRequired(true);
		this.descrizione.setLabel(Utils.getMessageFromResourceBundle("tributo.descrizione"));
		this.descrizione.setName("tr_descrizione");
		this.descrizione.setValue(null);

		this.stato = new BooleanField();
		this.stato.setRequired(false);
		this.stato.setLabel(Utils.getMessageFromResourceBundle("tributo.attivo"));
		this.stato.setName("tr_statoTributo");
		this.stato.setValue(null);

		this.scadenzario = new SelectListField(); 
		this.scadenzario.setRequired(true);
		this.scadenzario.setLabel(Utils.getMessageFromResourceBundle("tributo.scadenzario"));
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

	public FormField<String> getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(FormField<String> descrizione) {
		this.descrizione = descrizione;
	}
	public FormField<SelectItem> getScadenzario() {
		return scadenzario;
	}
	public void setScadenzario(FormField<SelectItem> scadenzario) {
		this.scadenzario = scadenzario;
	}
	public FormField<String> getCodice() {
		return codice;
	}

	public void setCodice(FormField<String> codice) {
		this.codice = codice;
	}

	public FormField<String> getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(FormField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public String valida (){ 
		String _codice = this.codice.getValue();
		if(StringUtils.isEmpty(_codice))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.codice.getLabel());

		SelectItem idSistema = this.scadenzario.getValue();
		if(idSistema!= null){
			String _idSistema = idSistema.getValue();

			if(_idSistema.equals(CostantiForm.NON_SELEZIONATO))
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.scadenzario.getLabel());
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

	public BooleanField getStato() {
		return stato;
	}

	public void setStato(BooleanField stato) {
		this.stato = stato;
	}
	public FormField<String> getIdTributo() {
		return idTributo;
	}
	public void setIdTributo(FormField<String> idTributo) {
		this.idTributo = idTributo;
	}
	public FormField<String> getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(FormField<String> ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	
	
}
