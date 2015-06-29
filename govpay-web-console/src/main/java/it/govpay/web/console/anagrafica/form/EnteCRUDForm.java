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

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.EnteCreditoreModel.EnumStato;
import it.govpay.web.console.anagrafica.bean.EnteBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.Form;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseForm;
import org.openspcoop2.generic_project.web.input.BooleanCheckBox;
import org.openspcoop2.generic_project.web.input.Text;

@Named("enteForm")
public class EnteCRUDForm extends BaseForm implements Form,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private Text idEnteCreditore; 
	private Text identificativoUnivoco;
	private Text idFiscale; 
	private Text denominazione; 
	private Text indirizzo;
	private Text civico;
	private Text localita;
	private Text cap;
	private Text provincia;
	private Text idDominio;
	private BooleanCheckBox abilitaNodoPagamento;
	private BooleanCheckBox stato;
	
	public static final String PROVINCIA_PATTERN = "[a-zA-Z]{2}";
	public static final String CAP_PATTERN = "[0-9]{5}";
	
	private Pattern provinciaPattern;
	private Pattern capPattern;


	public EnteCRUDForm(){
		try {
			init();
		} catch (FactoryException e) {
		}
	}

	/**
	 * Inizializza la form con i valori dell'elemento selezionato.
	 * 
	 * @param bean
	 */
	public void setValues(EnteBean bean){
		if(bean != null){
			this.identificativoUnivoco.setDefaultValue(bean.getDTO().getIdentificativoUnivoco());
			this.idEnteCreditore.setDefaultValue(bean.getDTO().getIdEnteCreditore());
			this.idFiscale.setDefaultValue(bean.getDTO().getIdFiscale());
			this.denominazione.setDefaultValue(bean.getDTO().getDenominazione());
			this.indirizzo.setDefaultValue(bean.getDTO().getIndirizzo());
			this.civico.setDefaultValue(bean.getDTO().getCivico());
			this.localita.setDefaultValue(bean.getDTO().getLocalita());
			this.cap.setDefaultValue(bean.getDTO().getCap());
			this.provincia.setDefaultValue(bean.getDTO().getProvincia());
			if(bean.getDTO().getStato() != null){
				if(bean.getDTO().getStato().equals(EnumStato.A)){
					this.stato.setDefaultValue(true);
				}else {
					this.stato.setDefaultValue(false);
				}
			}

			if(bean.getDominioEnte() != null){
				this.abilitaNodoPagamento.setDefaultValue(true);
				this.abilitaNodoPagamento.setDisabled(true); 
				this.idDominio.setDisabled(true);
				this.idDominio.setDefaultValue(bean.getDominioEnte().getIdDominio());
			}else {
				this.abilitaNodoPagamento.setDefaultValue(false);
				this.abilitaNodoPagamento.setDisabled(false); 
				this.idDominio.setDefaultValue(null);
			}
		} else {
			this.idDominio.setDefaultValue(null);
			this.stato.setDefaultValue(true);
			this.identificativoUnivoco.setDefaultValue(null);
			this.idEnteCreditore.setDefaultValue(null);
			this.idFiscale.setDefaultValue(null);
			this.denominazione.setDefaultValue(null);
			this.indirizzo.setDefaultValue(null);
			this.civico.setDefaultValue(null);
			this.localita.setDefaultValue(null);
			this.cap.setDefaultValue(null);
			this.provincia.setDefaultValue(null);
			this.abilitaNodoPagamento.setDefaultValue(false);
			this.abilitaNodoPagamento.setDisabled(false);
		}

		this.reset();
	}

	@Override
	public void init() throws FactoryException{
		
		this.provinciaPattern = Pattern.compile(PROVINCIA_PATTERN);
		this.capPattern = Pattern.compile(CAP_PATTERN);

		this.setClosable(false);
		this.setId("formEnte");
		this.setNomeForm(null); 

		this.idEnteCreditore = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idEnteCreditore.setRequired(true);
		this.idEnteCreditore.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.idEnteCreditore"));
		this.idEnteCreditore.setName("idEnteCreditore");
		this.idEnteCreditore.setValue(null);

		this.identificativoUnivoco = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.identificativoUnivoco.setRequired(true);
		this.identificativoUnivoco.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.identificativoUnivoco"));
		this.identificativoUnivoco.setName("identificativoUnivoco");
		this.identificativoUnivoco.setValue(null);

		this.idFiscale = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idFiscale.setRequired(true);
		this.idFiscale.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.idFiscale"));
		this.idFiscale.setName("idFiscale");
		this.idFiscale.setValue(null);

		this.denominazione = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.denominazione.setRequired(true);
		this.denominazione.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.denominazione"));
		this.denominazione.setName("denominazione");
		this.denominazione.setValue(null);

		this.indirizzo = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.indirizzo.setRequired(false);
		this.indirizzo.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.indirizzo"));
		this.indirizzo.setName("indirizzo");
		this.indirizzo.setValue(null);
		
		this.civico = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.civico.setRequired(false);
		this.civico.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.civico"));
		this.civico.setName("civico");
		this.civico.setValue(null);

		this.localita = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.localita.setRequired(false);
		this.localita.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.localita"));
		this.localita.setName("localita");
		this.localita.setValue(null);
		
		this.cap = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.cap.setRequired(false);
		this.cap.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.cap"));
		this.cap.setName("cap");
		this.cap.setValue(null);


		this.provincia = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.provincia.setRequired(false);
		this.provincia.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.provincia"));
		this.provincia.setName("provincia");
		this.provincia.setValue(null);

		this.stato = this.getWebGenericProjectFactory().getInputFieldFactory().createBooleanCheckBox();
		this.stato.setRequired(false);
		this.stato.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.attivo"));
		this.stato.setName("statoEnte");
		this.stato.setValue(null);

		this.abilitaNodoPagamento = this.getWebGenericProjectFactory().getInputFieldFactory().createBooleanCheckBox();
		this.abilitaNodoPagamento.setRequired(false);
		this.abilitaNodoPagamento.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.abilitaNodoPagamento"));
		this.abilitaNodoPagamento.setName("abilitaNodoPagamento");
		this.abilitaNodoPagamento.setValue(null);
//		this.abilitaNodoPagamento.setFieldsToUpdate(this.getId() + "_formPnl");
//		this.abilitaNodoPagamento.setForm(this);
		
		this.idDominio = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idDominio.setRequired(true);
		this.idDominio.setRendered(false);
		this.idDominio.setLabel(Utils.getInstance().getMessageFromResourceBundle("ente.idDominio"));
		this.idDominio.setName("idDominio");
		this.idDominio.setValue(null);

	}

	@Override
	public void reset() {
		this.idEnteCreditore.reset();
		this.identificativoUnivoco.reset();
		this.idFiscale.reset();
		this.denominazione.reset();
		this.indirizzo.reset();
		this.civico.reset();
		this.localita.reset();
		this.cap.reset();
		this.provincia.reset();
		this.stato.reset();
		this.idDominio.reset();
		this.abilitaNodoPagamento.reset();
	}

	public String valida(){
//		String _value = this.idEnteCreditore.getValue();
//		if(StringUtils.isEmpty(_value))
//			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.idEnteCreditore.getLabel());

		String _value = this.identificativoUnivoco.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.identificativoUnivoco.getLabel());

		_value = this.idFiscale.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.idFiscale.getLabel());

		_value = this.denominazione.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.denominazione.getLabel());

