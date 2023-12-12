/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
import java.util.List;

import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdDominio;

public class IuvConverter {

	public static List<Iuv> toDTOList(List<it.govpay.orm.IUV> applicazioneLst) throws CodificaInesistenteException {
		List<Iuv> lstDTO = new ArrayList<>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.IUV applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Iuv toDTO(it.govpay.orm.IUV vo) throws CodificaInesistenteException {
		Iuv dto = new Iuv();
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setPrg(vo.getPrg());
		dto.setDataGenerazione(vo.getDataGenerazione());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIuv(vo.getIuv());
		dto.setTipo(TipoIUV.toEnum(vo.getTipoIuv()));
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		dto.setApplicationCode(vo.getApplicationCode());
		dto.setAuxDigit(vo.getAuxDigit());
		return dto;
	}

	public static it.govpay.orm.IUV toVO(Iuv dto) {
		it.govpay.orm.IUV vo = new it.govpay.orm.IUV();
		vo.setId(dto.getId());
		vo.setPrg(dto.getPrg());
		vo.setDataGenerazione(dto.getDataGenerazione());
		vo.setIuv(dto.getIuv());
		vo.setTipoIuv(dto.getTipo().getCodifica());
		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		vo.setApplicationCode(dto.getApplicationCode());
		vo.setAuxDigit(dto.getAuxDigit());
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
				
		return vo;
	}

}
