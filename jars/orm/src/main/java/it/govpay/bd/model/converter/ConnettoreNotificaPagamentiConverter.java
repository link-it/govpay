/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.govpay.model.Connettore;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Versionabile.Versione;
import it.govpay.model.exception.CodificaInesistenteException;

public class ConnettoreNotificaPagamentiConverter {
	
	private ConnettoreNotificaPagamentiConverter() {}

	public static ConnettoreNotificaPagamenti toConnettoreNotificaPagamentiDTO(List<it.govpay.orm.Connettore> connettoreLst) throws CodificaInesistenteException {
		ConnettoreNotificaPagamenti dto = new ConnettoreNotificaPagamenti();
		if(connettoreLst != null && !connettoreLst.isEmpty()) {
			for(it.govpay.orm.Connettore connettore: connettoreLst){

				dto.setIdConnettore(connettore.getCodConnettore());
				if(ConnettoreNotificaPagamenti.P_ABILITATO.equals(connettore.getCodProprieta())) {
					dto.setAbilitato(Boolean.parseBoolean(connettore.getValore()));
				}

				if(ConnettoreNotificaPagamenti.P_CODICE_IPA.equals(connettore.getCodProprieta())) {
					dto.setCodiceIPA(connettore.getValore());
				}

				if(ConnettoreNotificaPagamenti.P_EMAIL_INDIRIZZO.equals(connettore.getCodProprieta())) {
					String [] values = connettore.getValore().split(",");
					if(values != null && values.length > 0) {
						dto.setEmailIndirizzi(Arrays.asList(values));
					}
				}
				
				if(ConnettoreNotificaPagamenti.P_EMAIL_SUBJECT.equals(connettore.getCodProprieta())) {
					dto.setEmailSubject(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_FILE_SYSTEM_PATH.equals(connettore.getCodProprieta())) {
					dto.setFileSystemPath(connettore.getValore());
				}

				if(ConnettoreNotificaPagamenti.P_TIPI_PENDENZA.equals(connettore.getCodProprieta())) {
					if(!connettore.getValore().equals("")) {
						String [] values = connettore.getValore().split(",");
						if(values != null && values.length > 0) {
							dto.setTipiPendenza(Arrays.asList(values));
						}
					} else {
						dto.setTipiPendenza(new ArrayList<>());
					}
				}

				if(ConnettoreNotificaPagamenti.P_TIPO_CONNETTORE.equals(connettore.getCodProprieta())) {
					dto.setTipoConnettore(ConnettoreNotificaPagamenti.TipoConnettore.valueOf(connettore.getValore()));
				}

				if(ConnettoreNotificaPagamenti.P_TIPO_TRACCIATO.equals(connettore.getCodProprieta())) {
					dto.setTipoTracciato(connettore.getValore());
				}

				if(ConnettoreNotificaPagamenti.P_VERSIONE_CSV.equals(connettore.getCodProprieta())) {
					dto.setVersioneCsv(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_CODICE_CLIENTE.equals(connettore.getCodProprieta())) {
					dto.setCodiceCliente(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_CODICE_ISTITUTO.equals(connettore.getCodProprieta())) {
					dto.setCodiceIstituto(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_CONTENUTI.equals(connettore.getCodProprieta())) {
					if(!connettore.getValore().equals("")) {
						String [] values = connettore.getValore().split(",");
						if(values != null && values.length > 0) {
							dto.setContenuti(Arrays.asList(values));
						}
					} else {
						dto.setContenuti(new ArrayList<>());
					}
				}
				
				if(ConnettoreNotificaPagamenti.P_EMAIL_ALLEGATO.equals(connettore.getCodProprieta())) {
					dto.setEmailAllegato(Boolean.parseBoolean(connettore.getValore()));
				}
				
				if(ConnettoreNotificaPagamenti.P_DOWNLOAD_BASE_URL.equals(connettore.getCodProprieta())) {
					dto.setDownloadBaseURL(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_PRINCIPAL_MAGGIOLI.equals(connettore.getCodProprieta())) {
					dto.setPrincipalMaggioli(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_INTERVALLO_CREAZIONE_TRACCIATO.equals(connettore.getCodProprieta())) {
					dto.setIntervalloCreazioneTracciato(Integer.parseInt(connettore.getValore()));
				}


				// ereditato da connettore
				
				if(Connettore.P_HTTPUSER_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpUser(connettore.getValore());
				}

				if(Connettore.P_HTTPPASSW_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpPassw(connettore.getValore());
				}

				if(Connettore.P_URL_NAME.equals(connettore.getCodProprieta())) {
					dto.setUrl(connettore.getValore());
				}
				
				if(Connettore.P_TIPOAUTENTICAZIONE_NAME.equals(connettore.getCodProprieta())) {
					dto.setTipoAutenticazione(Connettore.EnumAuthType.valueOf(connettore.getValore()));
				}

				if(Connettore.P_TIPOSSL_NAME.equals(connettore.getCodProprieta())) {
					dto.setTipoSsl(Connettore.EnumSslType.valueOf(connettore.getValore()));
				}

				if(Connettore.P_SSLKSLOCATION_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsLocation(connettore.getValore());
				}

				if(Connettore.P_SSLKSPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLKSTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsType(connettore.getValore());
				}

				if(Connettore.P_SSLTSLOCATION_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsLocation(connettore.getValore());
				}

				if(Connettore.P_SSLTSPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLTSTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsType(connettore.getValore());
				}

				if(Connettore.P_SSLPKEYPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslPKeyPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslType(connettore.getValore());
				}
				
				if(Connettore.P_HTTP_HEADER_AUTH_HEADER_NAME_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpHeaderName(connettore.getValore());
				}
				
				if(Connettore.P_HTTP_HEADER_AUTH_HEADER_VALUE_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpHeaderValue(connettore.getValore());
				}
				
				if(Connettore.P_API_KEY_AUTH_API_ID_NAME.equals(connettore.getCodProprieta())) {
					dto.setApiId(connettore.getValore());
				}
				
				if(Connettore.P_API_KEY_AUTH_API_KEY_NAME.equals(connettore.getCodProprieta())) {
					dto.setApiKey(connettore.getValore());
				}
				
				if(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID_NAME.equals(connettore.getCodProprieta())) {
					dto.setOauth2ClientCredentialsClientId(connettore.getValore());
				}
				
				if(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET_NAME.equals(connettore.getCodProprieta())) {
					dto.setOauth2ClientCredentialsClientSecret(connettore.getValore());
				}
				
				if(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_SCOPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setOauth2ClientCredentialsScope(connettore.getValore());
				}
				
				if(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT_NAME.equals(connettore.getCodProprieta())) {
					dto.setOauth2ClientCredentialsUrlTokenEndpoint(connettore.getValore());
				}

				if(Connettore.P_AZIONEINURL_NAME.equals(connettore.getCodProprieta())) {
					dto.setAzioneInUrl(Boolean.parseBoolean(connettore.getValore()));
				}
				
				if(Connettore.P_VERSIONE.equals(connettore.getCodProprieta())) {
					dto.setVersione(Versione.toEnum(connettore.getValore()));
				}
			}
		}
		return dto;
	}

	public static List<it.govpay.orm.Connettore> toConnettoreNotificaPagamentiVOList(ConnettoreNotificaPagamenti connettore) {
		List<it.govpay.orm.Connettore> voList = new ArrayList<>();
		
		if(connettore.getCodiceIPA() != null && !connettore.getCodiceIPA().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_CODICE_IPA);
			vo.setValore(connettore.getCodiceIPA());
			voList.add(vo);
		}

		if(connettore.getEmailIndirizzi() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_EMAIL_INDIRIZZO);
			vo.setValore(!connettore.getEmailIndirizzi().isEmpty() ? StringUtils.join(connettore.getEmailIndirizzi(), ","): "");
			voList.add(vo);
		}
		
		if(connettore.getEmailSubject() != null && !connettore.getEmailSubject().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_EMAIL_SUBJECT);
			vo.setValore(connettore.getEmailSubject());
			voList.add(vo);
		}
		
		if(connettore.getCodiceCliente() != null && !connettore.getCodiceCliente().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_CODICE_CLIENTE);
			vo.setValore(connettore.getCodiceCliente());
			voList.add(vo);
		}
		
		if(connettore.getCodiceIstituto() != null && !connettore.getCodiceIstituto().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_CODICE_ISTITUTO);
			vo.setValore(connettore.getCodiceIstituto());
			voList.add(vo);
		}
		
		if(connettore.getFileSystemPath() != null && !connettore.getFileSystemPath().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_FILE_SYSTEM_PATH);
			vo.setValore(connettore.getFileSystemPath());
			voList.add(vo);
		}

		if(connettore.getTipiPendenza() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_TIPI_PENDENZA);
			vo.setValore(!connettore.getTipiPendenza().isEmpty() ? StringUtils.join(connettore.getTipiPendenza(), ","): "");
			voList.add(vo);
		}

