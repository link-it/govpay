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
import java.util.List;

import it.govpay.bd.model.Promemoria;
import it.govpay.model.Promemoria.StatoSpedizione;
import it.govpay.model.Promemoria.TipoPromemoria;
import it.govpay.orm.IdVersamento;

public class PromemoriaConverter {

	public static it.govpay.orm.Promemoria toVO(Promemoria dto) {
		it.govpay.orm.Promemoria vo = new it.govpay.orm.Promemoria();
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setDataProssimaSpedizione(dto.getDataProssimaSpedizione());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		vo.setStato(dto.getStato().toString());
		vo.setTentativiSpedizione(dto.getTentativiSpedizione());
		vo.setTipo(dto.getTipo().toString());
		vo.setDebitoreEmail(dto.getDebitoreEmail());
		vo.setOggetto(dto.getOggetto());
		vo.setMessaggio(dto.getMessaggio());
		vo.setAllegaPdf(dto.isAllegaPdf());
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
		dto.setIdVersamento(vo.getIdVersamento().getId());
		dto.setStato(StatoSpedizione.valueOf(vo.getStato()));
		dto.setTentativiSpedizione(vo.getTentativiSpedizione());
		dto.setTipo(TipoPromemoria.valueOf(vo.getTipo()));
		dto.setDebitoreEmail(vo.getDebitoreEmail());
		dto.setOggetto(vo.getOggetto());
		dto.setMessaggio(vo.getMessaggio());
		dto.setAllegaPdf(vo.isAllegaPdf());
		return dto;
	}

}
