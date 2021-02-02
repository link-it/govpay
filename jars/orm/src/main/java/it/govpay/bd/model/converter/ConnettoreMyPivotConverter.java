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

import it.govpay.model.ConnettoreMyPivot;

public class ConnettoreMyPivotConverter {

	public static ConnettoreMyPivot toDTO(List<it.govpay.orm.Connettore> connettoreLst) throws ServiceException {
		ConnettoreMyPivot dto = new ConnettoreMyPivot();
		if(connettoreLst != null && !connettoreLst.isEmpty()) {
			for(it.govpay.orm.Connettore connettore: connettoreLst){

				dto.setIdConnettore(connettore.getCodConnettore());
				if(ConnettoreMyPivot.P_ABILITATO.equals(connettore.getCodProprieta())) {
					dto.setAbilitato(Boolean.parseBoolean(connettore.getValore()));
				}

				if(ConnettoreMyPivot.P_CODICE_IPA.equals(connettore.getCodProprieta())) {
					dto.setCodiceIPA(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_EMAIL_INDIRIZZO.equals(connettore.getCodProprieta())) {
					dto.setEmailIndirizzo(connettore.getValore());
				}
				
				if(ConnettoreMyPivot.P_FILE_SYSTEM_PATH.equals(connettore.getCodProprieta())) {
					dto.setFileSystemPath(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_TIPI_PENDENZA.equals(connettore.getCodProprieta())) {
					String [] values = connettore.getValore().split(",");
					if(values != null && values.length > 0) {
						dto.setTipiPendenza(Arrays.asList(values));
					}
				}

				if(ConnettoreMyPivot.P_TIPO_CONNETTORE.equals(connettore.getCodProprieta())) {
					dto.setTipoConnettore(ConnettoreMyPivot.TipoConnettore.valueOf(connettore.getValore()));
				}

				if(ConnettoreMyPivot.P_TIPO_TRACCIATO.equals(connettore.getCodProprieta())) {
					dto.setTipoTracciato(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_VERSIONE_CSV.equals(connettore.getCodProprieta())) {
					dto.setVersioneCsv(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_WEB_SERVICE_PASSWORD.equals(connettore.getCodProprieta())) {
					dto.setWebServicePassword(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_WEB_SERVICE_URL.equals(connettore.getCodProprieta())) {
					dto.setWebServiceUrl(connettore.getValore());
				}

				if(ConnettoreMyPivot.P_WEB_SERVICE_USERNAME.equals(connettore.getCodProprieta())) {
					dto.setWebServiceUsername(connettore.getValore());
				}

			}
		}
		return dto;
	}

	public static List<it.govpay.orm.Connettore> toVOList(ConnettoreMyPivot connettore) {
		List<it.govpay.orm.Connettore> voList = new ArrayList<>();
		
		if(connettore.getCodiceIPA() != null && !connettore.getCodiceIPA().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_CODICE_IPA);
			vo.setValore(connettore.getCodiceIPA());
			voList.add(vo);
		}

		if(connettore.getEmailIndirizzo() != null && !connettore.getEmailIndirizzo().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_EMAIL_INDIRIZZO);
			vo.setValore(connettore.getEmailIndirizzo());
			voList.add(vo);
		}
		
		if(connettore.getFileSystemPath() != null && !connettore.getFileSystemPath().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_FILE_SYSTEM_PATH);
			vo.setValore(connettore.getFileSystemPath());
			voList.add(vo);
		}

		if(connettore.getTipiPendenza() != null && !connettore.getTipiPendenza().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_TIPI_PENDENZA);
			vo.setValore(StringUtils.join(connettore.getTipiPendenza(), ","));
			voList.add(vo);
		}

		if(connettore.getTipoConnettore() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_TIPO_CONNETTORE);
			vo.setValore(connettore.getTipoConnettore().toString());
			voList.add(vo);
		}

		if(connettore.getTipoTracciato() != null && !connettore.getTipoTracciato().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_TIPO_TRACCIATO);
			vo.setValore(connettore.getTipoTracciato());
			voList.add(vo);
		}

		if(connettore.getVersioneCsv() != null && !connettore.getVersioneCsv().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_VERSIONE_CSV);
			vo.setValore(connettore.getVersioneCsv());
			voList.add(vo);
		}

		if(connettore.getWebServicePassword() != null && !connettore.getWebServicePassword().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_WEB_SERVICE_PASSWORD);
			vo.setValore(connettore.getWebServicePassword());
			voList.add(vo);
		}

		if(connettore.getWebServiceUrl() != null && !connettore.getWebServiceUrl().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_WEB_SERVICE_URL);
			vo.setValore(connettore.getWebServiceUrl());
			voList.add(vo);
		}

		if(connettore.getWebServiceUsername() != null && !connettore.getWebServiceUsername().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(ConnettoreMyPivot.P_WEB_SERVICE_USERNAME);
			vo.setValore(connettore.getWebServiceUsername());
			voList.add(vo);
		}
		
		it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
		vo.setCodConnettore(connettore.getIdConnettore());
		vo.setCodProprieta(ConnettoreMyPivot.P_ABILITATO);
		vo.setValore(Boolean.toString(connettore.isAbilitato()));
		voList.add(vo);

		return voList;
	}

}