		if(connettore.getTipoConnettore() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_TIPO_CONNETTORE);
			vo.setValore(connettore.getTipoConnettore().toString());
			voList.add(vo);
		}

		if(connettore.getTipoTracciato() != null && !connettore.getTipoTracciato().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_TIPO_TRACCIATO);
			vo.setValore(connettore.getTipoTracciato());
			voList.add(vo);
		}

		if(connettore.getVersioneCsv() != null && !connettore.getVersioneCsv().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_VERSIONE_CSV);
			vo.setValore(connettore.getVersioneCsv());
			voList.add(vo);
		}
		
		if(connettore.getContenuti() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_CONTENUTI);
			vo.setValore(!connettore.getContenuti().isEmpty() ? StringUtils.join(connettore.getContenuti(), ","): "");
			voList.add(vo);
		}
		
		if(connettore.getDownloadBaseURL() != null && !connettore.getDownloadBaseURL().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_DOWNLOAD_BASE_URL);
			vo.setValore(connettore.getDownloadBaseURL());
			voList.add(vo);
		}
		
		if(connettore.getIntervalloCreazioneTracciato() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_INTERVALLO_CREAZIONE_TRACCIATO);
			vo.setValore("" + connettore.getIntervalloCreazioneTracciato());
			voList.add(vo);
		}

		if(connettore.getHttpUser() != null && !connettore.getHttpUser().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTPUSER_NAME);
			vo.setValore(connettore.getHttpUser());
			voList.add(vo);
		}

		if(connettore.getHttpPassw() != null && !connettore.getHttpPassw().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTPPASSW_NAME);
			vo.setValore(connettore.getHttpPassw());
			voList.add(vo);
		}

		if(connettore.getUrl() != null && !connettore.getUrl().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_URL_NAME);
			vo.setValore(connettore.getUrl());
			voList.add(vo);
		}
		
		if(connettore.getTipoAutenticazione() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_TIPOAUTENTICAZIONE_NAME);
			vo.setValore(connettore.getTipoAutenticazione().toString());
			voList.add(vo);
		}

		if(connettore.getTipoSsl() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_TIPOSSL_NAME);
			vo.setValore(connettore.getTipoSsl().toString());
			voList.add(vo);
		}

		if(connettore.getSslKsLocation() != null && !connettore.getSslKsLocation().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSLOCATION_NAME);
			vo.setValore(connettore.getSslKsLocation());
			voList.add(vo);
		}

		if(connettore.getSslKsPasswd() != null && !connettore.getSslKsPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSPASS_WORD_NAME);
			vo.setValore(connettore.getSslKsPasswd());
			voList.add(vo);
		}

		if(connettore.getSslKsType() != null && !connettore.getSslKsType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSTYPE_NAME);
			vo.setValore(connettore.getSslKsType());
			voList.add(vo);
		}

		if(connettore.getSslTsLocation() != null && !connettore.getSslTsLocation().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSLOCATION_NAME);
			vo.setValore(connettore.getSslTsLocation());
			voList.add(vo);
		}

		if(connettore.getSslTsPasswd() != null && !connettore.getSslTsPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSPASS_WORD_NAME);
			vo.setValore(connettore.getSslTsPasswd());
			voList.add(vo);
		}

		if(connettore.getSslTsType() != null && !connettore.getSslTsType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSTYPE_NAME);
			vo.setValore(connettore.getSslTsType());
			voList.add(vo);
		}

		if(connettore.getSslPKeyPasswd() != null && !connettore.getSslPKeyPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLPKEYPASS_WORD_NAME);
			vo.setValore(connettore.getSslPKeyPasswd());
			voList.add(vo);
		}

		if(connettore.getSslType() != null && !connettore.getSslType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTYPE_NAME);
			vo.setValore(connettore.getSslType());
			voList.add(vo);
		}
		
		if(connettore.getVersione() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_VERSIONE);
			vo.setValore(connettore.getVersione().getApiLabel());
			voList.add(vo);
		}
		
		if(connettore.getPrincipalMaggioli() != null && !connettore.getPrincipalMaggioli().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreNotificaPagamenti.P_PRINCIPAL_MAGGIOLI);
			vo.setValore(connettore.getPrincipalMaggioli());
			voList.add(vo);
		}
		
		if(connettore.getHttpHeaderName() != null && !connettore.getHttpHeaderName().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTP_HEADER_AUTH_HEADER_NAME_NAME);
			vo.setValore(connettore.getHttpHeaderName());
			voList.add(vo);
		}
		
		if(connettore.getHttpHeaderValue() != null && !connettore.getHttpHeaderValue().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTP_HEADER_AUTH_HEADER_VALUE_NAME);
			vo.setValore(connettore.getHttpHeaderValue());
			voList.add(vo);
		}
		
		if(connettore.getApiId() != null && !connettore.getApiId().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_API_KEY_AUTH_API_ID_NAME);
			vo.setValore(connettore.getApiId());
			voList.add(vo);
		}
		
		if(connettore.getApiKey() != null && !connettore.getApiKey().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_API_KEY_AUTH_API_KEY_NAME);
			vo.setValore(connettore.getApiKey());
			voList.add(vo);
		}
		
		if(connettore.getOauth2ClientCredentialsClientId() != null && !connettore.getOauth2ClientCredentialsClientId().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_CLIENT_ID_NAME);
			vo.setValore(connettore.getOauth2ClientCredentialsClientId());
			voList.add(vo);
		}
		
		if(connettore.getOauth2ClientCredentialsClientSecret() != null && !connettore.getOauth2ClientCredentialsClientSecret().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_CLIENT_SECRET_NAME);
			vo.setValore(connettore.getOauth2ClientCredentialsClientSecret());
			voList.add(vo);
		}
		
		if(connettore.getOauth2ClientCredentialsScope() != null && !connettore.getOauth2ClientCredentialsScope().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_SCOPE_NAME);
			vo.setValore(connettore.getOauth2ClientCredentialsScope());
			voList.add(vo);
		}
		
		if(connettore.getOauth2ClientCredentialsUrlTokenEndpoint() != null && !connettore.getOauth2ClientCredentialsUrlTokenEndpoint().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_OAUTH2_CLIENT_CREDENTIALS_URL_TOKEN_ENDPOINT_NAME);
			vo.setValore(connettore.getOauth2ClientCredentialsUrlTokenEndpoint());
			voList.add(vo);
		}
		
		it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
		vo.setCodConnettore(connettore.getIdConnettore());
		vo.setCodProprieta(Connettore.P_AZIONEINURL_NAME);
		vo.setValore(Boolean.toString(connettore.isAzioneInUrl()));
		voList.add(vo);
		
		it.govpay.orm.Connettore voAbilitato = new it.govpay.orm.Connettore();
		voAbilitato.setCodConnettore(connettore.getIdConnettore());
		voAbilitato.setCodProprieta(ConnettoreNotificaPagamenti.P_ABILITATO);
		voAbilitato.setValore(Boolean.toString(connettore.isAbilitato()));
		voList.add(voAbilitato);
		
		it.govpay.orm.Connettore voEmailAllegato = new it.govpay.orm.Connettore();
		voEmailAllegato.setCodConnettore(connettore.getIdConnettore());
		voEmailAllegato.setCodProprieta(ConnettoreNotificaPagamenti.P_EMAIL_ALLEGATO);
		voEmailAllegato.setValore(Boolean.toString(connettore.isEmailAllegato()));
		voList.add(voEmailAllegato);
		
		return voList;
	}
	
}
