/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.bd.model.Promemoria;
import it.govpay.model.Promemoria.StatoSpedizione;
import it.govpay.model.Promemoria.TipoPromemoria;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdVersamento;

public class PromemoriaConverter {

	public static it.govpay.orm.Promemoria toVO(Promemoria dto) {
		it.govpay.orm.Promemoria vo = new it.govpay.orm.Promemoria();
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setDataProssimaSpedizione(dto.getDataProssimaSpedizione());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		if(dto.getIdVersamento() != null &&  dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		vo.setStato(dto.getStato().toString());
		vo.setTentativiSpedizione(dto.getTentativiSpedizione());
		vo.setTipo(dto.getTipo().toString());
		vo.setDestinatarioTo(dto.getDestinatarioTo());
		vo.setDestinatarioCc(dto.getDestinatarioCc());
		vo.setOggetto(dto.getOggetto());
		vo.setMessaggio(dto.getMessaggio());
		vo.setAllegaPdf(dto.isAllegaPdf());
		if(dto.getIdRpt() != null) {
			IdRpt idRpt = new IdRpt();
			idRpt.setId(dto.getIdRpt());
			vo.setIdRPT(idRpt);
		}
		vo.setMessaggioContentType(dto.getContentType());
		if(dto.getIdDocumento() != null &&  dto.getIdDocumento() > 0) {
			IdDocumento idDocumento = new IdDocumento();
			idDocumento.setId(dto.getIdDocumento());
			vo.setIdDocumento(idDocumento);
		}
		return vo;
	}

	public static List<Promemoria> toDTOList(List<it.govpay.orm.Promemoria> findAll) {
		List<Promemoria> notifiche = new ArrayList<>();
		for(it.govpay.orm.Promemoria vo : findAll)
			notifiche.add(toDTO(vo));
		return notifiche;
	}

	public static Promemoria toDTO(it.govpay.orm.Promemoria vo) {
		Promemoria dto = new Promemoria();
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setDataProssimaSpedizione(vo.getDataProssimaSpedizione());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());
		dto.setStato(StatoSpedizione.valueOf(vo.getStato()));
		dto.setTentativiSpedizione(vo.getTentativiSpedizione());
		dto.setTipo(TipoPromemoria.valueOf(vo.getTipo()));
		dto.setDestinatarioTo(vo.getDestinatarioTo());
		dto.setDestinatarioCc(vo.getDestinatarioCc());
		dto.setOggetto(vo.getOggetto());
		dto.setMessaggio(vo.getMessaggio());
		dto.setAllegaPdf(vo.isAllegaPdf());
		if(vo.getIdRPT() != null)
			dto.setIdRpt(vo.getIdRPT().getId());
		dto.setContentType(vo.getMessaggioContentType());
		if(vo.getIdDocumento() != null)
			dto.setIdDocumento(vo.getIdDocumento().getId());
		return dto;
	}

}
