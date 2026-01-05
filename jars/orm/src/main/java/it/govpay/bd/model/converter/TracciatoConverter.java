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

import it.govpay.bd.model.Tracciato;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.IdOperatore;

public class TracciatoConverter {
	
	private TracciatoConverter() {}

	public static Tracciato toDTO(it.govpay.orm.Tracciato vo) {
		Tracciato dto = new Tracciato();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setTipo(TIPO_TRACCIATO.valueOf(vo.getTipo()));
		dto.setStato(STATO_ELABORAZIONE.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setDataCaricamento(vo.getDataCaricamento());
		dto.setDataCompletamento(vo.getDataCompletamento());
		dto.setBeanDati(vo.getBeanDati());
		dto.setFileNameRichiesta(vo.getFileNameRichiesta());
		dto.setRawRichiesta(vo.getRawRichiesta());
		dto.setFileNameEsito(vo.getFileNameEsito());
		dto.setRawEsito(vo.getRawEsito());
		if(vo.getIdOperatore() != null) {
			dto.setIdOperatore(vo.getIdOperatore().getId());
		}
		
		dto.setFormato(FORMATO_TRACCIATO.valueOf(vo.getFormato()));
		dto.setCodTipoVersamento(vo.getCodTipoVersamento());
		dto.setZipStampe(vo.getZipStampe());

		return dto;
	}

	public static it.govpay.orm.Tracciato toVO(Tracciato dto)  {
		it.govpay.orm.Tracciato vo = new it.govpay.orm.Tracciato();
		vo.setId(dto.getId());

		vo.setCodDominio(dto.getCodDominio());
		vo.setTipo(dto.getTipo().name());
		vo.setStato(dto.getStato().name());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setDataCaricamento(dto.getDataCaricamento());
		vo.setDataCompletamento(dto.getDataCompletamento());
		vo.setBeanDati(dto.getBeanDati());
		vo.setFileNameRichiesta(dto.getFileNameRichiesta());
		vo.setRawRichiesta(dto.getRawRichiesta());
		vo.setFileNameEsito(dto.getFileNameEsito());
		vo.setRawEsito(dto.getRawEsito());
		
		if(dto.getIdOperatore() != null) {
			IdOperatore idOperatore = new IdOperatore();
			idOperatore.setId(dto.getIdOperatore());
			vo.setIdOperatore(idOperatore);
		}

		vo.setFormato(dto.getFormato().name());
		vo.setCodTipoVersamento(dto.getCodTipoVersamento());
		vo.setZipStampe(dto.getZipStampe());

		return vo;
	}

}
