/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Rendicontazione;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.IdSingoloVersamento;

public class RendicontazioneConverter {
	
	private RendicontazioneConverter() {}

	public static Rendicontazione toDTO(it.govpay.orm.Rendicontazione vo) throws CodificaInesistenteException {
		Rendicontazione dto = new Rendicontazione();
		dto.setAnomalie(vo.getAnomalie());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setIur(vo.getIur());
		dto.setIndiceDati(vo.getIndiceDati());
		dto.setImporto(vo.getImportoPagato());
		dto.setData(vo.getData());
		dto.setEsito(EsitoRendicontazione.toEnum(vo.getEsito()));
		dto.setStato(StatoRendicontazione.valueOf(vo.getStato()));
		dto.setIdFr(vo.getIdFR().getId());
		if(vo.getIdPagamento() != null)
			dto.setIdPagamento(vo.getIdPagamento().getId());
		if(vo.getIdSingoloVersamento() != null)
			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		
		return dto;
	}

	public static it.govpay.orm.Rendicontazione toVO(Rendicontazione dto) {
		it.govpay.orm.Rendicontazione vo = new it.govpay.orm.Rendicontazione();
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setIur(dto.getIur());
		vo.setIndiceDati(dto.getIndiceDati());

		vo.setImportoPagato(dto.getImporto());
		vo.setData(dto.getData());
		vo.setEsito(BigInteger.valueOf(dto.getEsito().getCodifica()));
		vo.setStato(dto.getStato().toString());
		vo.setAnomalie(dto.getAnomalieString());
		
		IdFr idFr = new IdFr();
		idFr.setId(dto.getIdFr());
		vo.setIdFR(idFr);
		if(dto.getIdPagamento() != null) {
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(dto.getIdPagamento());
			idPagamento.setIdPagamento(dto.getIdPagamento());
			vo.setIdPagamento(idPagamento);
		}
		
		if(dto.getIdSingoloVersamento() != null) {
			IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
			idSingoloVersamento.setId(dto.getIdSingoloVersamento());
			vo.setIdSingoloVersamento(idSingoloVersamento);
		}
		return vo;
	}

	public static List<Rendicontazione> toDTO(
			List<it.govpay.orm.Rendicontazione> rendicontazioneVOLst) throws CodificaInesistenteException {
		List<Rendicontazione> dto = new ArrayList<>();
		for(it.govpay.orm.Rendicontazione vo : rendicontazioneVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
