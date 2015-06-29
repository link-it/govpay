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

import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.web.console.anagrafica.bean.StazioneBean;
import it.govpay.web.console.anagrafica.mbean.StazioneMBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.Form;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseForm;
import org.openspcoop2.generic_project.web.input.Text;

@Named("stazioneForm")
public class StazioneForm extends BaseForm implements Form,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODICE_PATTERN = "[0-9]{2}";

	private Pattern codicePattern;

	private Text idIntermediarioPA;
	private Text idIntermediarioStazionePA;
	private Text codice;
	private Text password;

	private StazioneMBean mBean = null;

	public StazioneForm(){
		try {
			init();
		} catch (FactoryException e) {
		}
	}

	@Override
	public void init() throws FactoryException {

		this.codicePattern = Pattern.compile(CODICE_PATTERN);

		this.idIntermediarioPA = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idIntermediarioPA.setRequired(true);
		this.idIntermediarioPA.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.idIntermediarioPA"));
		this.idIntermediarioPA.setName("staz_idIntermediarioPA");
		this.idIntermediarioPA.setDisabled(true);
		this.idIntermediarioPA.setValue(null);
		
		this.idIntermediarioStazionePA = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.idIntermediarioStazionePA.setRequired(true);
		this.idIntermediarioStazionePA.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.form.idIntermediarioStazionePA"));
		this.idIntermediarioStazionePA.setName("staz_idIntermediarioStazionePA");
		this.idIntermediarioStazionePA.setDisabled(true);
		this.idIntermediarioStazionePA.setValue(null);

		this.codice = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.codice.setRequired(true);
		this.codice.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.stazione"));
		this.codice.setName("staz_codice");
		this.codice.setValue(null);
		
		this.password = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.password.setRequired(true);
		this.password.setLabel(Utils.getInstance().getMessageFromResourceBundle("stazione.password"));
		this.password.setName("staz_password");
		this.password.setValue(null);
	}

	@Override
	public void reset() {
		this.idIntermediarioPA.reset();
		this.codice.reset();
		this.idIntermediarioStazionePA.reset();
		this.password.reset();

	}
	
	/**
	 * Inizializza la form con i valori dell'elemento selezionato.
	 * 
	 * @param bean
	 */
	public void setValues(StazioneBean bean){
		if(bean != null){
			this.codice.setDisabled(true);
			
			this.password.setDefaultValue(bean.getDTO().getPassword());
				//				SelectItem defValue = new SelectItem(bean.getStazione().getIdIntermediarioPA(), bean.getStazione().getIdIntermediarioPA());
				this.idIntermediarioPA.setDefaultValue(bean.getDTO().getIdIntermediarioPA());
				this.idIntermediarioStazionePA.setDefaultValue(bean.getDTO().getIdStazioneIntermediarioPA());
				String codTmp = bean.getDTO().getIdStazioneIntermediarioPA();
				if(codTmp != null){
					this.codice.setDefaultValue(codTmp.substring(codTmp.lastIndexOf("_")+ 1));
				}else 
					this.codice.setDefaultValue(null);
		} else {
			this.idIntermediarioStazionePA.setDefaultValue(null);
			this.idIntermediarioPA.setDefaultValue(null);
			this.codice.setDefaultValue(null);
			this.codice.setDisabled(false);
			this.password.setDefaultValue(null);
		}

		this.reset();
	}

	public StazioneModel getStazione(){

		String value2 = this.idIntermediarioPA.getValue();

		if(this.codice.getValue() != null && value2 != null){
			StazioneModel stazione = new StazioneModel();
			stazione.setIdIntermediarioPA(value2);
			stazione.setIdStazioneIntermediarioPA(value2 + "_" + this.codice.getValue()); 
			
			stazione.setPassword(this.password.getValue()); 
			return stazione;
		}
		return null;
	}

	public String valida(){
		// valido password
		String _password = this.password.getValue();
		if(StringUtils.isEmpty(_password))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.password.getLabel());
		
		// valido Codice
		String _codice = this.codice.getValue();

		// Id intermediario valido, validazione codice
		if(StringUtils.isEmpty(_codice))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.codice.getLabel());

		Matcher matcher = this.codicePattern.matcher(this.codice.getValue());
		if(!matcher.matches())
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO , this.codice.getLabel());


		return null;
	}

	public Text getIdIntermediarioPA() {
		return idIntermediarioPA;
	}

	public void setIdIntermediarioPA(Text idIntermediarioPA) {
		this.idIntermediarioPA = idIntermediarioPA;
	}

	public Text getIdIntermediarioStazionePA() {
		return idIntermediarioStazionePA;
	}

	public void setIdIntermediarioStazionePA(
			Text idIntermediarioStazionePA) {
		this.idIntermediarioStazionePA = idIntermediarioStazionePA;
	}

	public Text getCodice() {
		return codice;
	}

	public void setCodice(Text codice) {
		this.codice = codice;
	}

	public StazioneMBean getMbean() {
		return mBean;
	}

	public void setMbean(StazioneMBean mBean) {
		this.mBean = mBean;
	}
	public Text getPassword() {
		return password;
	}

	public void setPassword(Text password) {
		this.password = password;
	}
}

