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

import it.govpay.ejb.model.ConnettoreModel;
import it.govpay.ejb.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.model.ConnettorePddModel;
import it.govpay.web.console.anagrafica.bean.ConnettoreBean;

import java.io.Serializable;
import java.net.URI;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseForm;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.form.field.BooleanField;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.SelectItem;
import org.openspcoop2.generic_project.web.form.field.SelectListField;
import org.openspcoop2.generic_project.web.form.field.TextField;

public class ConnettoreForm extends BaseForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FormField<String> url;
	private BooleanField azioneInUrl;
	private BooleanField abilitato;
	private SelectListField autenticazione;

	private FormField<String> username;
	private FormField<String> password;

	private SelectListField tipoSsl;
	private FormField<String> sslKsType;
	private FormField<String> sslKsLocation;
	private FormField<String> sslKsPasswd;
	private FormField<String> sslPKeyPasswd;
	private FormField<String> sslTsType;
	private FormField<String> sslTsLocation;
	private FormField<String> sslTsPasswd;
	private FormField<String> sslType;

	private BaseForm form;

	private boolean isConnettorePdd = false;

	private String idConn = "conn";

	public ConnettoreForm (){
		this(null);
	}

	public ConnettoreForm (String idConn){
		if(idConn != null)
			this.idConn = idConn;
		init();
	}

	public void init(){

		this.tipoSsl = new SelectListField();
		this.tipoSsl.setName(this.idConn+"_tipoSsl");
		this.tipoSsl.setValue(null);
		this.tipoSsl.setRequired(true);
		this.tipoSsl.setLabel("connettore.autenticazione.ssl.tipoSsl");

		this.autenticazione = new SelectListField();
		this.autenticazione.setRequired(true);
		this.autenticazione.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione"));
		this.autenticazione.setName(this.idConn+"_autenticazione");
		this.autenticazione.setHandlerMethodPrefix("autenticazione");
		this.autenticazione.setValue(null);
		this.autenticazione.setForm(this);

		this.url = new TextField();
		this.url.setRequired(true);
		this.url.setLabel(Utils.getMessageFromResourceBundle("connettore.url"));
		this.url.setName(this.idConn+"_url");
		this.url.setValue(null);

		this.azioneInUrl = new BooleanField();
		this.azioneInUrl.setRequired(false);
		this.azioneInUrl.setLabel(Utils.getMessageFromResourceBundle("connettore.azioneInUrl"));
		this.azioneInUrl.setName(this.idConn+"_azioneInUrl");
		this.azioneInUrl.setValue(null);

		this.abilitato = new BooleanField();
		this.abilitato.setRequired(false);
		this.abilitato.setLabel(Utils.getMessageFromResourceBundle("connettore.abilitato"));
		this.abilitato.setName(this.idConn+"_abilitato");
		this.abilitato.setHandlerMethodPrefix("abilitato"); 
		this.abilitato.setValue(null);
		this.abilitato.setForm(this);

		this.username = new TextField();
		this.username.setRequired(true);
		this.username.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.http.username"));
		this.username.setName(this.idConn+"_httpUsername");
		this.username.setValue(null);

		this.password = new TextField();
		this.password.setRequired(true);
		this.password.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.http.password"));
		this.password.setName(this.idConn+"_httpPassword");
		this.password.setValue(null);

		this.sslKsType = new TextField();
		this.sslKsType.setRequired(true);
		this.sslKsType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsType"));
		this.sslKsType.setName(this.idConn+"_sslKsType");
		this.sslKsType.setValue(null);

		this.sslKsLocation = new TextField();
		this.sslKsLocation.setRequired(true);
		this.sslKsLocation.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsLocation"));
		this.sslKsLocation.setName(this.idConn+"_sslKsLocation");
		this.sslKsLocation.setValue(null);

		this.sslKsPasswd = new TextField();
		this.sslKsPasswd.setRequired(true);
		this.sslKsPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsPasswd"));
		this.sslKsPasswd.setName(this.idConn+"_sslKsPasswd");
		this.sslKsPasswd.setValue(null);

		this.sslPKeyPasswd = new TextField();
		this.sslPKeyPasswd.setRequired(true);
		this.sslPKeyPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslPKeyPasswd"));
		this.sslPKeyPasswd.setName(this.idConn+"_sslPKeyPasswd");
		this.sslPKeyPasswd.setValue(null);

		this.sslTsType = new TextField();
		this.sslTsType.setRequired(true);
		this.sslTsType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsType"));
		this.sslTsType.setName(this.idConn+"_sslTsType");
		this.sslTsType.setValue(null);

		this.sslTsLocation = new TextField();
		this.sslTsLocation.setRequired(true);
		this.sslTsLocation.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsLocation"));
		this.sslTsLocation.setName(this.idConn+"_sslTsLocation");
		this.sslTsLocation.setValue(null);

		this.sslTsPasswd = new TextField();
		this.sslTsPasswd.setRequired(true);
		this.sslTsPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsPasswd"));
		this.sslTsPasswd.setName(this.idConn+"_sslTsPasswd");
		this.sslTsPasswd.setValue(null);

		this.sslType = new TextField();
		this.sslType.setRequired(true);
		this.sslType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslType"));
		this.sslType.setName(this.idConn+"_sslType");
		this.sslType.setValue(null);

	}

	public String getIdConn() {
		return idConn;
	}

	public void setIdConn(String idConn) {
		this.idConn = idConn;
	}

	public FormField<String> getUrl() {
		setRendered(url, true,false, false);
		return url;
	}

	public void setUrl(FormField<String> url) {
		this.url = url;
	}

	public BooleanField getAzioneInUrl() {
		return azioneInUrl;
	}

	public void setAzioneInUrl(BooleanField azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}

	public BooleanField getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(BooleanField abilitato) {
		this.abilitato = abilitato;
	}

	public SelectListField getAutenticazione() {
		setRendered(autenticazione, true,false, false);
		return autenticazione;
	}

	public void setAutenticazione(SelectListField autenticazione) {
		this.autenticazione = autenticazione;
	}

	public FormField<String> getUsername() {
		setRendered(username, false,true, false);
		return username;
	}

	public void setUsername(FormField<String> username) {
		this.username = username;
	}

	public FormField<String> getPassword() {
		setRendered(password, false,true, false);
		return password;
	}

	public void setPassword(FormField<String> password) {
		this.password = password;
	}

	public SelectListField getTipoSsl() {
		setRendered(tipoSsl, false,false, true);
		return tipoSsl;
	}

	public void setTipoSsl(SelectListField tipoSsl) {
		this.tipoSsl = tipoSsl;
	}

	public FormField<String> getSslKsType() {
		setRendered(sslKsType, false,false, true);
		return sslKsType;
	}

	public void setSslKsType(FormField<String> sslKsType) {
		this.sslKsType = sslKsType;
	}

	public FormField<String> getSslKsLocation() {
		setRendered(sslKsLocation,false, false, true);
		return sslKsLocation;
	}

	public void setSslKsLocation(FormField<String> sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}

	public FormField<String> getSslKsPasswd() {
		setRendered(sslKsPasswd, false,false, true);
		return sslKsPasswd;
	}

	public void setSslKsPasswd(FormField<String> sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}

	public FormField<String> getSslPKeyPasswd() {
		setRendered(sslPKeyPasswd, false,false, true);
		return sslPKeyPasswd;
	}

	public void setSslPKeyPasswd(FormField<String> sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}

	public FormField<String> getSslTsType() {
		setRendered(sslTsType, false,false, true);
		return sslTsType;
	}

	public void setSslTsType(FormField<String> sslTsType) {
		this.sslTsType = sslTsType;
	}

	public FormField<String> getSslTsLocation() {
		setRendered(sslTsLocation,false, false, true);
		return sslTsLocation;
	}

	public void setSslTsLocation(FormField<String> sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}

	public FormField<String> getSslTsPasswd() {
		setRendered(sslTsPasswd, false,false, true);
		return sslTsPasswd;
	}

	public void setSslTsPasswd(FormField<String> sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}

	public FormField<String> getSslType() {
		setRendered(sslType, false,false, true); 
		return sslType;
	}

	public void setSslType(FormField<String> sslType) {
		this.sslType = sslType;
	}

	public BaseForm getForm() {
		return form;
	}

	public void setForm(BaseForm form) {
		this.form = form;
		this.autenticazione.setFieldsToUpdate(this.form.getIdForm() + "_formPnl");
		this.abilitato.setFieldsToUpdate(this.form.getIdForm() + "_formPnl");
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
					this.autenticazione.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
							EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));
				else if(enumTipoAuth.equals(EnumAuthType.HTTPBasic))
					this.autenticazione.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
							EnumAuthType.HTTPBasic.toString(), ("connettore.autenticazione.http")));
				else 
					this.autenticazione.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
							EnumAuthType.SSL.toString(), ("connettore.autenticazione.ssl")));
			}else 
				this.autenticazione.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));

			this.username.setDefaultValue(bean.getDTO().getHttpUser());
			this.password.setDefaultValue(bean.getDTO().getHttpPassw());

			EnumSslType enumSslType = bean.getDTO().getTipoSsl();
			if(enumSslType != null){
				if(enumSslType.equals(EnumSslType.CLIENT))
					this.tipoSsl.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
							EnumSslType.CLIENT.toString(), ("connettore.autenticazione.ssl.tipoSsl.client")));
				else 
					this.tipoSsl.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
							EnumSslType.SERVER.toString(), ("connettore.autenticazione.ssl.tipoSsl.server")));
			}else 
				this.tipoSsl.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO));


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

			this.autenticazione.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(
					EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna")));
			this.username.setDefaultValue(null);
			this.password.setDefaultValue(null);
			this.tipoSsl.setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO));
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
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.url.getLabel());

		try{
			new URI(this.url.getValue());
		}catch(Exception e){
			return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.INPUT_VALORE_NON_VALIDO, this.url.getLabel());
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
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.username.getLabel());

				String _password = this.password.getValue();
				if(StringUtils.isEmpty(_password))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.password.getLabel());

			} else {
				SelectItem sslTypeValue = this.tipoSsl.getValue();
				if(sslTypeValue!= null){
					String _sslType = sslTypeValue.getValue();

					if(_sslType.equals(CostantiForm.NON_SELEZIONATO))
						return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.SELECT_VALORE_NON_VALIDO, this.tipoSsl.getLabel());
				}

				String _sslValue = this.sslKsType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsType.getLabel());

				_sslValue = this.sslKsPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsPasswd.getLabel());

				_sslValue = this.sslKsLocation.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslKsLocation.getLabel());

				_sslValue = this.sslPKeyPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslPKeyPasswd.getLabel());

				_sslValue = this.sslTsType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsType.getLabel());

				_sslValue = this.sslTsLocation.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsLocation.getLabel());

				_sslValue = this.sslTsPasswd.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslTsPasswd.getLabel());

				_sslValue = this.sslType.getValue();
				if(StringUtils.isEmpty(_sslValue))
					return Utils.getMessageWithParamsFromCommonsResourceBundle(CostantiForm.FIELD_NON_PUO_ESSERE_VUOTO, this.sslType.getLabel());

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
		SelectListField selField = (SelectListField) field;

		SelectItem value = selField.getValue();

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
