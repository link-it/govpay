/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.Connector;
import it.govpay.backoffice.v1.beans.Connector.VersioneApiEnum;
import it.govpay.backoffice.v1.beans.TipoAutenticazione;
import it.govpay.backoffice.v1.beans.TipoAutenticazione.TipoEnum;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Versionabile;
import it.govpay.model.exception.CodificaInesistenteException;

public class ConnettoriConverter {

	public static Connettore getConnettore(it.govpay.backoffice.v1.beans.Connector connector) throws CodificaInesistenteException {
		Connettore connettore = new Connettore();

		ConnettoriConverter.setAutenticazione(connettore, connector.getAuth());

		connettore.setUrl(connector.getUrl());
		if(connector.getVersioneApi() != null)
			connettore.setVersione(Versionabile.Versione.toEnum(VersioneApiEnum.fromValue(connector.getVersioneApi()).toNameString()));

		return connettore;
	}

	public static void setAutenticazione(Connettore connettore, TipoAutenticazione auth) {
		if(auth != null) {
			connettore.setHttpUser(auth.getUsername());
			connettore.setHttpPassw(auth.getPassword());
			connettore.setSslKsLocation(auth.getKsLocation());
			connettore.setSslTsLocation(auth.getTsLocation());
			connettore.setSslKsPasswd(auth.getKsPassword());
			connettore.setSslTsPasswd(auth.getTsPassword());
			connettore.setSslTsType(auth.getTsType());
			connettore.setSslType(auth.getSslType());
			connettore.setSslKsType(auth.getKsType());
			connettore.setSslPKeyPasswd(auth.getKsPKeyPasswd());
			connettore.setHttpHeaderName(auth.getHeaderName());
			connettore.setHttpHeaderValue(auth.getHeaderValue());
			connettore.setApiId(auth.getApiId());
			connettore.setApiKey(auth.getApiKey());
			connettore.setOauth2ClientCredentialsClientId(auth.getClientId());
			connettore.setOauth2ClientCredentialsClientSecret(auth.getClientSecret());
			connettore.setOauth2ClientCredentialsScope(auth.getScope());
			connettore.setOauth2ClientCredentialsUrlTokenEndpoint(auth.getUrlTokenEndpoint());

			if(auth.getClientId() != null) {
				connettore.setTipoAutenticazione(EnumAuthType.OAUTH2_CLIENT_CREDENTIALS);
			} else {
				if(auth.getApiId() != null) {
					connettore.setTipoAutenticazione(EnumAuthType.API_KEY);
				} else {
					if(auth.getHeaderName() != null) {
						connettore.setTipoAutenticazione(EnumAuthType.HTTP_HEADER);
					} else {
						if(auth.getTipo() != null) {
							connettore.setTipoAutenticazione(EnumAuthType.SSL);
							if(auth.getTipo() != null) {
								switch (auth.getTipo()) {
								case CLIENT:
									connettore.setTipoSsl(EnumSslType.CLIENT);
									break;
								case SERVER:
								default:
									connettore.setTipoSsl(EnumSslType.SERVER);
									break;
								}
							}
						} else {
							connettore.setTipoAutenticazione(EnumAuthType.HTTPBasic);
						}
					}
				}
			}
		} else {
			connettore.setTipoAutenticazione(EnumAuthType.NONE);
		}
	}

	public static Connector toRsModel(it.govpay.model.Connettore connettore) {
		Connector rsModel = new Connector();
		if(connettore.getTipoAutenticazione()!=null && !connettore.getTipoAutenticazione().equals(EnumAuthType.NONE))
			rsModel.setAuth(toTipoAutenticazioneRsModel(connettore));
		rsModel.setUrl(connettore.getUrl());
		if(connettore.getVersione() != null)
			rsModel.setVersioneApi(VersioneApiEnum.fromName(connettore.getVersione().getApiLabel()).toString());

		return rsModel;
	}

	public static it.govpay.backoffice.v1.beans.TipoAutenticazione toTipoAutenticazioneRsModel(it.govpay.model.Connettore connettore) {
		it.govpay.backoffice.v1.beans.TipoAutenticazione rsModel = new it.govpay.backoffice.v1.beans.TipoAutenticazione();

		rsModel.username(connettore.getHttpUser())
		.password(connettore.getHttpPassw())
		.ksLocation(connettore.getSslKsLocation())
		.ksPassword(connettore.getSslKsPasswd())
		.tsLocation(connettore.getSslTsLocation())
		.tsPassword(connettore.getSslTsPasswd())
		.tsType(connettore.getSslTsType())
		.sslType(connettore.getSslType())
		.ksType(connettore.getSslKsType())
		.ksPKeyPasswd(connettore.getSslPKeyPasswd())
		.headerName(connettore.getHttpHeaderName())
		.headerValue(connettore.getHttpHeaderValue())
		.apiId(connettore.getApiId())
		.apiKey(connettore.getApiKey())
		.clientId(connettore.getOauth2ClientCredentialsClientId())
		.clientSecret(connettore.getOauth2ClientCredentialsClientSecret())
		.scope(connettore.getOauth2ClientCredentialsScope())
		.urlTokenEndpoint(connettore.getOauth2ClientCredentialsUrlTokenEndpoint());

		if(connettore.getTipoSsl() != null) {
			switch (connettore.getTipoSsl() ) {
			case CLIENT:
				rsModel.setTipo(TipoEnum.CLIENT);
				break;
			case SERVER:
			default:
				rsModel.setTipo(TipoEnum.SERVER);
				break;
			}
		}

		return rsModel;
	}
}
