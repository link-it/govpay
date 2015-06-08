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

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseForm;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.field.BooleanField;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.TextField;

@Named("enteForm")
public class EnteCRUDForm extends BaseForm implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private FormField<String> idEnteCreditore; 
	private FormField<String> identificativoUnivoco;
	private FormField<String> idFiscale; 
	private FormField<String> denominazione; 
	private FormField<String> indirizzo;
	private FormField<String> civico;
	private FormField<String> localita;
	private FormField<String> cap;
	private FormField<String> provincia;
	private FormField<String> idDominio;
	private BooleanField abilitaNodoPagamento;
	private BooleanField stato;
	
	public static final String PROVINCIA_PATTERN = "[a-zA-Z]{2}";
	public static final String CAP_PATTERN = "[0-9]{5}";
	
	private Pattern provinciaPattern;
	private Pattern capPattern;


	public EnteCRUDForm(){
		init();
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
	protected void init() {
		
		this.provinciaPattern = Pattern.compile(PROVINCIA_PATTERN);
		this.capPattern = Pattern.compile(CAP_PATTERN);

		this.setClosable(false);
		this.setIdForm("formEnte");
		this.setNomeForm(null); 

		this.idEnteCreditore = new TextField();
		this.idEnteCreditore.setRequired(true);
		this.idEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("ente.idEnteCreditore"));
		this.idEnteCreditore.setName("idEnteCreditore");
		this.idEnteCreditore.setValue(null);

		this.identificativoUnivoco = new TextField();
		this.identificativoUnivoco.setRequired(true);
		this.identificativoUnivoco.setLabel(Utils.getMessageFromResourceBundle("ente.identificativoUnivoco"));
		this.identificativoUnivoco.setName("identificativoUnivoco");
		this.identificativoUnivoco.setValue(null);

		this.idFiscale = new TextField();
		this.idFiscale.setRequired(true);
		this.idFiscale.setLabel(Utils.getMessageFromResourceBundle("ente.idFiscale"));
		this.idFiscale.setName("idFiscale");
		this.idFiscale.setValue(null);

		this.denominazione = new TextField();
		this.denominazione.setRequired(true);
		this.denominazione.setLabel(Utils.getMessageFromResourceBundle("ente.denominazione"));
		this.denominazione.setName("denominazione");
		this.denominazione.setValue(null);

		this.indirizzo = new TextField();
		this.indirizzo.setRequired(false);
		this.indirizzo.setLabel(Utils.getMessageFromResourceBundle("ente.indirizzo"));
		this.indirizzo.setName("indirizzo");
		this.indirizzo.setValue(null);
		
		this.civico = new TextField();
		this.civico.setRequired(false);
		this.civico.setLabel(Utils.getMessageFromResourceBundle("ente.civico"));
		this.civico.setName("civico");
		this.civico.setValue(null);

		this.localita = new TextField();
		this.localita.setRequired(false);
		this.localita.setLabel(Utils.getMessageFromResourceBundle("ente.localita"));
		this.localita.setName("localita");
		this.localita.setValue(null);
		
		this.cap = new TextField();
		this.cap.setRequired(false);
		this.cap.setLabel(Utils.getMessageFromResourceBundle("ente.cap"));
		this.cap.setName("cap");
		this.cap.setValue(null);


		this.provincia = new TextField();
		this.provincia.setRequired(false);
		this.provincia.setLabel(Utils.getMessageFromResourceBundle("ente.provincia"));
		this.provincia.setName("provincia");
		this.provincia.setValue(null);

		this.stato = new BooleanField();
		this.stato.setRequired(false);
		this.stato.setLabel(Utils.getMessageFromResourceBundle("ente.attivo"));
		this.stato.setName("statoEnte");
		this.stato.setValue(null);

		this.abilitaNodoPagamento = new BooleanField();
		this.abilitaNodoPagamento.setRequired(false);
		this.abilitaNodoPagamento.setLabel(Utils.getMessageFromResourceBundle("ente.abilitaNodoPagamento"));
		this.abilitaNodoPagamento.setName("abilitaNodoPagamento");
		this.abilitaNodoPagamento.setValue(null);
//		this.abilitaNodoPagamento.setFieldsToUpdate(this.getIdForm() + "_formPnl");
//		this.abilitaNodoPagamento.setForm(this);
		
		this.idDominio = new TextField();
		this.idDominio.setRequired(true);
		this.idDominio.setRendered(false);
		this.idDominio.setLabel(Utils.getMessageFromResourceBundle("ente.idDominio"));
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
//			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.idEnteCreditore.getLabel());

		String _value = this.identificativoUnivoco.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.identificativoUnivoco.getLabel());

		_value = this.idFiscale.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.idFiscale.getLabel());

		_value = this.denominazione.getValue();
		if(StringUtils.isEmpty(_value))
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.denominazione.getLabel());

