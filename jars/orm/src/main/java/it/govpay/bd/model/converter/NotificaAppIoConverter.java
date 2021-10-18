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

import it.govpay.bd.model.NotificaAppIo;
import it.govpay.model.NotificaAppIo.StatoMessaggio;
import it.govpay.model.NotificaAppIo.StatoSpedizione;
import it.govpay.model.NotificaAppIo.TipoNotifica;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdTipoVersamentoDominio;
import it.govpay.orm.IdVersamento;

public class NotificaAppIoConverter {

	public static it.govpay.orm.NotificaAppIO toVO(NotificaAppIo dto) {
		it.govpay.orm.NotificaAppIO vo = new it.govpay.orm.NotificaAppIO();
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setDataProssimaSpedizione(dto.getDataProssimaSpedizione());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		
		
		if(dto.getIdTipoVersamentoDominio() > 0) {
			IdTipoVersamentoDominio idTipoVersamento = new IdTipoVersamentoDominio();
			idTipoVersamento.setId(dto.getIdTipoVersamentoDominio());
			vo.setIdTipoVersamentoDominio(idTipoVersamento);
		}

		vo.setStato(dto.getStato().toString());
		vo.setTentativiSpedizione(dto.getTentativiSpedizione());
		vo.setTipoEsito(dto.getTipo().toString());
		
		vo.setDebitoreIdentificativo(dto.getDebitoreIdentificativo());
		vo.setCodVersamentoEnte(dto.getCodVersamentoEnte());
		vo.setCodApplicazione(dto.getCodApplicazione());
		vo.setCodDominio(dto.getCodDominio());
		vo.setIuv(dto.getIuv());
		
		vo.setIdMessaggio(dto.getIdMessaggio());
		if(dto.getStatoMessaggio() != null)
			vo.setStatoMessaggio(dto.getStatoMessaggio().toString());
		
		if(dto.getIdRpt() != null) {
			IdRpt idRpt = new IdRpt();
			idRpt.setId(dto.getIdRpt());
			vo.setIdRpt(idRpt);
		}
		
		return vo;
	}

	public static List<NotificaAppIo> toDTOList(List<it.govpay.orm.NotificaAppIO> findAll) {
		List<NotificaAppIo> notifiche = new ArrayList<>();
		for(it.govpay.orm.NotificaAppIO vo : findAll)
			notifiche.add(toDTO(vo));
		return notifiche;
	}

	public static NotificaAppIo toDTO(it.govpay.orm.NotificaAppIO vo) {
		NotificaAppIo dto = new NotificaAppIo();
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setDataProssimaSpedizione(vo.getDataProssimaSpedizione());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		if(vo.getIdTipoVersamentoDominio() != null)
			dto.setIdTipoVersamentoDominio(vo.getIdTipoVersamentoDominio().getId());
		
		dto.setStato(StatoSpedizione.valueOf(vo.getStato()));
		dto.setTentativiSpedizione(vo.getTentativiSpedizione());
		dto.setTipo(TipoNotifica.valueOf(vo.getTipoEsito()));
		
		dto.setDebitoreIdentificativo(vo.getDebitoreIdentificativo());
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setCodDominio(vo.getCodDominio());
		dto.setIuv(vo.getIuv());
		
		dto.setIdMessaggio(vo.getIdMessaggio());
		if(vo.getStatoMessaggio() != null)
			dto.setStatoMessaggio(StatoMessaggio.valueOf(vo.getStatoMessaggio()));
		
		if(vo.getIdRpt() != null)
			dto.setIdRpt(vo.getIdRpt().getId());
		
		return dto;
	}

}
