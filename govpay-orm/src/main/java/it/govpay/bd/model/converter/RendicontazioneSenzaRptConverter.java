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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.model.RendicontazioneSenzaRpt;
import it.govpay.orm.IdFrApplicazione;
import it.govpay.orm.IdIuv;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.RendicontazioneSenzaRPT;

public class RendicontazioneSenzaRptConverter {

	public static RendicontazioneSenzaRPT toVO(RendicontazioneSenzaRpt dto) {
		RendicontazioneSenzaRPT vo = new RendicontazioneSenzaRPT();
		vo.setId(dto.getId());
		IdFrApplicazione idFrApplicazione = new IdFrApplicazione();
		idFrApplicazione.setId(dto.getIdFrApplicazioni());
		vo.setIdFrApplicazione(idFrApplicazione);
		IdIuv idIuv = new IdIuv();
		idIuv.setId(dto.getIdIuv());
		vo.setIdIuv(idIuv);
		if(dto.getIdSingoloVersamento() != null){
			IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
			idSingoloVersamento.setId(dto.getIdSingoloVersamento());
			vo.setIdSingoloVersamento(idSingoloVersamento);
		}
		vo.setImportoPagato(dto.getImportoPagato().doubleValue());
		vo.setIur(dto.getIur());
		vo.setRendicontazioneData(dto.getDataRendicontazione());
		return vo;
	}

	public static List<RendicontazioneSenzaRpt> toDTO(List<RendicontazioneSenzaRPT> rendicontazioniSenzaRpt) {
		List<RendicontazioneSenzaRpt> dto = new ArrayList<RendicontazioneSenzaRpt>();
		for(it.govpay.orm.RendicontazioneSenzaRPT vo : rendicontazioniSenzaRpt) {
			dto.add(toDTO(vo));
		}
		return dto;
	}

	public static RendicontazioneSenzaRpt toDTO(RendicontazioneSenzaRPT vo) {
		RendicontazioneSenzaRpt dto = new RendicontazioneSenzaRpt();
		dto.setDataRendicontazione(vo.getRendicontazioneData());
		dto.setId(vo.getId());
		dto.setIdFrApplicazioni(vo.getIdFrApplicazione().getId());
		dto.setIdIuv(vo.getIdIuv().getId());
		if(vo.getIdSingoloVersamento() != null)
			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setIur(vo.getIur());
		return dto;
	}

}