//		SelectItem idIntPAValue = this.idIntermediarioPA.getValue();
//		if(idIntPAValue!= null){
//			String _idIntPAValue = idIntPAValue.getValue();
//
//			if(_idIntPAValue.equals(CostantiForm.NON_SELEZIONATO))
//				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.idIntermediarioPA.getLabel());
//		}
		
		_value = this.provincia.getValue();
		if(!StringUtils.isEmpty(_value)){
			Matcher matcher = this.provinciaPattern.matcher(_value);
			
			if(!matcher.matches())
				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.provincia.getLabel());
		}
		
		_value = this.cap.getValue();
		if(!StringUtils.isEmpty(_value)){
			Matcher matcher = this.capPattern.matcher(_value);
			
			if(!matcher.matches())
				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.cap.getLabel());
			
			// Se ho settato il CAP devo indicare anche la localita'
			_value = this.localita.getValue();
			if(StringUtils.isEmpty(_value))
				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.localita.getLabel());	
		}
		
		_value = this.civico.getValue();
		
		if(!StringUtils.isEmpty(_value)){
			// Se ho settato il civico devo indicare anche l'indirizzo
			_value = this.indirizzo.getValue();
			if(StringUtils.isEmpty(_value))
				return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.indirizzo.getLabel());
		}
		

		return null;
	}


	public EnteCreditoreModel getEnteCreditore(){
		EnteCreditoreModel enteCreditore = new EnteCreditoreModel();

		enteCreditore.setDenominazione(this.denominazione.getValue());
		enteCreditore.setIdentificativoUnivoco(this.identificativoUnivoco.getValue());
		enteCreditore.setIdFiscale(this.idFiscale.getValue());
		enteCreditore.setIndirizzo(this.indirizzo.getValue());
		enteCreditore.setCivico(this.civico.getValue());
		enteCreditore.setLocalita(this.localita.getValue());
		enteCreditore.setCap(this.cap.getValue());
		enteCreditore.setProvincia(this.provincia.getValue());

		boolean attivo = this.stato.getValue() != null ? this.stato.getValue().booleanValue() : false;

		if(attivo)
			enteCreditore.setStato(EnumStato.A);
		else 
			enteCreditore.setStato(EnumStato.D);

		return enteCreditore;
	}

	public Text getIdEnteCreditore() {
		return this.idEnteCreditore;
	}

	public void setIdEnteCreditore(Text idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public Text getIdentificativoUnivoco() {
		return this.identificativoUnivoco;
	}

	public void setIdentificativoUnivoco(Text identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	public Text getIdFiscale() {
		return this.idFiscale;
	}

	public void setIdFiscale(Text idFiscale) {
		this.idFiscale = idFiscale;
	}

	public Text getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(Text denominazione) {
		this.denominazione = denominazione;
	}

	public Text getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(Text indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public Text getCivico() {
		return this.civico;
	}

	public void setCivico(Text civico) {
		this.civico = civico;
	}

	public Text getLocalita() {
		return this.localita;
	}

	public void setLocalita(Text localita) {
		this.localita = localita;
	}
	
	public Text getCap() {
		return this.cap;
	}

	public void setCap(Text cap) {
		this.cap = cap;
	}

	public Text getProvincia() {
		return this.provincia;
	}

	public void setProvincia(Text provincia) {
		this.provincia = provincia;
	}

	public BooleanCheckBox getStato() {
		return stato;
	}

	public void setStato(BooleanCheckBox stato) {
		this.stato = stato;
	}

	public Text getIdDominio() {
		boolean rendered = this.abilitaNodoPagamento.getValue() != null ? this.abilitaNodoPagamento.getValue().booleanValue() : false;
		
		this.idDominio.setRendered(rendered);
		
		return idDominio;
	}

	public void setIdDominio(Text idDominio) {
		this.idDominio = idDominio;
	}

	public BooleanCheckBox getAbilitaNodoPagamento() {
		return abilitaNodoPagamento;
	}

	public void setAbilitaNodoPagamento(BooleanCheckBox abilitaNodoPagamento) {
		this.abilitaNodoPagamento = abilitaNodoPagamento;
	}


	public void abilitaNodoPagamentoOnChangeListener (ActionEvent ae){
		
		boolean rendered = this.abilitaNodoPagamento.getValue() != null ? this.abilitaNodoPagamento.getValue().booleanValue() : false;
		
		if(rendered)
			this.idDominio.setValue(this.idFiscale.getValue());
		else 
			this.idDominio.reset();
	}


}
