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
package it.govpay.bd.model.converter;

import it.govpay.bd.model.Dominio;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdStazione;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class DominioConverter {

	public static List<Dominio> toDTOList(List<it.govpay.orm.Dominio> anagraficaLst) throws ServiceException {
		List<Dominio> lstDTO = new ArrayList<Dominio>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Dominio anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Dominio toDTO(it.govpay.orm.Dominio vo) throws ServiceException {
		Dominio dto = new Dominio();
		dto.setId(vo.getId());
		if(vo.getIdApplicazioneDefault() != null) {
			dto.setIdApplicazioneDefault(vo.getIdApplicazioneDefault().getId());
		}
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setAbilitato(vo.isAbilitato());
		dto.setRiusoIuv(vo.getRiusoIUV());
		dto.setCustomIuv(vo.getCustomIUV());
		dto.setIdStazione(vo.getIdStazione().getId());
		dto.setContiAccredito(vo.getXmlContiAccredito());
		dto.setTabellaControparti(vo.getXmlTabellaControparti());
		dto.setAuxDigit(vo.getAuxDigit());
		dto.setIuvPrefix(vo.getIuvPrefix());
		dto.setIuvPrefixStrict(vo.isIuvPrefixStrict());
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
		vo.setRiusoIUV(dto.isRiusoIuv());
		vo.setCustomIUV(dto.isCustomIuv());
		vo.setXmlContiAccredito(dto.getContiAccredito());
		vo.setXmlTabellaControparti(dto.getTabellaControparti());
		vo.setAuxDigit(dto.getAuxDigit());
		vo.setIuvPrefix(dto.getIuvPrefix());
		vo.setIuvPrefixStrict(dto.isIuvPrefixStrict());
		IdStazione idStazione = new IdStazione();
		idStazione.setId(dto.getIdStazione());
		vo.setIdStazione(idStazione);
		return vo;
	}

}
