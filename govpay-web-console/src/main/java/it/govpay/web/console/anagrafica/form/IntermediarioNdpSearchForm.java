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

import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.openspcoop2.generic_project.web.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.SelectItem;
import org.openspcoop2.generic_project.web.form.field.TextField;


@Named("intNdpSearchForm") @SessionScoped
public class IntermediarioNdpSearchForm extends BaseSearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FormField<String> idIntermediario = null;

	private FormField<String> idEnteCreditore = null;

	public IntermediarioNdpSearchForm(){
		init();
	}
	
	@Override
	protected void init() {

		// Properties del form
		this.setIdForm("formIntermediariNdp");
		this.setNomeForm("Ricerca Intermediari Nodo dei pagamenti");
		this.setClosable(false);

		// Init dei FormField
		this.idIntermediario = new TextField();
		this.idIntermediario.setType("textWithSuggestion");
		this.idIntermediario.setName("idIntermediario");
		this.idIntermediario.setDefaultValue(null);
		this.idIntermediario.setLabel(Utils.getMessageFromResourceBundle("intermediariNdp.search.idIntermediario"));
		this.idIntermediario.setAutoComplete(true);
		this.idIntermediario.setEnableManualInput(true);
		this.idIntermediario.setFieldsToUpdate(this.getIdForm() + "_formPnl");
		this.idIntermediario.setForm(this);

		this.idEnteCreditore = new TextField();
		this.idEnteCreditore.setName("idEnteCreditore");
		this.idEnteCreditore.setDefaultValue(null);

		resetParametriPaginazione();
	}


	@Override
	public void reset() {
		resetParametriPaginazione();
	}

	public FormField<String> getIdIntermediario() {
		return idIntermediario;
	}


	public void setIdIntermediario(FormField<String> idIntermediario) {
		this.idIntermediario = idIntermediario;
	}


	public List<SelectItem> idIntermediarioAutoComplete(Object val){
		return new ArrayList<SelectItem>();
	}

	public void idIntermediarioSelectListener(ActionEvent ae){
	}


	public FormField<String> getIdEnteCreditore() {
		return idEnteCreditore;
	}


	public void setIdEnteCreditore(FormField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

//
//	public List<String> getIdentificativiEnteCreditore() {
//		return identificativiEnteCreditore;
//	}
//
//
//	public void setIdentificativiEnteCreditore(
//			List<String> identificativiEnteCreditore) {
//		this.identificativiEnteCreditore = identificativiEnteCreditore;
//	}

}
