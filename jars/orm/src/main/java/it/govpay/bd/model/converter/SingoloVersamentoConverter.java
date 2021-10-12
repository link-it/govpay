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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.SingoloVersamento;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdVersamento;

public class SingoloVersamentoConverter {

	public static List<SingoloVersamento> toDTO(List<it.govpay.orm.SingoloVersamento> applicazioneLst) throws ServiceException {
		List<SingoloVersamento> lstDTO = new ArrayList<>();
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
		if(vo.getIdTributo() != null)
			dto.setIdTributo(vo.getIdTributo().getId());
		if(vo.getIdIbanAccredito() != null)
			dto.setIdIbanAccredito(vo.getIdIbanAccredito().getId());
		if(vo.getIdIbanAppoggio() != null)
			dto.setIdIbanAppoggio(vo.getIdIbanAppoggio().getId());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		dto.setImportoSingoloVersamento(BigDecimal.valueOf(vo.getImportoSingoloVersamento()));
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		dto.setCodContabilita(vo.getCodiceContabilita());
		dto.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf(vo.getStatoSingoloVersamento()));
		if(vo.getTipoBollo() != null)
			dto.setTipoBollo(TipoBollo.toEnum(vo.getTipoBollo()));
		if(vo.getTipoContabilita() != null)
			dto.setTipoContabilita(TipoContabilita.toEnum(vo.getTipoContabilita()));
		dto.setHashDocumento(vo.getHashDocumento());
		dto.setProvinciaResidenza(vo.getProvinciaResidenza());
		dto.setDatiAllegati(vo.getDatiAllegati());
		dto.setDescrizione(vo.getDescrizione());
		dto.setIndiceDati(vo.getIndiceDati()); 
		dto.setDescrizioneCausaleRPT(vo.getDescrizioneCausaleRPT());
		dto.setContabilita(vo.getContabilita());
		if(vo.getIdDominio() != null)
			dto.setIdDominio(vo.getIdDominio().getId());
		
		return dto;
	}

	public static it.govpay.orm.SingoloVersamento toVO(SingoloVersamento dto) {
		it.govpay.orm.SingoloVersamento vo = new it.govpay.orm.SingoloVersamento();
		vo.setId(dto.getId());
		
		if(dto.getIdIbanAccredito() != null) {
			IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
			idIbanAccredito.setId(dto.getIdIbanAccredito());
			vo.setIdIbanAccredito(idIbanAccredito);
		}
		
		if(dto.getIdIbanAppoggio() != null) {
			IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
			idIbanAccredito.setId(dto.getIdIbanAppoggio());
			vo.setIdIbanAppoggio(idIbanAccredito);
		}
		
		if(dto.getIdTributo() != null) {
			IdTributo idTributo = new IdTributo();
			idTributo.setId(dto.getIdTributo());
			vo.setIdTributo(idTributo);
		}
		
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		
		vo.setCodSingoloVersamentoEnte(dto.getCodSingoloVersamentoEnte());
		vo.setCodiceContabilita(dto.getCodContabilita());
		vo.setStatoSingoloVersamento(dto.getStatoSingoloVersamento().toString());
		vo.setImportoSingoloVersamento(dto.getImportoSingoloVersamento().doubleValue());
		if(dto.getTipoBollo() != null)
			vo.setTipoBollo(dto.getTipoBollo().getCodifica());
		if(dto.getTipoContabilita() != null)
			vo.setTipoContabilita(dto.getTipoContabilita().getCodifica());
		vo.setHashDocumento(dto.getHashDocumento());
		vo.setProvinciaResidenza(dto.getProvinciaResidenza());
		vo.setDatiAllegati(dto.getDatiAllegati());
		vo.setDescrizione(dto.getDescrizione());
		vo.setIndiceDati(dto.getIndiceDati());
		vo.setDescrizioneCausaleRPT(dto.getDescrizioneCausaleRPT());
		vo.setContabilita(dto.getContabilita());
		
		if(dto.getIdDominio() != null) {
			IdDominio idDominio = new IdDominio();
			idDominio.setId(dto.getIdDominio());
			vo.setIdDominio(idDominio);
		}
		
		return vo;
	}
}
