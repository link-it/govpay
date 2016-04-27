/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.anagrafica.connettori;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Connettore.EnumAuthType;
import it.govpay.bd.model.Connettore.EnumSslType;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.Password;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslKsLocation;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslKsPasswd;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslKsType;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslPKeyPasswd;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslTsLocation;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslTsPasswd;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslTsType;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.SslType;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.TipoSsl;
import it.govpay.web.rs.dars.anagrafica.intermediari.input.Username;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class ConnettoreHandler {

	private static Map<String, Map<String, ParamField<?>>> infoCreazioneMap = null; 
	private String pathServizio =null;
	private String nomeServizio = null;
	private String nomeConnettore = null;
	public static final String TIPO_AUTENTICAZIONE_VALUE_NONE = "NONE";
	public static final String TIPO_AUTENTICAZIONE_VALUE_HTTP_BASIC = "HTTPBasic";
	public static final String TIPO_AUTENTICAZIONE_VALUE_SSL = "SSL";


	public ConnettoreHandler(String nomeConnettore,String nomeServizio,String pathServizio){
		this.pathServizio = pathServizio;
		this.nomeServizio = nomeServizio;
		this.nomeConnettore = nomeConnettore;
		
		if(infoCreazioneMap == null)
			infoCreazioneMap = new HashMap<String, Map<String,ParamField<?>>>();
	}

	@SuppressWarnings("unchecked")
	public List<ParamField<?>> getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {

		String urlId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".url.id");
		String tipoAutenticazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.id");
		String usernameId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".username.id");
		String passwordId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".password.id");
		String azioneInURLId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".azioneInURL.id");

		String tipoSslId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.id");
		String sslKsTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsType.id");
		String sslKsLocationId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsLocation.id");
		String sslKsPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsPasswd.id");
		String sslPKeyPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslPKeyPasswd.id");
		String sslTsTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsType.id");
		String sslTsLocationId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsLocation.id");
		String sslTsPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsPasswd.id");
		String sslTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslType.id");

		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeConnettore);
		
		if(mappaCreazione == null){
			mappaCreazione = new HashMap<String, ParamField<?>>();

			// url
			String urlLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".url.label");
			InputText url = new InputText(urlId, urlLabel, null, true, false, true, 1, 255);
			url.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".url.errorMessage"));
			mappaCreazione.put(urlId, url);

			// azioneInUrL
			String azioneInURLLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".azioneInURL.label");
			CheckButton azioneInURL = new CheckButton(azioneInURLId, azioneInURLLabel, false, false, false, true);
			azioneInURL.setAvanzata(true); 
			mappaCreazione.put(azioneInURLId, azioneInURL);

			// tipo autenticazione
			String tipoAutenticazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.label");
			List<Voce<String>> tipiAutenticazione = new ArrayList<Voce<String>>();
			tipiAutenticazione.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.nessuna"),TIPO_AUTENTICAZIONE_VALUE_NONE));
			tipiAutenticazione.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.basic"),TIPO_AUTENTICAZIONE_VALUE_HTTP_BASIC));
			tipiAutenticazione.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.ssl"),TIPO_AUTENTICAZIONE_VALUE_SSL));
			SelectList<String> tipoAutenticazione = new SelectList<String>(tipoAutenticazioneId, tipoAutenticazioneLabel, TIPO_AUTENTICAZIONE_VALUE_NONE, true, false, true, tipiAutenticazione);
			mappaCreazione.put(tipoAutenticazioneId, tipoAutenticazione);

			List<RawParamValue> tipoAutenticazioneValues = new ArrayList<RawParamValue>();
			tipoAutenticazioneValues.add(new RawParamValue(tipoAutenticazioneId, TIPO_AUTENTICAZIONE_VALUE_NONE));

			// BASIC
			// username
			String usernameLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".username.label");
			URI refreshUsernameUri = this.getUriField(uriInfo, bd, usernameId); 
			Username username = new Username(this.nomeConnettore,this.nomeServizio,usernameId, usernameLabel, 1, 20, refreshUsernameUri , tipoAutenticazioneValues);
			username.addDependencyField(tipoAutenticazione);
			username.init(tipoAutenticazioneValues);
			username.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".username.errorMessage"));
			mappaCreazione.put(usernameId, username);

			// password
			String passwordLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".password.label");
			URI refreshPasswordUri = this.getUriField(uriInfo, bd, passwordId); 
			Password password = new Password(this.nomeConnettore,this.nomeServizio,passwordId, passwordLabel, 1, 20, refreshPasswordUri , tipoAutenticazioneValues);
			password.addDependencyField(tipoAutenticazione);
			password.init(tipoAutenticazioneValues);
			password.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".password.errorMessage"));
			mappaCreazione.put(passwordId, password);


			// SSL

			//tipoSSl
			String tipoSslLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.label");
			URI refreshTipoSslUri = this.getUriField(uriInfo, bd, tipoSslId);
			TipoSsl tipoSsl = new TipoSsl(this.nomeConnettore,this.nomeServizio,tipoSslId, tipoSslLabel, refreshTipoSslUri, tipoAutenticazioneValues, bd);
			tipoSsl.addDependencyField(tipoAutenticazione);
			tipoSsl.init(tipoAutenticazioneValues);
			mappaCreazione.put(tipoSslId, tipoSsl);

			// sslKsType
			String sslKsTypeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsType.label");
			URI refreshSslKsTypeUri = this.getUriField(uriInfo, bd, sslKsTypeId);
			SslKsType sslKsType = new SslKsType(this.nomeConnettore,this.nomeServizio,sslKsTypeId, sslKsTypeLabel, 1,255, refreshSslKsTypeUri, tipoAutenticazioneValues);
			sslKsType.addDependencyField(tipoAutenticazione);
			sslKsType.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslKsTypeId, sslKsType);

			// sslKsLocation
			String sslKsLocationLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsLocation.label");
			URI refreshSslKsLocationUri = this.getUriField(uriInfo, bd, sslKsLocationId);
			SslKsLocation sslKsLocation = new SslKsLocation(this.nomeConnettore,this.nomeServizio,sslKsLocationId, sslKsLocationLabel, 1,255, refreshSslKsLocationUri, tipoAutenticazioneValues);
			sslKsLocation.addDependencyField(tipoAutenticazione);
			sslKsLocation.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslKsLocationId, sslKsLocation);

			// sslKsPasswd
			String sslKsPasswdLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsPasswd.label");
			URI refreshSslKsPasswdUri = this.getUriField(uriInfo, bd, sslKsPasswdId);
			SslKsPasswd sslKsPasswd = new SslKsPasswd(this.nomeConnettore,this.nomeServizio,sslKsPasswdId, sslKsPasswdLabel, 1,255, refreshSslKsPasswdUri, tipoAutenticazioneValues);
			sslKsPasswd.addDependencyField(tipoAutenticazione);
			sslKsPasswd.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslKsPasswdId, sslKsPasswd);

			// sslPKeyPasswd
			String sslPKeyPasswdLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslPKeyPasswd.label");
			URI refreshSslPKeyPasswdUri = this.getUriField(uriInfo, bd, sslPKeyPasswdId);
			SslPKeyPasswd sslPKeyPasswd = new SslPKeyPasswd(this.nomeConnettore,this.nomeServizio,sslPKeyPasswdId, sslPKeyPasswdLabel, 1,255, refreshSslPKeyPasswdUri, tipoAutenticazioneValues);
			sslPKeyPasswd.addDependencyField(tipoAutenticazione);
			sslPKeyPasswd.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslPKeyPasswdId, sslPKeyPasswd);

			// sslTsType
			String sslTsTypeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsType.label");
			URI refreshSslTsTypeUri = this.getUriField(uriInfo, bd, sslTsTypeId);
			SslTsType sslTsType = new SslTsType(this.nomeConnettore,this.nomeServizio,sslTsTypeId, sslTsTypeLabel, 1,255, refreshSslTsTypeUri,  tipoAutenticazioneValues);
			sslTsType.addDependencyField(tipoAutenticazione);
			sslTsType.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslTsTypeId, sslTsType);

			// sslTsLocation
			String sslTsLocationLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsLocation.label");
			URI refreshSslTsLocationlUri = this.getUriField(uriInfo, bd, sslTsLocationId);
			SslTsLocation sslTsLocation = new SslTsLocation(this.nomeConnettore,this.nomeServizio,sslTsLocationId, sslTsLocationLabel, 1,255, refreshSslTsLocationlUri, tipoAutenticazioneValues);
			sslTsLocation.addDependencyField(tipoAutenticazione);
			sslTsLocation.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslTsLocationId, sslTsLocation);

			// sslTsPasswd
			String sslTsPasswdLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsPasswd.label");
			URI refreshSslTsPasswdUri = this.getUriField(uriInfo, bd, sslTsPasswdId);
			SslTsPasswd sslTsPasswd = new SslTsPasswd(this.nomeConnettore,this.nomeServizio,sslTsPasswdId, sslTsPasswdLabel, 1,255, refreshSslTsPasswdUri, tipoAutenticazioneValues);
			sslTsPasswd.addDependencyField(tipoAutenticazione);
			sslTsPasswd.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslTsPasswdId, sslTsPasswd);

			// sslType
			String sslTypeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslType.label");
			URI refreshSslTypeUri = this.getUriField(uriInfo, bd, sslTypeId);
			SslType sslType = new SslType(this.nomeConnettore,this.nomeServizio,sslTypeId, sslTypeLabel, 1,255, refreshSslTypeUri,  tipoAutenticazioneValues);
			sslType.addDependencyField(tipoAutenticazione);
			sslType.init(tipoAutenticazioneValues);
			mappaCreazione.put(sslTypeId, sslType);

			infoCreazioneMap.put(this.nomeServizio + "." + this.nomeConnettore, mappaCreazione);
			
		}
		
		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();
		
		// lista field
		
		InputText url = (InputText) mappaCreazione.get(urlId);
		url.setDefaultValue(null);
		listaParametri.add( url);

		CheckButton azioneInURL = (CheckButton) mappaCreazione.get(azioneInURLId);
		azioneInURL.setDefaultValue(false);
		listaParametri.add(azioneInURL);

		SelectList<String> tipoAutenticazione = (SelectList<String>) mappaCreazione.get(tipoAutenticazioneId);
		tipoAutenticazione.setDefaultValue(TIPO_AUTENTICAZIONE_VALUE_NONE); 
		listaParametri.add(tipoAutenticazione);

		Username username = (Username) mappaCreazione.get(usernameId);
		username.setDefaultValue(null);
		listaParametri.add(username);

		Password password = (Password) mappaCreazione.get(passwordId);
		password.setDefaultValue(null);
		listaParametri.add(password);

		TipoSsl tipoSsl = (TipoSsl) mappaCreazione.get(tipoSslId);
		tipoSsl.setDefaultValue(TipoSsl.TIPO_SSL_VALUE_CLIENT);
		listaParametri.add(tipoSsl);

		SslKsType sslKsType = (SslKsType) mappaCreazione.get(sslKsTypeId);
		sslKsType.setDefaultValue(null);
		listaParametri.add(sslKsType);

		SslKsLocation sslKsLocation = (SslKsLocation) mappaCreazione.get(sslKsLocationId);
		sslKsLocation.setDefaultValue(null);
		listaParametri.add(sslKsLocation);

		SslKsPasswd sslKsPasswd = (SslKsPasswd) mappaCreazione.get(sslKsPasswdId);
		sslKsPasswd.setDefaultValue(null);
		listaParametri.add(sslKsPasswd);

		SslPKeyPasswd sslPKeyPasswd = (SslPKeyPasswd) mappaCreazione.get(sslPKeyPasswdId);
		sslPKeyPasswd.setDefaultValue(null);
		listaParametri.add(sslPKeyPasswd);

		SslTsType sslTsType =  (SslTsType) mappaCreazione.get(sslTsTypeId);
		sslTsType.setDefaultValue(null);
		listaParametri.add(sslTsType);

		SslTsLocation sslTsLocation = (SslTsLocation) mappaCreazione.get(sslTsLocationId);
		sslTsLocation.setDefaultValue(null);
		listaParametri.add(sslTsLocation);

		SslTsPasswd sslTsPasswd = (SslTsPasswd) mappaCreazione.get(sslTsPasswdId);
		sslTsPasswd.setDefaultValue(null);
		listaParametri.add(sslTsPasswd);

		SslType sslType =  (SslType) mappaCreazione.get(sslTypeId);
		sslType.setDefaultValue(null);
		listaParametri.add(sslType);
	 

		return listaParametri;
	}

	@SuppressWarnings("unchecked")
	public List<ParamField<?>> getInfoModifica(UriInfo uriInfo, BasicBD bd, Connettore connettore) throws ConsoleException {

		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();

		String urlId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".url.id");
		String tipoAutenticazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.id");
		String usernameId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".username.id");
		String passwordId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".password.id");
		String azioneInURLId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".azioneInURL.id");

		String tipoSslId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.id");
		String sslKsTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsType.id");
		String sslKsLocationId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsLocation.id");
		String sslKsPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsPasswd.id");
		String sslPKeyPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslPKeyPasswd.id");
		String sslTsTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsType.id");
		String sslTsLocationId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsLocation.id");
		String sslTsPasswdId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsPasswd.id");
		String sslTypeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslType.id");

		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeConnettore);
		
		if(mappaCreazione == null)
			this.getInfoCreazione(uriInfo, bd);

		// prelevo i componenti e gli setto i valori correnti
		InputText url = (InputText) mappaCreazione.get(urlId);
		url.setDefaultValue(connettore.getUrl());
		listaParametri.add( url);

		CheckButton azioneInURL = (CheckButton) mappaCreazione.get(azioneInURLId);
		azioneInURL.setDefaultValue(connettore.isAzioneInUrl());
		listaParametri.add(azioneInURL);

		SelectList<String> tipoAutenticazione = (SelectList<String>) mappaCreazione.get(tipoAutenticazioneId);
		EnumAuthType tipoAutenticazioneVal = connettore.getTipoAutenticazione() != null ? connettore.getTipoAutenticazione() : EnumAuthType.NONE;
		switch(tipoAutenticazioneVal){
		case SSL: tipoAutenticazione.setDefaultValue(TIPO_AUTENTICAZIONE_VALUE_SSL); break;
		case HTTPBasic: tipoAutenticazione.setDefaultValue(TIPO_AUTENTICAZIONE_VALUE_HTTP_BASIC); break;
		case NONE: default: tipoAutenticazione.setDefaultValue(TIPO_AUTENTICAZIONE_VALUE_NONE); break;
		}
		listaParametri.add(tipoAutenticazione);

		Username username = (Username) mappaCreazione.get(usernameId);
		username.setDefaultValue(connettore.getHttpUser());
		listaParametri.add(username);

		Password password = (Password) mappaCreazione.get(passwordId);
		password.setDefaultValue(connettore.getHttpPassw());
		listaParametri.add(password);

		TipoSsl tipoSsl = (TipoSsl) mappaCreazione.get(tipoSslId);
		EnumSslType tipoSsl2 = connettore.getTipoSsl() != null ? connettore.getTipoSsl() : EnumSslType.CLIENT;
		switch (tipoSsl2) {
		case SERVER:
			tipoSsl.setDefaultValue(TipoSsl.TIPO_SSL_VALUE_SERVER);
			break;
		case CLIENT:
		default:
			tipoSsl.setDefaultValue(TipoSsl.TIPO_SSL_VALUE_CLIENT);
			break;
		}		

		listaParametri.add(tipoSsl);

		SslKsType sslKsType = (SslKsType) mappaCreazione.get(sslKsTypeId);
		sslKsType.setDefaultValue(connettore.getSslKsType());
		listaParametri.add(sslKsType);

		SslKsLocation sslKsLocation = (SslKsLocation) mappaCreazione.get(sslKsLocationId);
		sslKsLocation.setDefaultValue(connettore.getSslKsLocation());
		listaParametri.add(sslKsLocation);

		SslKsPasswd sslKsPasswd = (SslKsPasswd) mappaCreazione.get(sslKsPasswdId);
		sslKsPasswd.setDefaultValue(connettore.getSslKsPasswd());
		listaParametri.add(sslKsPasswd);

		SslPKeyPasswd sslPKeyPasswd = (SslPKeyPasswd) mappaCreazione.get(sslPKeyPasswdId);
		sslPKeyPasswd.setDefaultValue(connettore.getSslPKeyPasswd());
		listaParametri.add(sslPKeyPasswd);

		SslTsType sslTsType =  (SslTsType) mappaCreazione.get(sslTsTypeId);
		sslTsType.setDefaultValue(connettore.getSslTsType());
		listaParametri.add(sslTsType);

		SslTsLocation sslTsLocation = (SslTsLocation) mappaCreazione.get(sslTsLocationId);
		sslTsLocation.setDefaultValue(connettore.getSslTsLocation());
		listaParametri.add(sslTsLocation);

		SslTsPasswd sslTsPasswd = (SslTsPasswd) mappaCreazione.get(sslTsPasswdId);
		sslTsPasswd.setDefaultValue(connettore.getSslTsPasswd());
		listaParametri.add(sslTsPasswd);

		SslType sslType =  (SslType) mappaCreazione.get(sslTypeId);
		sslType.setDefaultValue(connettore.getSslType());
		listaParametri.add(sslType);

		return listaParametri;
	}

	public URI getUriField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException {
		try{
			URI uri = uriInfo.getBaseUriBuilder().path(this.pathServizio).path(BaseDarsService.PATH_FIELD).path(fieldName).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}

	public void fillSezione(it.govpay.web.rs.dars.model.Sezione sezioneConnettore, Connettore connettore) {

		List<Voce<String>> listaVoci = new ArrayList<Voce<String>>();
		
		sezioneConnettore.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".url.label"), connettore.getUrl());
		sezioneConnettore.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".azioneInURL.label"), 
				Utils.getSiNoAsLabel(connettore.isAzioneInUrl()),true);

		String tipoAutenticazione = null;
		EnumAuthType tipoAutenticazioneVal = connettore.getTipoAutenticazione() != null ? connettore.getTipoAutenticazione() : EnumAuthType.NONE;
		switch(tipoAutenticazioneVal){
		case SSL: 
			tipoAutenticazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.ssl"); 
			
			EnumSslType tipoSsl2 = connettore.getTipoSsl() != null ? connettore.getTipoSsl() : EnumSslType.CLIENT;
			String tipoSsl = null;
			switch (tipoSsl2) {
			case SERVER:
				tipoSsl = TipoSsl.TIPO_SSL_VALUE_SERVER;
				break;
			case CLIENT:
			default:
				tipoSsl = TipoSsl.TIPO_SSL_VALUE_CLIENT;
				break;
			}	

			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoSsl.label"), tipoSsl));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsType.label"), connettore.getSslKsType()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsLocation.label"), connettore.getSslKsLocation()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslKsPasswd.label"), connettore.getSslKsPasswd()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslPKeyPasswd.label"), connettore.getSslPKeyPasswd()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsType.label"), connettore.getSslTsType()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsLocation.label"), connettore.getSslTsLocation()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslTsPasswd.label"), connettore.getSslTsPasswd()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".sslType.label"), connettore.getSslType()));
			
		
		
		break;
		case HTTPBasic: 
			tipoAutenticazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.basic"); 
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".username.label"), connettore.getHttpUser()));
			listaVoci.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".password.label"), connettore.getHttpPassw()));
			break;
		case NONE: default: tipoAutenticazione = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.nessuna"); break;
		}

		sezioneConnettore.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeConnettore + ".tipoAutenticazione.label"), tipoAutenticazione);

		if(listaVoci.size()>0){
			sezioneConnettore.getVoci().addAll(listaVoci);
		}
	}

	public void valida(Connettore connettore) throws ValidationException {
		if(connettore == null)  throw new ValidationException(this.nomeConnettore + " nullo");
		if(connettore.getUrl() == null)  throw new ValidationException("URL "+this.nomeConnettore+" nullo");

		try {
			new URL(connettore.getUrl());
		} catch (MalformedURLException e) {
			throw new ValidationException("URL "+this.nomeConnettore+" non valida");
		}

		if(connettore.getTipoAutenticazione() == null)  throw new ValidationException("Tipo Autenticazione "+this.nomeConnettore+" non puo' essere nullo");

		switch(connettore.getTipoAutenticazione()) {
		case HTTPBasic:
			if(connettore.getHttpUser() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere HttpUser nullo");
			if(connettore.getHttpPassw() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere HttpPassw nullo");
			break;
		case NONE:
			break;
		case SSL:
			if(connettore.getSslKsType() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslKsType nullo");
			if(connettore.getSslKsLocation() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslKsLocation nullo");
			if(connettore.getSslKsPasswd() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslKsPasswd nullo");
			if(connettore.getSslTsType() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslTsType nullo");
			if(connettore.getSslTsLocation() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslTsLocation nullo");
			if(connettore.getSslTsPasswd() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslTsPasswd nullo");
			if(connettore.getSslPKeyPasswd() == null)  throw new ValidationException(this.nomeConnettore + " con TipoAutorizzazione ["+connettore.getTipoAutenticazione()+"] non deve avere SslPKeyPasswd nullo");
			break;
		default:throw new ValidationException("TipoAutenticazione "+this.nomeConnettore+" ["+connettore.getTipoAutenticazione()+"] non valido");

		}
	}
}
