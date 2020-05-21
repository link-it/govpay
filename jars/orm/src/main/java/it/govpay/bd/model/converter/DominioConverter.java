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

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdStazione;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class DominioConverter {

	public static List<Dominio> toDTOList(List<it.govpay.orm.Dominio> anagraficaLst, BasicBD bd) throws ServiceException {
		List<Dominio> lstDTO = new ArrayList<>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Dominio anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica, bd));
			}
		}
		return lstDTO;
	}

	public static Dominio toDTO(it.govpay.orm.Dominio vo, BasicBD bd) throws ServiceException {
		Dominio dto = new Dominio(bd, vo.getId(), vo.getIdStazione().getId());
		if(vo.getIdApplicazioneDefault() != null) {
			dto.setIdApplicazioneDefault(vo.getIdApplicazioneDefault().getId());
		}
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setAbilitato(vo.isAbilitato());
		dto.setAuxDigit(vo.getAuxDigit());
		dto.setIuvPrefix(vo.getIuvPrefix());
		dto.setSegregationCode(vo.getSegregationCode());
		dto.setLogo(vo.getLogo());
		dto.setCbill(vo.getCbill());
		dto.setAutStampaPoste(vo.getAutStampaPoste());
		return dto;
	}

	public static it.govpay.orm.Dominio toVO(Dominio dto) throws ServiceException {
		it.govpay.orm.Dominio vo = new it.govpay.orm.Dominio();
		vo.setId(dto.getId());
		if(dto.getIdApplicazioneDefault() != null) {
			IdApplicazione idApplicazioneDefault = new IdApplicazione();
			idApplicazioneDefault.setId(dto.getIdApplicazioneDefault());
			vo.setIdApplicazioneDefault(idApplicazioneDefault);
		}
		vo.setCodDominio(dto.getCodDominio());
		vo.setRagioneSociale(dto.getRagioneSociale()); 
		vo.setGln(dto.getGln());
		vo.setAbilitato(dto.isAbilitato());
		vo.setAuxDigit(dto.getAuxDigit());
		vo.setIuvPrefix(dto.getIuvPrefix());
		IdStazione idStazione = new IdStazione();
		idStazione.setId(dto.getIdStazione());
		vo.setIdStazione(idStazione);
		vo.setSegregationCode(dto.getSegregationCode());
		vo.setLogo(dto.getLogo());
		vo.setCbill(dto.getCbill());
		vo.setAutStampaPoste(dto.getAutStampaPoste());
		return vo;
	}

}
