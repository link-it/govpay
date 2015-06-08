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

import it.govpay.web.console.anagrafica.bean.ScadenzarioBean;
import it.govpay.web.console.anagrafica.mbean.ScadenzarioMBean;
import it.govpay.web.console.anagrafica.model.ScadenzarioModel;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseForm;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.SelectItem;
import org.openspcoop2.generic_project.web.form.field.SelectListField;
import org.openspcoop2.generic_project.web.form.field.TextField;

@Named("scadenzarioForm")
public class ScadenzarioForm extends BaseForm implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormField<String> getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(FormField<String> idEnte) {
		this.idEnte = idEnte;
	}

	public ConnettoreForm getConnettoreVerifica() {
		return connettoreVerifica;
	}

	public void setConnettoreVerifica(ConnettoreForm connettoreVerifica) {
		this.connettoreVerifica = connettoreVerifica;
	}

	private FormField<String> nome;
	private FormField<String> idEnte;
	private SelectListField idIntermediarioPA;
	private SelectListField stazione;

	private ConnettoreForm connettoreNotifica;
	private ConnettoreForm connettoreVerifica;

	private ScadenzarioMBean mbean = null;

	public ScadenzarioForm(){
		init();
	}

	@Override
	protected void init() {

		this.setClosable(false);
		this.setIdForm("formScadenzario");
		this.setNomeForm(null); 

		this.nome = new TextField();
		this.nome.setRequired(true);
		this.nome.setLabel(Utils.getMessageFromResourceBundle("scadenzario.nome"));
		this.nome.setName("scad_nome");
		this.nome.setValue(null);

		this.idEnte = new TextField();
		this.idEnte.setName("scad_idEnte");
		this.idEnte.setValue(null);
		this.idEnte.setRequired(true);
		this.idEnte.setRendered(false);
		this.idEnte.setLabel("scadenzario.idEnte");

		this.idIntermediarioPA = new SelectListField();
		this.idIntermediarioPA.setRequired(true);
		this.idIntermediarioPA.setLabel(Utils.getMessageFromResourceBundle("scadenzario.idIntermediarioPA"));
		this.idIntermediarioPA.setName("scad_idIntermediarioPA");
		this.idIntermediarioPA.setDisabled(true);
		this.idIntermediarioPA.setFieldsToUpdate(this.getIdForm() + "_formPnl");
		this.idIntermediarioPA.setForm(this);

		this.stazione = new SelectListField();
		this.stazione.setRequired(true);
		this.stazione.setLabel(Utils.getMessageFromResourceBundle("scadenzario.stazione"));
		this.stazione.setName("scad_stazione");
		this.stazione.setValue(null);

		this.connettoreNotifica = new ConnettoreForm("connNot");
		this.connettoreNotifica.setConnettorePdd(false); 
		this.connettoreNotifica.setForm(this);

		this.connettoreVerifica = new ConnettoreForm("connVer");
		this.connettoreVerifica.setConnettorePdd(false); 
		this.connettoreVerifica.setForm(this); 
	}

	@Override
	public void reset() {
		this.nome.reset();
		this.idIntermediarioPA.reset();
		this.stazione.reset();
		this.connettoreNotifica.reset();
		this.connettoreVerifica.reset();
		this.idEnte.reset();

		_setIdentificativoPA();
	}

	/**
	 * Inizializza la form con i valori dell'elemento selezionato.
	 * 
	 * @param bean
	 */
	public void setValues(ScadenzarioBean bean){
		if(bean != null){
			this.nome.setDefaultValue(bean.getDTO().getIdSystem());
			this.nome.setDisabled(true);
			this.idEnte.setDefaultValue(bean.getIdEnte().getValue());
			SelectItem defValue = new SelectItem(bean.getIdIntermediarioPA().getValue(), bean.getIdIntermediarioPA().getValue());
			this.idIntermediarioPA.setDefaultValue(defValue);
			SelectItem stazDefValue = new SelectItem(bean.getIdStazioneIntermediarioPA().getValue(), bean.getIdStazioneIntermediarioPA().getValue());
			this.stazione.setDefaultValue(stazDefValue);
			this.connettoreNotifica.setValues(bean.getConnettoreNotifica());
			this.connettoreVerifica.setValues(bean.getConnettoreVerifica());
		} else {
			this.nome.setDisabled(false);
			this.nome.setDefaultValue(null);
			this.idEnte.setDefaultValue(null);
			this.idIntermediarioPA.setDefaultValue(null);
			this.stazione.setDefaultValue(null);
			//			this.idIntermediarioPA.setDefaultValue(new SelectItem(CostantiForm.NON_SELEZIONATO,CostantiForm.NON_SELEZIONATO));
			//			this.stazione.setDefaultValue(new SelectItem(CostantiForm.NON_SELEZIONATO,CostantiForm.NON_SELEZIONATO));
			this.connettoreNotifica.setValues(null);
			this.connettoreVerifica.setValues(null);
		}

		this.reset();
	}

	private void _setIdentificativoPA(){

	}


	public FormField<String> getNome() {
		return nome;
	}

	public void setNome(FormField<String> nome) {
		this.nome = nome;
	}


	public SelectListField getIdIntermediarioPA() {
		return idIntermediarioPA;
	}

	public void setIdIntermediarioPA(SelectListField idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public SelectListField getStazione() {
		SelectItem value = this.idIntermediarioPA.getValue();

		boolean rendered = false;
		if(value != null){
			String value2 = value.getValue();
			if(StringUtils.isNotEmpty(value2) && !value.equals(CostantiForm.NON_SELEZIONATO))
				rendered = true;
		}

		this.stazione.setRendered(rendered);
		return stazione;
	}

	public void setStazione(SelectListField stazione) {
		this.stazione = stazione;
	}

	public ConnettoreForm getConnettoreNotifica() {
		return connettoreNotifica;
	}

	public void setConnettoreNotifica(ConnettoreForm connettoreNotifica) {
		this.connettoreNotifica = connettoreNotifica;
	}


	public ScadenzarioModel getScadenzario(){
		it.govpay.ejb.core.model.ScadenzarioModel scadenzarioModel = new it.govpay.ejb.core.model.ScadenzarioModel();

		String value = this.idEnte.getValue();
		scadenzarioModel.setIdEnte(value); 
		scadenzarioModel.setIdSystem(this.nome.getValue());

		scadenzarioModel.setConnettoreNotifica(this.connettoreNotifica.getConnettore());
		scadenzarioModel.setConnettoreVerifica(this.connettoreVerifica.getConnettore());

		SelectItem staz = this.stazione.getValue();
		String idStazione = null;

		if(staz!= null){
			idStazione = staz.getValue();
		}
		scadenzarioModel.setIdStazione(idStazione);

		return new ScadenzarioModel(scadenzarioModel);
	}

	public String valida(){
		String _nomeSoggettoSPC = this.nome.getValue();
		if(StringUtils.isEmpty(_nomeSoggettoSPC))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.nome.getLabel());


		SelectItem idIntermediario = this.idIntermediarioPA.getValue();
		String idInt = null;

		if(idIntermediario!= null){
			idInt = idIntermediario.getValue();
			if(StringUtils.isEmpty(idInt) || idInt.equals(CostantiForm.NON_SELEZIONATO))
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.idIntermediarioPA.getLabel());
		}

		SelectItem staz = this.stazione.getValue();
		String st = null;

		if(staz!= null){
			st = staz.getValue();
		}	  
		if(StringUtils.isEmpty(st) || st.equals(CostantiForm.NON_SELEZIONATO))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.stazione.getLabel());

		String msg = this.connettoreNotifica.valida();
		if(msg!= null)
			return msg;

		return this.connettoreVerifica.valida();
	}

	public ScadenzarioMBean getMbean() {
		return mbean;
	}

	public void setMbean(ScadenzarioMBean mbean) {
		this.mbean = mbean;
	}


	public void scad_idIntermediarioPASelectListener(ActionEvent ae){
		SelectItem value = this.idIntermediarioPA.getValue();
		String value2 = null;
		if(value!= null){
			value2 = value.getValue();
		}

		if((value2 == null || (value2!=null && !value2.equals(CostantiForm.NON_SELEZIONATO)))){
			this.stazione.setElencoSelectItems(this.mbean.getListaStazioni(value2));
			this.stazione.setValue(new SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)); 
		}
	}



}
