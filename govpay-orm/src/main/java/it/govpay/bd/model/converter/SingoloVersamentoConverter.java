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

import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.TipoBollo;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdVersamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class SingoloVersamentoConverter {

	public static List<SingoloVersamento> toDTO(List<it.govpay.orm.SingoloVersamento> applicazioneLst) throws ServiceException {
		List<SingoloVersamento> lstDTO = new ArrayList<SingoloVersamento>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.SingoloVersamento vo: applicazioneLst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static SingoloVersamento toDTO(it.govpay.orm.SingoloVersamento vo) throws ServiceException {
		SingoloVersamento dto = new SingoloVersamento();
		dto.setId(vo.getId());
		dto.setIdTributo(vo.getIdTributo().getId());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		dto.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf(vo.getStatoSingoloVersamento()));
		dto.setImportoSingoloVersamento(BigDecimal.valueOf(vo.getImportoSingoloVersamento()));
		if(vo.getTipoBollo() != null)
			dto.setTipoBollo(TipoBollo.toEnum(vo.getTipoBollo()));
		dto.setHashDocumento(vo.getHashDocumento());
		dto.setProvinciaResidenza(vo.getProvinciaResidenza());
		return dto;
	}

	public static it.govpay.orm.SingoloVersamento toVO(SingoloVersamento dto) {
		it.govpay.orm.SingoloVersamento vo = new it.govpay.orm.SingoloVersamento();
		vo.setId(dto.getId());
		IdTributo idTributo = new IdTributo();
		idTributo.setId(dto.getIdTributo());
		vo.setIdTributo(idTributo);
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		vo.setCodSingoloVersamentoEnte(dto.getCodSingoloVersamentoEnte());
		vo.setStatoSingoloVersamento(dto.getStatoSingoloVersamento().toString());
		vo.setImportoSingoloVersamento(dto.getImportoSingoloVersamento().doubleValue());
		if(dto.getTipoBollo()!=null)
			vo.setTipoBollo(dto.getTipoBollo().getCodifica());
		vo.setHashDocumento(dto.getHashDocumento());
		vo.setProvinciaResidenza(dto.getProvinciaResidenza());
		return vo;
	}
}