//		SelectItem idIntPAValue = this.idIntermediarioPA.getValue();
//		if(idIntPAValue!= null){
//			String _idIntPAValue = idIntPAValue.getValue();
//
//			if(_idIntPAValue.equals(CostantiForm.NON_SELEZIONATO))
//				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.idIntermediarioPA.getLabel());
//		}
		
		_value = this.provincia.getValue();
		if(!StringUtils.isEmpty(_value)){
			Matcher matcher = this.provinciaPattern.matcher(_value);
			
			if(!matcher.matches())
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.provincia.getLabel());
		}
		
		_value = this.cap.getValue();
		if(!StringUtils.isEmpty(_value)){
			Matcher matcher = this.capPattern.matcher(_value);
			
			if(!matcher.matches())
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.cap.getLabel());
			
			// Se ho settato il CAP devo indicare anche la localita'
			_value = this.localita.getValue();
			if(StringUtils.isEmpty(_value))
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.localita.getLabel());	
		}
		
		_value = this.civico.getValue();
		
		if(!StringUtils.isEmpty(_value)){
			// Se ho settato il civico devo indicare anche l'indirizzo
			_value = this.indirizzo.getValue();
			if(StringUtils.isEmpty(_value))
				return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.indirizzo.getLabel());
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

	public FormField<String> getIdEnteCreditore() {
		return this.idEnteCreditore;
	}

	public void setIdEnteCreditore(FormField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public FormField<String> getIdentificativoUnivoco() {
		return this.identificativoUnivoco;
	}

	public void setIdentificativoUnivoco(FormField<String> identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	public FormField<String> getIdFiscale() {
		return this.idFiscale;
	}

	public void setIdFiscale(FormField<String> idFiscale) {
		this.idFiscale = idFiscale;
	}

	public FormField<String> getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(FormField<String> denominazione) {
		this.denominazione = denominazione;
	}

	public FormField<String> getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(FormField<String> indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public FormField<String> getCivico() {
		return this.civico;
	}

	public void setCivico(FormField<String> civico) {
		this.civico = civico;
	}

	public FormField<String> getLocalita() {
		return this.localita;
	}

	public void setLocalita(FormField<String> localita) {
		this.localita = localita;
	}
	
	public FormField<String> getCap() {
		return this.cap;
	}

	public void setCap(FormField<String> cap) {
		this.cap = cap;
	}

	public FormField<String> getProvincia() {
		return this.provincia;
	}

	public void setProvincia(FormField<String> provincia) {
		this.provincia = provincia;
	}

	public BooleanField getStato() {
		return stato;
	}

	public void setStato(BooleanField stato) {
		this.stato = stato;
	}

	public FormField<String> getIdDominio() {
		boolean rendered = this.abilitaNodoPagamento.getValue() != null ? this.abilitaNodoPagamento.getValue().booleanValue() : false;
		
		this.idDominio.setRendered(rendered);
		
		return idDominio;
	}

	public void setIdDominio(FormField<String> idDominio) {
		this.idDominio = idDominio;
	}

	public BooleanField getAbilitaNodoPagamento() {
		return abilitaNodoPagamento;
	}

	public void setAbilitaNodoPagamento(BooleanField abilitaNodoPagamento) {
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
