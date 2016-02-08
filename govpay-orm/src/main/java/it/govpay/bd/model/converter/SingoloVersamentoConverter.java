/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
import it.govpay.orm.IdTributo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SingoloVersamentoConverter {

	public static List<SingoloVersamento> toDTOList(List<it.govpay.orm.SingoloVersamento> applicazioneLst) {
		List<SingoloVersamento> lstDTO = new ArrayList<SingoloVersamento>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.SingoloVersamento vo: applicazioneLst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static SingoloVersamento toDTO(it.govpay.orm.SingoloVersamento vo) {
		SingoloVersamento dto = new SingoloVersamento();

		dto.setId(vo.getId());
		dto.setAnnoRiferimento(vo.getAnnoRiferimento());
		dto.setCausaleVersamento(vo.getCausaleVersamento());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		dto.setDataEsitoSingoloPagamento(vo.getDataEsitoSingoloPagamento());
		dto.setDatiSpecificiRiscossione(vo.getDatiSpecificiRiscossione());
		dto.setEsitoSingoloPagamento(vo.getEsitoSingoloPagamento());
		dto.setIbanAccredito(vo.getIbanAccredito());

		if(vo.getImportoCommissioniPA()!= null)
			dto.setImportoCommissioniPA(BigDecimal.valueOf(vo.getImportoCommissioniPA()));

		dto.setImportoSingoloVersamento(BigDecimal.valueOf(vo.getImportoSingoloVersamento()));
		
		if(vo.getSingoloImportoPagato()!=null)
			dto.setSingoloImportoPagato(BigDecimal.valueOf(vo.getSingoloImportoPagato()));
		
		dto.setIndice(vo.getIndice());
		dto.setIur(vo.getIur());
		dto.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf(vo.getStatoSingoloVersamento()));
		dto.setIdTributo(vo.getIdTributo().getId());

		return dto;
	}

	public static it.govpay.orm.SingoloVersamento toVO(SingoloVersamento dto) {
		it.govpay.orm.SingoloVersamento vo = new it.govpay.orm.SingoloVersamento();

		vo.setAnnoRiferimento(dto.getAnnoRiferimento());
		vo.setCausaleVersamento(dto.getCausaleVersamento());
		vo.setCodSingoloVersamentoEnte(dto.getCodSingoloVersamentoEnte());
		vo.setDataEsitoSingoloPagamento(dto.getDataEsitoSingoloPagamento());
		vo.setDatiSpecificiRiscossione(dto.getDatiSpecificiRiscossione());
		vo.setEsitoSingoloPagamento(dto.getEsitoSingoloPagamento());
		vo.setIbanAccredito(dto.getIbanAccredito());
		vo.setId(dto.getId());
		if(dto.getImportoCommissioniPA() != null)
			vo.setImportoCommissioniPA(dto.getImportoCommissioniPA().doubleValue());

		vo.setImportoSingoloVersamento(dto.getImportoSingoloVersamento().doubleValue());

		if(dto.getSingoloImportoPagato() != null)
			vo.setSingoloImportoPagato(dto.getSingoloImportoPagato().doubleValue());
		vo.setIndice(dto.getIndice());
		vo.setIur(dto.getIur());
		vo.setStatoSingoloVersamento(dto.getStatoSingoloVersamento().toString());
		IdTributo idTributo = new IdTributo();
		idTributo.setId(dto.getIdTributo());

		vo.setIdTributo(idTributo);

		return vo;
	}
}
