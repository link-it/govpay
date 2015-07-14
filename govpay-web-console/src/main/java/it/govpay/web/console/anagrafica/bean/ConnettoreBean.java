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
import it.govpay.web.console.utils.Utils;

import org.openspcoop2.generic_project.web.bean.IBean;
import org.openspcoop2.generic_project.web.factory.FactoryException;
import org.openspcoop2.generic_project.web.impl.jsf1.bean.BaseBean;
import org.openspcoop2.generic_project.web.output.OutputGroup;
import org.openspcoop2.generic_project.web.output.Text;

public class ConnettoreBean extends BaseBean<ConnettoreModel, String> implements IBean<ConnettoreModel, String>{ 

	private Text autenticazione;

	private Text abilitato;
	// Url
	private Text url;
	private Text azioneInUrl;

	// Campi autenticazione HTTP	
	private Text httpUser;
	private Text httpPassw;

	// Campi autenticazione SSL
	private Text tipoSsl;
	private Text sslKsType;
	private Text sslKsLocation;
	private Text sslKsPasswd;
	private Text sslPKeyPasswd;
	private Text sslTsType;
	private Text sslTsLocation;
	private Text sslTsPasswd;
	private Text sslType;

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
		try{
			init();
		}catch(FactoryException e) {

		} 
	}

	private void init() throws FactoryException {

		this.url = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.url.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.url"));
		this.url.setName(this.connettoreId +"url");

		this.azioneInUrl = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.azioneInUrl.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.azioneInUrl"));
		this.azioneInUrl.setName(this.connettoreId +"azioneInUrl");

		this.autenticazione = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.autenticazione.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione"));
		this.autenticazione.setName(this.connettoreId +"autenticazione");

		this.abilitato = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.abilitato.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.abilitato"));
		this.abilitato.setName(this.connettoreId +"abilitato");

		this.httpUser = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.httpUser.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.http.username"));
		this.httpUser.setName(this.connettoreId +"httpUser");

		this.httpPassw = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.httpPassw.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.http.password"));
		this.httpPassw.setName(this.connettoreId +"httpPassw");

		this.tipoSsl = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.tipoSsl.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl"));
		this.tipoSsl.setName(this.connettoreId +"tipoSsl");

		this.sslKsType = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslKsType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsType"));
		this.sslKsType.setName(this.connettoreId +"sslKsType");

		this.sslKsLocation = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslKsLocation.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsLocation"));
		this.sslKsLocation.setName(this.connettoreId +"sslKsLocation");

		this.sslKsPasswd = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslKsPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslKsPasswd"));
		this.sslKsPasswd.setName(this.connettoreId +"sslKsPasswd");

		this.sslPKeyPasswd = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslPKeyPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslPKeyPasswd"));
		this.sslPKeyPasswd.setName(this.connettoreId +"sslPKeyPasswd");

		this.sslTsType = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslTsType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsType"));
		this.sslTsType.setName(this.connettoreId +"sslTsType");

		this.sslTsLocation = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslTsLocation.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsLocation"));
		this.sslTsLocation.setName(this.connettoreId +"sslTsLocation");

		this.sslTsPasswd = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslTsPasswd.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslTsPasswd"));
		this.sslTsPasswd.setName(this.connettoreId +"sslTsPasswd");

		this.sslType = this.getWebGenericProjectFactory().getOutputFieldFactory().createText();
		this.sslType.setLabel(Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.sslType"));
		this.sslType.setName(this.connettoreId +"sslType");

		this.fieldsDatiGenerali = this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsDatiGenerali.setId(this.connettoreId + "DatiGenerali");
		this.fieldsDatiGenerali.setColumns(2);
		this.fieldsDatiGenerali.setRendered(true);
		this.fieldsDatiGenerali.setStyleClass("beanTable"); 
		this.fieldsDatiGenerali.setColumnClasses("labelAllineataDx,valueAllineataSx");//,labelAllineataDx,valueAllineataSx");

		this.fieldsDatiGenerali.addField(this.abilitato);
		this.fieldsDatiGenerali.addField(this.url);
		this.fieldsDatiGenerali.addField(this.azioneInUrl);
		this.fieldsDatiGenerali.addField(this.autenticazione);

		this.fieldsAuthHttp =  this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsAuthHttp.setId (this.connettoreId + "AuthHttp");
		this.fieldsAuthHttp.setColumns(2);
		this.fieldsAuthHttp.setRendered(false);
		this.fieldsAuthHttp.addField(this.httpUser);
		this.fieldsAuthHttp.addField(this.httpPassw);
		this.fieldsAuthHttp.setStyleClass("beanTable"); 
		this.fieldsAuthHttp.setColumnClasses("labelAllineataDx,valueAllineataSx");//,labelAllineataDx,valueAllineataSx");

		this.fieldsAuthSsl =  this.getWebGenericProjectFactory().getOutputFieldFactory().createOutputGroup();
		this.fieldsAuthSsl.setId (this.connettoreId + "AuthSsl");
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

//		System.out.println(("[" + this.toString() + "] [" + this.connettoreId + "] Init"));
	}

	@Override
	public String getId() {
		if(this.id == null)
			this.id = this.getDTO().getIdConnettore()  != null ? (this.getDTO().getIdConnettore() + "")  : null;

			return this.id;
	}

	@Override
	public void setDTO(ConnettoreModel dto) {
		super.setDTO(dto);

		this.url.setValue(this.getDTO().getUrl());

		this.isAb = dto != null;

		String abilit = this.isAb ? Utils.getInstance().getMessageFromResourceBundle("commons.label.SI") :
			Utils.getInstance().getMessageFromResourceBundle("commons.label.NO");

		this.abilitato.setValue(abilit);

		if(this.isAb){
			EnumAuthType tipoAutenticazione = this.getDTO().getTipoAutenticazione();
			String tipoAuthString = Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.nessuna");

			if(tipoAutenticazione != null){
				if(tipoAutenticazione.equals(EnumAuthType.NONE)){
					this.fieldsAuthSsl.getFields().clear();
					this.fieldsAuthHttp.getFields().clear();
				} else if(tipoAutenticazione.equals(EnumAuthType.HTTPBasic)){
					this.fieldsAuthSsl.getFields().clear();
					this.fieldsAuthHttp.setRendered(true);
					tipoAuthString = Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.http");
				} else {
					this.fieldsAuthSsl.setRendered(true);
					this.fieldsAuthHttp.getFields().clear();
					tipoAuthString = Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl");
				}
			}


			if(this.isConnettorePdd){
				this.azioneInUrl.setValue(((ConnettorePddModel) this.getDTO()).isAzioneInUrl() ? Utils.getInstance().getMessageFromResourceBundle("commons.label.SI") : Utils.getInstance().getMessageFromResourceBundle("commons.label.NO"));
				this.azioneInUrl.setRendered(true);
				this.abilitato.setRendered(false);
			}else {
				this.azioneInUrl.setRendered(false);
				this.abilitato.setRendered(true);
			}
			
			this.url.setRendered(true);
			this.autenticazione.setRendered(true);

			this.autenticazione.setValue(tipoAuthString);

			this.httpUser.setValue(this.getDTO().getHttpUser());
			this.httpPassw.setValue(this.getDTO().getHttpPassw());
			EnumSslType tipoSsl2 = this.getDTO().getTipoSsl();
			String tipoSSlS= null;
			if(tipoSsl2 != null){
				if(tipoSsl2.equals(EnumSslType.CLIENT))
					tipoSSlS = Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl.client");
				else 
					tipoSSlS = Utils.getInstance().getMessageFromResourceBundle("connettore.autenticazione.ssl.tipoSsl.server");
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
			
			this.abilitato.setRendered(true);
			this.url.setRendered(false);
			this.azioneInUrl.setRendered(false);
			this.autenticazione.setRendered(false);
			
//			this.fieldsDatiGenerali.getFields().clear();
//			this.fieldsDatiGenerali.addField(this.abilitato);
		}

//		System.out.println(("[" + this.toString() + "] [" + this.connettoreId + "] After setDTO Size["+this.fieldsDatiGenerali.getFields().size()+"]")); 
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
			//this.fieldsDatiGenerali.getFields().clear();
			this.abilitato.setRendered(false);
			this.url.setRendered(true);
			this.azioneInUrl.setRendered(true);
			this.autenticazione.setRendered(true);
			
//			this.fieldsDatiGenerali.addField(this.url);
//			this.fieldsDatiGenerali.addField(this.azioneInUrl);
//			this.fieldsDatiGenerali.addField(this.autenticazione);
		}else{
			this.abilitato.setRendered(true);
			this.url.setRendered(true);
			this.azioneInUrl.setRendered(false);
			this.autenticazione.setRendered(true);
			
//			this.fieldsDatiGenerali.getFields().clear();
//			this.fieldsDatiGenerali.addField(this.abilitato);
//			this.fieldsDatiGenerali.addField(this.url);
//			this.fieldsDatiGenerali.addField(this.autenticazione);
		}
		
//		System.out.println(("[" + this.toString() + "] [" + this.connettoreId + "] After setConnettorePdd Size["+this.fieldsDatiGenerali.getFields().size()+"]"));
	}

	public Text getAutenticazione() {
		return autenticazione;
	}

	public void setAutenticazione(Text autenticazione) {
		this.autenticazione = autenticazione;
	}

	public Text getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(Text abilitato) {
		this.abilitato = abilitato;
	}

	public Text getUrl() {
		return url;
	}

	public void setUrl(Text url) {
		this.url = url;
	}

	public Text getHttpUser() {
		return httpUser;
	}

	public void setHttpUser(Text httpUser) {
		this.httpUser = httpUser;
	}

	public Text getHttpPassw() {
		return httpPassw;
	}

	public void setHttpPassw(Text httpPassw) {
		this.httpPassw = httpPassw;
	}

	public Text getTipoSsl() {
		return tipoSsl;
	}

	public void setTipoSsl(Text tipoSsl) {
		this.tipoSsl = tipoSsl;
	}

	public Text getSslKsType() {
		return sslKsType;
	}

	public void setSslKsType(Text sslKsType) {
		this.sslKsType = sslKsType;
	}

	public Text getSslKsLocation() {
		return sslKsLocation;
	}

	public void setSslKsLocation(Text sslKsLocation) {
		this.sslKsLocation = sslKsLocation;
	}

	public Text getSslKsPasswd() {
		return sslKsPasswd;
	}

	public void setSslKsPasswd(Text sslKsPasswd) {
		this.sslKsPasswd = sslKsPasswd;
	}

	public Text getSslPKeyPasswd() {
		return sslPKeyPasswd;
	}

	public void setSslPKeyPasswd(Text sslPKeyPasswd) {
		this.sslPKeyPasswd = sslPKeyPasswd;
	}

	public Text getSslTsType() {
		return sslTsType;
	}

	public void setSslTsType(Text sslTsType) {
		this.sslTsType = sslTsType;
	}

	public Text getSslTsLocation() {
		return sslTsLocation;
	}

	public void setSslTsLocation(Text sslTsLocation) {
		this.sslTsLocation = sslTsLocation;
	}

	public Text getSslTsPasswd() {
		return sslTsPasswd;
	}

	public void setSslTsPasswd(Text sslTsPasswd) {
		this.sslTsPasswd = sslTsPasswd;
	}

	public Text getSslType() {
		return sslType;
	}

	public void setSslType(Text sslType) {
		this.sslType = sslType;
	}

	public OutputGroup getFieldsDatiGenerali() {
		
//		System.out.println(("GETFIELDS [" + this.toString() + "] [" + this.connettoreId + "] Size["+this.fieldsDatiGenerali.getFields().size()+"]")); 
//		for (OutputField<?> field : this.fieldsDatiGenerali.getFields()) {
//			System.out.println(("Field [" + field.getName() + "] Rendered ["+field.isRendered()+"] Value ["+field.getValue()+"]"));	
//		}
		 
		
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


	public Text getAzioneInUrl() {
		return azioneInUrl;
	}


	public void setAzioneInUrl(Text azioneInUrl) {
		this.azioneInUrl = azioneInUrl;
	}

	//	private void setRendered(FormField<?> field){
	//		boolean rendered = this.isAb;
	//
	//		field.setRendered(rendered);
	//	}

}
