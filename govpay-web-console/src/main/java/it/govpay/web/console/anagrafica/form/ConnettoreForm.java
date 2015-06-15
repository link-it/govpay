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

import it.govpay.ejb.core.model.ConnettoreModel;
import it.govpay.ejb.core.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.core.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.core.model.ConnettorePddModel;
import it.govpay.web.console.anagrafica.bean.ConnettoreBean;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.net.URI;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.Form;
import org.openspcoop2.generic_project.web.impl.jsf1.form.BaseForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem;
import org.openspcoop2.generic_project.web.input.BooleanCheckBox;
import org.openspcoop2.generic_project.web.input.FormField;
import org.openspcoop2.generic_project.web.input.SelectList;
import org.openspcoop2.generic_project.web.input.Text;

public class ConnettoreForm extends BaseForm implements Form,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Text url;
	private BooleanCheckBox azioneInUrl;
	private BooleanCheckBox abilitato;
	private SelectList<SelectItem> autenticazione;

	private Text username;
	private Text password;

	private SelectList<SelectItem> tipoSsl;
	private Text sslKsType;
	private Text sslKsLocation;
	private Text sslKsPasswd;
	private Text sslPKeyPasswd;
	private Text sslTsType;
	private Text sslTsLocation;
	private Text sslTsPasswd;
	private Text sslType;

	private Form form;

	private boolean isConnettorePdd = false;

	private String idConn = "conn";

	public ConnettoreForm (){
		this(null);
	}

	public ConnettoreForm (String idConn){
		if(idConn != null)
			this.idConn = idConn;
		try {
			init();
		} catch (FactoryException e) {
		}
	}

	public void init() throws FactoryException{

		this.tipoSsl = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.tipoSsl.setName(this.idConn+"_tipoSsl");
		this.tipoSsl.setValue(null);
		this.tipoSsl.setRequired(true);
		this.tipoSsl.setLabel("connettore.autenticazione.ssl.tipoSsl");

		this.autenticazione = this.getWebGenericProjectFactory().getInputFieldFactory().createSelectList();
		this.autenticazione.setRequired(true);
		this.autenticazione.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione"));
		this.autenticazione.setName(this.idConn+"_autenticazione");
		this.autenticazione.setHandlerMethodPrefix("autenticazione");
		this.autenticazione.setValue(null);
		this.autenticazione.setForm(this);

		this.url = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.url.setRequired(true);
		this.url.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.url"));
		this.url.setName(this.idConn+"_url");
		this.url.setValue(null);

		this.azioneInUrl = this.getWebGenericProjectFactory().getInputFieldFactory().createBooleanCheckBox();
		this.azioneInUrl.setRequired(false);
		this.azioneInUrl.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.azioneInUrl"));
		this.azioneInUrl.setName(this.idConn+"_azioneInUrl");
		this.azioneInUrl.setValue(null);

		this.abilitato = this.getWebGenericProjectFactory().getInputFieldFactory().createBooleanCheckBox();
		this.abilitato.setRequired(false);
		this.abilitato.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.abilitato"));
		this.abilitato.setName(this.idConn+"_abilitato");
		this.abilitato.setHandlerMethodPrefix("abilitato"); 
		this.abilitato.setValue(null);
		this.abilitato.setForm(this);

		this.username = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.username.setRequired(true);
		this.username.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.http.username"));
		this.username.setName(this.idConn+"_httpUsername");
		this.username.setValue(null);

		this.password = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.password.setRequired(true);
		this.password.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.http.password"));
		this.password.setName(this.idConn+"_httpPassword");
		this.password.setValue(null);

		this.sslKsType = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslKsType.setRequired(true);
		this.sslKsType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsType"));
		this.sslKsType.setName(this.idConn+"_sslKsType");
		this.sslKsType.setValue(null);

		this.sslKsLocation = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslKsLocation.setRequired(true);
		this.sslKsLocation.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsLocation"));
		this.sslKsLocation.setName(this.idConn+"_sslKsLocation");
		this.sslKsLocation.setValue(null);

		this.sslKsPasswd = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslKsPasswd.setRequired(true);
		this.sslKsPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsPasswd"));
		this.sslKsPasswd.setName(this.idConn+"_sslKsPasswd");
		this.sslKsPasswd.setValue(null);

		this.sslPKeyPasswd = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslPKeyPasswd.setRequired(true);
		this.sslPKeyPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslPKeyPasswd"));
		this.sslPKeyPasswd.setName(this.idConn+"_sslPKeyPasswd");
		this.sslPKeyPasswd.setValue(null);

		this.sslTsType = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslTsType.setRequired(true);
		this.sslTsType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsType"));
		this.sslTsType.setName(this.idConn+"_sslTsType");
		this.sslTsType.setValue(null);

		this.sslTsLocation = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslTsLocation.setRequired(true);
		this.sslTsLocation.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsLocation"));
		this.sslTsLocation.setName(this.idConn+"_sslTsLocation");
		this.sslTsLocation.setValue(null);

		this.sslTsPasswd = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslTsPasswd.setRequired(true);
		this.sslTsPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsPasswd"));
		this.sslTsPasswd.setName(this.idConn+"_sslTsPasswd");
		this.sslTsPasswd.setValue(null);

		this.sslType = this.getWebGenericProjectFactory().getInputFieldFactory().createText();
		this.sslType.setRequired(true);
		this.sslType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslType"));
		this.sslType.setName(this.idConn+"_sslType");
		this.sslType.setValue(null);

	}

	public String getIdConn() {
		return idConn;
	}

	public void setIdConn(String idConn) {
		this.idConn = idConn;
	}

	public Text getUrl() {
		setRendered(url, true,false, false);
		return url;
	}

	public void setUrl(Text url) {
		this.url = url;
	}

	public BooleanCheckBox getAzioneInUrl() {
		return azioneInUrl;
	}

	public void setAzioneInUrl(BooleanCheckBox azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}

	public BooleanCheckBox getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(BooleanCheckBox abilitato) {
		this.abilitato = abilitato;
	}

	public SelectList<SelectItem> getAutenticazione() {
		setRendered(autenticazione, true,false, false);
		return autenticazione;
	}

	public void setAutenticazione(SelectList<SelectItem> autenticazione) {
		this.autenticazione = autenticazione;
	}

	public Text getUsername() {
		setRendered(username, false,true, false);
		return username;
	}

	public void setUsername(Text username) {
		this.username = username;
	}

	public Text getPassword() {
		setRendered(password, false,true, false);
		return password;
	}

	public void setPassword(Text password) {
		this.password = password;
	}

	public SelectList<SelectItem> getTipoSsl() {
		setRendered(tipoSsl, false,false, true);
		return tipoSsl;
	}

	public void setTipoSsl(SelectList<SelectItem> tipoSsl) {
		this.tipoSsl = tipoSsl;
	}

	public Text getSslKsType() {
		setRendered(sslKsType, false,false, true);
		return sslKsType;
	}

	public void setSslKsType(Text sslKsType) {
		this.sslKsType = sslKsType;
	}

	public Text getSslKsLocation() {
		setRendered(sslKsLocation,false, false, true);
		return sslKsLocation;
	}

	public void setSslKsLocation(Text sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}

	public Text getSslKsPasswd() {
		setRendered(sslKsPasswd, false,false, true);
		return sslKsPasswd;
	}

	public void setSslKsPasswd(Text sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}

	public Text getSslPKeyPasswd() {
		setRendered(sslPKeyPasswd, false,false, true);
		return sslPKeyPasswd;
	}

	public void setSslPKeyPasswd(Text sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}

	public Text getSslTsType() {
		setRendered(sslTsType, false,false, true);
		return sslTsType;
	}

	public void setSslTsType(Text sslTsType) {
		this.sslTsType = sslTsType;
	}

	public Text getSslTsLocation() {
		setRendered(sslTsLocation,false, false, true);
		return sslTsLocation;
	}

	public void setSslTsLocation(Text sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}

	public Text getSslTsPasswd() {
		setRendered(sslTsPasswd, false,false, true);
		return sslTsPasswd;
	}

	public void setSslTsPasswd(Text sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}

	public Text getSslType() {
		setRendered(sslType, false,false, true); 
		return sslType;
	}

	public void setSslType(Text sslType) {
		this.sslType = sslType;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
		this.autenticazione.setFieldsToUpdate(this.form.getId() + "_formPnl");
		this.abilitato.setFieldsToUpdate(this.form.getId() + "_formPnl");
	}

	public void reset(){
		this.url.reset();
		this.azioneInUrl.reset();
		this.abilitato.reset();
		this.autenticazione.reset();
		this.username.reset();
		this.password.reset();
		this.tipoSsl.reset();
		this.sslKsType.reset();
		this.sslKsLocation.reset();
		this.sslKsPasswd.reset();
		this.sslPKeyPasswd.reset();
		this.sslTsType.reset();
		this.sslTsLocation.reset();
		this.sslTsPasswd.reset();
		this.sslType.reset();
	}

	public void setValues(ConnettoreBean bean){

		if(!isConnettorePdd){
			if(bean == null)
				this.abilitato.setDefaultValue(false);
			else {
				boolean ab = (bean.getDTO().getTipoAutenticazione() != null && bean.getDTO().getUrl() != null);
				this.abilitato.setDefaultValue(ab);
			}
		}else{
			this.abilitato.setDefaultValue(true);
			if(bean == null)
				this.azioneInUrl.setDefaultValue(false);
			else 
				this.azioneInUrl.setDefaultValue(((ConnettorePddModel)bean.getDTO()).isAzioneInUrl());
		}
		if(bean != null){
			this.url.setDefaultValue(bean.getDTO().getUrl());


			EnumAuthType enumTipoAuth = bean.getDTO().getTipoAutenticazione();

			if(enumTipoAuth!= null){
				if(enumTipoAuth.equals(EnumAuthType.NONE))
					this.autenticazione.setDefaultValue(new SelectItem(
							EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));
				else if(enumTipoAuth.equals(EnumAuthType.HTTPBasic))
					this.autenticazione.setDefaultValue(new SelectItem(
							EnumAuthType.HTTPBasic.toString(), ("connettore.autenticazione.http")));
				else 
					this.autenticazione.setDefaultValue(new SelectItem(
							EnumAuthType.SSL.toString(), ("connettore.autenticazione.ssl")));
			}else 
				this.autenticazione.setDefaultValue(new SelectItem(
						EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));

			this.username.setDefaultValue(bean.getDTO().getHttpUser());
			this.password.setDefaultValue(bean.getDTO().getHttpPassw());

			EnumSslType enumSslType = bean.getDTO().getTipoSsl();
			if(enumSslType != null){
				if(enumSslType.equals(EnumSslType.CLIENT))
					this.tipoSsl.setDefaultValue(new SelectItem(
							EnumSslType.CLIENT.toString(), ("connettore.autenticazione.ssl.tipoSsl.client")));
				else 
					this.tipoSsl.setDefaultValue(new SelectItem(
							EnumSslType.SERVER.toString(), ("connettore.autenticazione.ssl.tipoSsl.server")));
			}else 
				this.tipoSsl.setDefaultValue(new SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO));


			this.sslKsType.setDefaultValue(bean.getDTO().getSslKsType());
			this.sslKsLocation.setDefaultValue(bean.getDTO().getSslKsLocation());
			this.sslKsPasswd.setDefaultValue(bean.getDTO().getSslKsPasswd());
			this.sslPKeyPasswd.setDefaultValue(bean.getDTO().getSslPKeyPasswd());
			this.sslTsType.setDefaultValue(bean.getDTO().getSslTsType());
			this.sslTsLocation.setDefaultValue(bean.getDTO().getSslTsLocation());
			this.sslTsPasswd.setDefaultValue(bean.getDTO().getSslTsPasswd());
			this.sslType.setDefaultValue(bean.getDTO().getSslType());
		}else {
			this.url.setDefaultValue(null);

			this.autenticazione.setDefaultValue(new SelectItem(EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));
			this.username.setDefaultValue(null);
			this.password.setDefaultValue(null);
			this.tipoSsl.setDefaultValue(new SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO));
			this.sslKsType.setDefaultValue(null);
			this.sslKsLocation.setDefaultValue(null);
			this.sslKsPasswd.setDefaultValue(null);
			this.sslPKeyPasswd.setDefaultValue(null);
			this.sslTsType.setDefaultValue(null);
			this.sslTsLocation.setDefaultValue(null);
			this.sslTsPasswd.setDefaultValue(null);
			this.sslType.setDefaultValue(null);
		}
	}

	public ConnettoreModel getConnettore(){
		ConnettoreModel connettore = null;


		if(this.isConnettorePdd){
			connettore = new ConnettorePddModel();

			Boolean value = this.azioneInUrl.getValue();
			((ConnettorePddModel) connettore).setAzioneInUrl(value != null ? value.booleanValue() : false);
		}
		else {
			connettore = new ConnettoreModel();
			Boolean value = this.abilitato.getValue();

			// Se il connettore non e' abilitato lo imposto a null.
			boolean v =value != null ? value.booleanValue() : false;
			if(!v)
				return null;

		}

		connettore.setUrl(this.url.getValue());

		SelectItem value = this.autenticazione.getValue();
		if(value!= null){
			String value2 = value.getValue();

			EnumAuthType auth = EnumAuthType.valueOf(value2);

			connettore.setTipoAutenticazione(auth);

			if(auth.equals(EnumAuthType.NONE))
			{

			}
			else if(auth.equals(EnumAuthType.HTTPBasic)){
				connettore.setHttpUser(this.username.getValue());
				connettore.setHttpPassw(this.password.getValue());
			} else {
				SelectItem sslTypeValue = this.tipoSsl.getValue();
				if(sslTypeValue!= null){
					String _sslType = sslTypeValue.getValue();

					EnumSslType sslTypes = EnumSslType.valueOf(_sslType);
					connettore.setTipoSsl(sslTypes);
				}

				connettore.setSslKsType(this.sslKsType.getValue());
				connettore.setSslKsPasswd(this.sslKsPasswd.getValue());
				connettore.setSslKsLocation(this.sslKsLocation.getValue());
				connettore.setSslPKeyPasswd(this.sslPKeyPasswd.getValue());
				connettore.setSslTsType(this.sslTsType.getValue());
				connettore.setSslTsLocation(this.sslTsLocation.getValue());
				connettore.setSslTsPasswd(this.sslTsPasswd.getValue());
				connettore.setSslType(this.sslType.getValue());

			}
		}

		return connettore;
	}


	public String valida(){

		if(!isConnettorePdd){
			Boolean value = this.abilitato.getValue();
			boolean abilt = (value != null ? value.booleanValue() : false);

			// Se il connettore non e' abilitato ignoro la validazione tanto lo imposto a null
			if(!abilt)
				return null;
		}

		// URL
		String _codice = this.url.getValue();
		if(StringUtils.isEmpty(_codice))
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.url.getLabel());

		try{
			new URI(this.url.getValue());
		}catch(Exception e){
			return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.url.getLabel());
		}

		SelectItem value = this.autenticazione.getValue();

		if(value!= null){
			String value2 = value.getValue();

			EnumAuthType auth = EnumAuthType.valueOf(value2);

			if(auth.equals(EnumAuthType.NONE))
			{}
			else if(auth.equals(EnumAuthType.HTTPBasic)){
				String _usr = this.username.getValue();
				if(StringUtils.isEmpty(_usr))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.username.getLabel());

				String _password = this.password.getValue();
				if(StringUtils.isEmpty(_password))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.password.getLabel());

			} else {
				SelectItem sslTypeValue = this.tipoSsl.getValue();
				if(sslTypeValue!= null){
					String _sslType = sslTypeValue.getValue();

					if(_sslType.equals(CostantiForm.NON_SELEZIONATO))
						return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.tipoSsl.getLabel());
				}

				String _sslValue = this.sslKsType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsType.getLabel());

				_sslValue = this.sslKsPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsPasswd.getLabel());

				_sslValue = this.sslKsLocation.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsLocation.getLabel());

				_sslValue = this.sslPKeyPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslPKeyPasswd.getLabel());

				_sslValue = this.sslTsType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsType.getLabel());

				_sslValue = this.sslTsLocation.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsLocation.getLabel());

				_sslValue = this.sslTsPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsPasswd.getLabel());

				_sslValue = this.sslType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getInstance().getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslType.getLabel());

			}
		}


		return null;
	}

	public boolean isConnettorePdd() {
		return isConnettorePdd;
	}

	public void setConnettorePdd(boolean isConnettorePdd) {
		this.isConnettorePdd = isConnettorePdd;
		if(isConnettorePdd){
			this.azioneInUrl.setRendered(true);
			this.abilitato.setRendered(false);
			this.abilitato.setDefaultValue(true);
		} else {
			this.abilitato.setRendered(true);
			this.azioneInUrl.setRendered(false);
		}
	}

	private void setRendered(FormField<?> field, boolean common, boolean isHttp, boolean isSsl){


		boolean rendered = false;
		Boolean value1= this.abilitato.getValue();
		boolean abilt = (value1 != null ? value1.booleanValue() : false);

		if(common){
			if(!isConnettorePdd){
				if(abilt){
					rendered = true;
				} else{ 
					rendered =false;
				}
			} else 
				rendered = true;
		} else {
			if(abilt){

				SelectItem value = this.autenticazione.getValue();

				if(value!= null){
					String value2 = value.getValue();

					if(value2 != null){
						EnumAuthType auth = EnumAuthType.valueOf(value2);

						if(auth.equals(EnumAuthType.NONE))
							rendered = false;
						else if(auth.equals(EnumAuthType.HTTPBasic)){
							if(isHttp)
								rendered =true;

							if(isSsl)
								rendered =false; 
						} else {
							if(isHttp)
								rendered =false;

							if(isSsl)
								rendered =true;
						}
					}
				}
			}
		}

		field.setRendered(rendered);
	}

	public void autenticazioneSelectListener(FormField<?> field, ActionEvent ae){
		SelectList<?> selField = (SelectList<?>) field;
		SelectItem value = (SelectItem) selField.getValue(); 

		if(value!= null){
			String value2 = value.getValue();

			EnumAuthType auth = EnumAuthType.valueOf(value2);

			if(auth.equals(EnumAuthType.NONE)){
				this.username.reset();
				this.password.reset();
				this.tipoSsl.reset();
				this.sslKsType.reset();
				this.sslKsLocation.reset();
				this.sslKsPasswd.reset();
				this.sslPKeyPasswd.reset();
				this.sslTsType.reset();
				this.sslTsLocation.reset();
				this.sslTsPasswd.reset();
				this.sslType.reset();
			}else if(auth.equals(EnumAuthType.HTTPBasic)){
				this.username.reset();
				this.password.reset();
				this.tipoSsl.reset();
				this.sslKsType.reset();
				this.sslKsLocation.reset();
				this.sslKsPasswd.reset();
				this.sslPKeyPasswd.reset();
				this.sslTsType.reset();
				this.sslTsLocation.reset();
				this.sslTsPasswd.reset();
				this.sslType.reset();
			} else {
				this.username.reset();
				this.password.reset();
				this.tipoSsl.reset();
				this.sslKsType.reset();
				this.sslKsLocation.reset();
				this.sslKsPasswd.reset();
				this.sslPKeyPasswd.reset();
				this.sslTsType.reset();
				this.sslTsLocation.reset();
				this.sslTsPasswd.reset();
				this.sslType.reset();
			}
		}
	}


	public void abilitatoOnChangeListener(FormField<?> field, ActionEvent ae){

		this.autenticazione.reset();

	}
}
