/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Connettore;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Versionabile.Versione;

public class ConnettoreNotificaPagamentiConverter {

	public static ConnettoreNotificaPagamenti toConnettoreNotificaPagamentiDTO(List<it.govpay.orm.Connettore> connettoreLst) throws ServiceException {
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
						dto.setTipiPendenza(new ArrayList<String>());
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
						dto.setContenuti(new ArrayList<String>());
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
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_COMPANY.equals(connettore.getCodProprieta())) {
					dto.setNetPayCompany(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_PASSWORD.equals(connettore.getCodProprieta())) {
					dto.setNetPayPassword(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_PRINCIPAL.equals(connettore.getCodProprieta())) {
					dto.setNetPayPrincipal(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_RUOLO.equals(connettore.getCodProprieta())) {
					dto.setNetPayRuolo(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_URL.equals(connettore.getCodProprieta())) {
					dto.setNetPayURL(connettore.getValore());
				}
				
				if(ConnettoreNotificaPagamenti.P_NETPAY_USERNAME.equals(connettore.getCodProprieta())) {
					dto.setNetPayUsername(connettore.getValore());
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
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_CODICE_IPA, connettore.getCodiceIPA()));
		}

		if(connettore.getEmailIndirizzi() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_EMAIL_INDIRIZZO, !connettore.getEmailIndirizzi().isEmpty() ? StringUtils.join(connettore.getEmailIndirizzi(), ","): ""));
		}
		
		if(connettore.getEmailSubject() != null && !connettore.getEmailSubject().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_EMAIL_SUBJECT, connettore.getEmailSubject()));
		}
		
		if(connettore.getCodiceCliente() != null && !connettore.getCodiceCliente().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_CODICE_CLIENTE, connettore.getCodiceCliente()));
		}
		
		if(connettore.getCodiceIstituto() != null && !connettore.getCodiceIstituto().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_CODICE_ISTITUTO, connettore.getCodiceIstituto()));
		}
		
		if(connettore.getFileSystemPath() != null && !connettore.getFileSystemPath().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_FILE_SYSTEM_PATH, connettore.getFileSystemPath()));
		}

		if(connettore.getTipiPendenza() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_TIPI_PENDENZA, !connettore.getTipiPendenza().isEmpty() ? StringUtils.join(connettore.getTipiPendenza(), ","): ""));
		}

		if(connettore.getTipoConnettore() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_TIPO_CONNETTORE, connettore.getTipoConnettore().toString()));
		}

		if(connettore.getTipoTracciato() != null && !connettore.getTipoTracciato().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_TIPO_TRACCIATO, connettore.getTipoTracciato()));
		}

		if(connettore.getVersioneCsv() != null && !connettore.getVersioneCsv().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_VERSIONE_CSV, connettore.getVersioneCsv()));
		}
		
		if(connettore.getContenuti() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_CONTENUTI, !connettore.getContenuti().isEmpty() ? StringUtils.join(connettore.getContenuti(), ","): ""));
		}
		
		if(connettore.getDownloadBaseURL() != null && !connettore.getDownloadBaseURL().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_DOWNLOAD_BASE_URL, connettore.getDownloadBaseURL()));
		}
		
		if(connettore.getIntervalloCreazioneTracciato() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_INTERVALLO_CREAZIONE_TRACCIATO, "" + connettore.getIntervalloCreazioneTracciato()));
		}

		if(connettore.getHttpUser() != null && !connettore.getHttpUser().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_HTTPUSER_NAME, connettore.getHttpUser()));
		}

		if(connettore.getHttpPassw() != null && !connettore.getHttpPassw().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_HTTPPASSW_NAME, connettore.getHttpPassw()));
		}

		if(connettore.getUrl() != null && !connettore.getUrl().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_URL_NAME, connettore.getUrl()));
		}
		
		if(connettore.getTipoAutenticazione() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_TIPOAUTENTICAZIONE_NAME, connettore.getTipoAutenticazione().toString()));
		}

		if(connettore.getTipoSsl() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_TIPOSSL_NAME, connettore.getTipoSsl().toString()));
		}

		if(connettore.getSslKsLocation() != null && !connettore.getSslKsLocation().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLKSLOCATION_NAME, connettore.getSslKsLocation()));
		}

		if(connettore.getSslKsPasswd() != null && !connettore.getSslKsPasswd().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLKSPASS_WORD_NAME, connettore.getSslKsPasswd()));
		}

		if(connettore.getSslKsType() != null && !connettore.getSslKsType().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLKSTYPE_NAME, connettore.getSslKsType()));
		}

		if(connettore.getSslTsLocation() != null && !connettore.getSslTsLocation().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLTSLOCATION_NAME, connettore.getSslTsLocation()));
		}

		if(connettore.getSslTsPasswd() != null && !connettore.getSslTsPasswd().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLTSPASS_WORD_NAME, connettore.getSslTsPasswd()));
		}

		if(connettore.getSslTsType() != null && !connettore.getSslTsType().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLTSTYPE_NAME, connettore.getSslTsType()));
		}

		if(connettore.getSslPKeyPasswd() != null && !connettore.getSslPKeyPasswd().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLPKEYPASS_WORD_NAME, connettore.getSslPKeyPasswd()));
		}

		if(connettore.getSslType() != null && !connettore.getSslType().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_SSLTYPE_NAME, connettore.getSslType()));
		}
		
		if(connettore.getVersione() != null) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_VERSIONE, connettore.getVersione().getApiLabel()));
		}
		
		if(connettore.getPrincipalMaggioli() != null && !connettore.getPrincipalMaggioli().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_PRINCIPAL_MAGGIOLI, connettore.getPrincipalMaggioli()));
		}
		
		voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_AZIONEINURL_NAME, Boolean.toString(connettore.isAzioneInUrl())));
		
		voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_ABILITATO, Boolean.toString(connettore.isAbilitato())));
		
		voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_EMAIL_ALLEGATO, Boolean.toString(connettore.isEmailAllegato())));
		
		if(connettore.getNetPayCompany() != null && !connettore.getNetPayCompany().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_COMPANY, connettore.getNetPayCompany()));
		}
		
		if(connettore.getNetPayPassword() != null && !connettore.getNetPayPassword().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_PASSWORD, connettore.getNetPayPassword()));
		}
		
		if(connettore.getNetPayPrincipal() != null && !connettore.getNetPayPrincipal().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_PRINCIPAL, connettore.getNetPayPrincipal()));
		}
		
		if(connettore.getNetPayRuolo() != null && !connettore.getNetPayRuolo().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_RUOLO, connettore.getNetPayRuolo()));
		}
		
		if(connettore.getNetPayURL() != null && !connettore.getNetPayURL().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_URL, connettore.getNetPayURL()));
		}
		
		if(connettore.getNetPayUsername() != null && !connettore.getNetPayUsername().trim().isEmpty()) {
			voList.add(getConnettoreVO(connettore.getIdConnettore(), ConnettoreNotificaPagamenti.P_NETPAY_USERNAME, connettore.getNetPayUsername()));
		}
		
		return voList;
	}

	private static it.govpay.orm.Connettore getConnettoreVO(String codConnettore, String codProprieta, String valore) {
		it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
		vo.setCodConnettore(codConnettore);
		vo.setCodProprieta(codProprieta);
		vo.setValore(valore);
		return vo;
	}
}
