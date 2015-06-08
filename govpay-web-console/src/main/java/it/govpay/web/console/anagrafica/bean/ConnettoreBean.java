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

import it.govpay.ejb.core.model.ConnettoreModel;
import it.govpay.ejb.core.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.core.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.core.model.ConnettorePddModel;

import org.openspcoop2.generic_project.web.bean.BaseBean;
import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.presentation.field.OutputField;
import org.openspcoop2.generic_project.web.presentation.field.OutputGroup;
import org.openspcoop2.generic_project.web.presentation.field.OutputText;

public class ConnettoreBean extends BaseBean<ConnettoreModel, String>{

	private OutputField<String> autenticazione;

	private OutputField<String> abilitato;
	// Url
	private OutputField<String> url;
	private OutputField<String> azioneInUrl;

	// Campi autenticazione HTTP	
	private OutputField<String> httpUser;
	private OutputField<String> httpPassw;

	// Campi autenticazione SSL
	private OutputField<String> tipoSsl;
	private OutputField<String> sslKsType;
	private OutputField<String> sslKsLocation;
	private OutputField<String> sslKsPasswd;
	private OutputField<String> sslPKeyPasswd;
	private OutputField<String> sslTsType;
	private OutputField<String> sslTsLocation;
	private OutputField<String> sslTsPasswd;
	private OutputField<String> sslType;

	private OutputGroup fieldsDatiGenerali = null;
	private OutputGroup fieldsAuthHttp = null;
	private OutputGroup fieldsAuthSsl = null;

	private boolean isConnettorePdd = false;

	private boolean isAb = false;

	private String connettoreId = "conn";

	public ConnettoreBean(){
		this(null);
	}

	public ConnettoreBean(String connettoreId){
		if(connettoreId != null)
			this.connettoreId  = connettoreId;
		init();
	}

	private void init() {

		this.url = new OutputText();
		this.url.setLabel(Utils.getMessageFromResourceBundle("connettore.url"));
		this.url.setName(this.connettoreId +"url");

		this.azioneInUrl = new OutputText();
		this.azioneInUrl.setLabel(Utils.getMessageFromResourceBundle("connettore.azioneInUrl"));
		this.azioneInUrl.setName(this.connettoreId +"azioneInUrl");

		this.autenticazione = new OutputText();
		this.autenticazione.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione"));
		this.autenticazione.setName(this.connettoreId +"autenticazione");

		this.abilitato = new OutputText();
		this.abilitato.setLabel(Utils.getMessageFromResourceBundle("connettore.abilitato"));
		this.abilitato.setName(this.connettoreId +"abilitato");

		this.httpUser = new OutputText();
		this.httpUser.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.http.username"));
		this.httpUser.setName(this.connettoreId +"httpUser");

		this.httpPassw = new OutputText();
		this.httpPassw.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.http.password"));
		this.httpPassw.setName(this.connettoreId +"httpPassw");

		this.tipoSsl = new OutputText();
		this.tipoSsl.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl"));
		this.tipoSsl.setName(this.connettoreId +"tipoSsl");

		this.sslKsType = new OutputText();
		this.sslKsType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsType"));
		this.sslKsType.setName(this.connettoreId +"sslKsType");

		this.sslKsLocation = new OutputText();
		this.sslKsLocation.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsLocation"));
		this.sslKsLocation.setName(this.connettoreId +"sslKsLocation");

		this.sslKsPasswd = new OutputText();
		this.sslKsPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsPasswd"));
		this.sslKsPasswd.setName(this.connettoreId +"sslKsPasswd");

		this.sslPKeyPasswd = new OutputText();
		this.sslPKeyPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslPKeyPasswd"));
		this.sslPKeyPasswd.setName(this.connettoreId +"sslPKeyPasswd");

		this.sslTsType = new OutputText();
		this.sslTsType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsType"));
		this.sslTsType.setName(this.connettoreId +"sslTsType");

		this.sslTsLocation = new OutputText();
		this.sslTsLocation.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsLocation"));
		this.sslTsLocation.setName(this.connettoreId +"sslTsLocation");

		this.sslTsPasswd = new OutputText();
		this.sslTsPasswd.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsPasswd"));
		this.sslTsPasswd.setName(this.connettoreId +"sslTsPasswd");

		this.sslType = new OutputText();
		this.sslType.setLabel(Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.sslType"));
		this.sslType.setName(this.connettoreId +"sslType");

		this.fieldsDatiGenerali = new OutputGroup();
		this.fieldsDatiGenerali.setIdGroup(this.connettoreId + "DatiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");//,labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.abilitato);
		this.fieldsDatiGenerali.addField(this.url);
		this.fieldsDatiGenerali.addField(this.azioneInUrl);
		this.fieldsDatiGenerali.addField(this.autenticazione);

		this.fieldsAuthHttp = new OutputGroup();
		this.fieldsAuthHttp.setIdGroup(this.connettoreId + "AuthHttp");
		this.fieldsAuthHttp.setColumns(2);
		this.fieldsAuthHttp.setRendered(false);
		this.fieldsAuthHttp.addField(this.httpUser);
		this.fieldsAuthHttp.addField(this.httpPassw);
		this.fieldsAuthHttp.setStyleClass("beanTable"); 
		this.fieldsAuthHttp.setColumnClasses("labelAllineataDx,valueAllineataSx");//,labelAllineataDx,valueAllineataSx");

		this.fieldsAuthSsl = new OutputGroup();
		this.fieldsAuthSsl.setIdGroup(this.connettoreId + "AuthSsl");
		this.fieldsAuthSsl.setColumns(2);
		this.fieldsAuthSsl.setRendered(false);
		this.fieldsAuthSsl.addField(this.tipoSsl);
		this.fieldsAuthSsl.addField(this.sslKsType);
		this.fieldsAuthSsl.addField(this.sslKsLocation);
		this.fieldsAuthSsl.addField(this.sslKsPasswd);
		this.fieldsAuthSsl.addField(this.sslPKeyPasswd);
		this.fieldsAuthSsl.addField(this.sslTsType);
		this.fieldsAuthSsl.addField(this.sslTsLocation);
		this.fieldsAuthSsl.addField(this.sslTsPasswd);
		this.fieldsAuthSsl.addField(this.sslType);
		this.fieldsAuthSsl.setStyleClass("beanTable"); 
		this.fieldsAuthSsl.setColumnClasses("labelAllineataDx,valueAllineataSx");//,labelAllineataDx,valueAllineataSx");

	}

	@Override
	public void setDTO(ConnettoreModel dto) {
		super.setDTO(dto);

		this.url.setValue(this.getDTO().getUrl());

		this.isAb = dto != null;

		String abilit = this.isAb ? Utils.getMessageFromResourceBundle("commons.label.SI") :
			Utils.getMessageFromResourceBundle("commons.label.NO");

		this.abilitato.setValue(abilit);

		if(this.isAb){
			EnumAuthType tipoAutenticazione = this.getDTO().getTipoAutenticazione();
			String tipoAuthString = Utils.getMessageFromResourceBundle("connettore.autenticazione.nessuna");

			if(tipoAutenticazione != null){
				if(tipoAutenticazione.equals(EnumAuthType.NONE)){
					this.fieldsAuthSsl.getFields().clear();
					this.fieldsAuthHttp.getFields().clear();
				} else if(tipoAutenticazione.equals(EnumAuthType.HTTPBasic)){
					this.fieldsAuthSsl.getFields().clear();
					this.fieldsAuthHttp.setRendered(true);
					tipoAuthString = Utils.getMessageFromResourceBundle("connettore.autenticazione.http");
				} else {
					this.fieldsAuthSsl.setRendered(true);
					this.fieldsAuthHttp.getFields().clear();
					tipoAuthString = Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl");
				}
			}


			if(this.isConnettorePdd){
				this.azioneInUrl.setValue(((ConnettorePddModel) this.getDTO()).isAzioneInUrl() ? Utils.getMessageFromResourceBundle("commons.label.SI") : Utils.getMessageFromResourceBundle("commons.label.NO"));
				this.azioneInUrl.setRendered(true);
			}else
				this.azioneInUrl.setRendered(false);

			this.autenticazione.setValue(tipoAuthString);

			this.httpUser.setValue(this.getDTO().getHttpUser());
			this.httpPassw.setValue(this.getDTO().getHttpPassw());
			EnumSslType tipoSsl2 = this.getDTO().getTipoSsl();
			String tipoSSlS= null;
			if(tipoSsl2 != null){
				if(tipoSsl2.equals(EnumSslType.CLIENT))
					tipoSSlS = Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl.client");
				else 
					tipoSSlS = Utils.getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl.server");
			}
			this.tipoSsl.setValue(tipoSSlS);

			this.sslKsType.setValue(this.getDTO().getSslKsType());
			this.sslKsLocation.setValue(this.getDTO().getSslKsLocation());
			this.sslKsPasswd.setValue(this.getDTO().getSslKsPasswd());
			this.sslPKeyPasswd.setValue(this.getDTO().getSslPKeyPasswd());
			this.sslTsType.setValue(this.getDTO().getSslTsType());
			this.sslTsLocation.setValue(this.getDTO().getSslTsLocation());
			this.sslTsPasswd.setValue(this.getDTO().getSslTsPasswd());
			this.sslType.setValue(this.getDTO().getSslType());
		} else {
			
			this.fieldsAuthSsl.getFields().clear();
			this.fieldsAuthHttp.getFields().clear();
			this.fieldsDatiGenerali.getFields().clear();
			this.fieldsDatiGenerali.addField(this.abilitato);
		}

	}

	public boolean isConnettorePdd() {
		return isConnettorePdd;
	}

	public String getConnettoreId() {
		return connettoreId;
	}

	public void setConnettoreId(String connettoreId) {
		this.connettoreId = connettoreId;
	}

	public void setConnettorePdd(boolean isConnettorePdd) {
		this.isConnettorePdd = isConnettorePdd;
		
		if(this.isConnettorePdd){
			this.fieldsDatiGenerali.getFields().clear();
			this.fieldsDatiGenerali.addField(this.url);
			this.fieldsDatiGenerali.addField(this.azioneInUrl);
			this.fieldsDatiGenerali.addField(this.autenticazione);
		}else{
			this.fieldsDatiGenerali.getFields().clear();
			this.fieldsDatiGenerali.addField(this.abilitato);

			this.fieldsDatiGenerali.addField(this.url);
			this.fieldsDatiGenerali.addField(this.autenticazione);
		}
	}

	public OutputField<String> getAutenticazione() {
		return autenticazione;
	}

	public void setAutenticazione(OutputField<String> autenticazione) {
		this.autenticazione = autenticazione;
	}

	public OutputField<String> getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(OutputField<String> abilitato) {
		this.abilitato = abilitato;
	}

	public OutputField<String> getUrl() {
		return url;
	}

	public void setUrl(OutputField<String> url) {
		this.url = url;
	}

	public OutputField<String> getHttpUser() {
		return httpUser;
	}

	public void setHttpUser(OutputField<String> httpUser) {
		this.httpUser = httpUser;
	}

	public OutputField<String> getHttpPassw() {
		return httpPassw;
	}

	public void setHttpPassw(OutputField<String> httpPassw) {
		this.httpPassw = httpPassw;
	}

	public OutputField<String> getTipoSsl() {
		return tipoSsl;
	}

	public void setTipoSsl(OutputField<String> tipoSsl) {
		this.tipoSsl = tipoSsl;
	}

	public OutputField<String> getSslKsType() {
		return sslKsType;
	}

	public void setSslKsType(OutputField<String> sslKsType) {
		this.sslKsType = sslKsType;
	}

	public OutputField<String> getSslKsLocation() {
		return sslKsLocation;
	}

	public void setSslKsLocation(OutputField<String> sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}

	public OutputField<String> getSslKsPasswd() {
		return sslKsPasswd;
	}

	public void setSslKsPasswd(OutputField<String> sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}

	public OutputField<String> getSslPKeyPasswd() {
		return sslPKeyPasswd;
	}

	public void setSslPKeyPasswd(OutputField<String> sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}

	public OutputField<String> getSslTsType() {
		return sslTsType;
	}

	public void setSslTsType(OutputField<String> sslTsType) {
		this.sslTsType = sslTsType;
	}

	public OutputField<String> getSslTsLocation() {
		return sslTsLocation;
	}

	public void setSslTsLocation(OutputField<String> sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}

	public OutputField<String> getSslTsPasswd() {
		return sslTsPasswd;
	}

	public void setSslTsPasswd(OutputField<String> sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}

	public OutputField<String> getSslType() {
		return sslType;
	}

	public void setSslType(OutputField<String> sslType) {
		this.sslType = sslType;
	}

	public OutputGroup getFieldsDatiGenerali() {
		return fieldsDatiGenerali;
	}

	public void setFieldsDatiGenerali(OutputGroup fieldsDatiGenerali) {
		this.fieldsDatiGenerali = fieldsDatiGenerali;
	}

	public OutputGroup getFieldsAuthHttp() {
		return fieldsAuthHttp;
	}

	public void setFieldsAuthHttp(OutputGroup fieldsAuthHttp) {
		this.fieldsAuthHttp = fieldsAuthHttp;
	}

	public OutputGroup getFieldsAuthSsl() {
		return fieldsAuthSsl;
	}

	public void setFieldsAuthSsl(OutputGroup fieldsAuthSsl) {
		this.fieldsAuthSsl = fieldsAuthSsl;
	}


	public OutputField<String> getAzioneInUrl() {
		return azioneInUrl;
	}


	public void setAzioneInUrl(OutputField<String> azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}

	//	private void setRendered(FormField<?> field){
	//		boolean rendered = this.isAb;
	//
	//		field.setRendered(rendered);
	//	}

}
