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

import it.govpay.bd.model.Notifica;
import it.govpay.model.Notifica.StatoSpedizione;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdRr;

public class NotificaConverter {

	public static it.govpay.orm.Notifica toVO(Notifica dto) {
		it.govpay.orm.Notifica vo = new it.govpay.orm.Notifica();
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setDataProssimaSpedizione(dto.getDataProssimaSpedizione());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		IdApplicazione idApplicazione = new IdApplicazione();
		idApplicazione.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApplicazione);
		if(dto.getIdRpt() != null) {
			IdRpt idRpt = new IdRpt();
			idRpt.setId(dto.getIdRpt());
			vo.setIdRpt(idRpt);
		}
		if(dto.getIdRr() != null) {
			IdRr idRr = new IdRr();
			idRr.setId(dto.getIdRr());
			vo.setIdRr(idRr);
		}
		vo.setStato(dto.getStato().toString());
		vo.setTentativiSpedizione(dto.getTentativiSpedizione());
		vo.setTipoEsito(dto.getTipo().toString());
		return vo;
	}

	public static List<Notifica> toDTOList(List<it.govpay.orm.Notifica> findAll) {
		List<Notifica> notifiche = new ArrayList<Notifica>();
		for(it.govpay.orm.Notifica vo : findAll)
			notifiche.add(toDTO(vo));
		return notifiche;
	}

	public static Notifica toDTO(it.govpay.orm.Notifica vo) {
		Notifica dto = new Notifica();
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setDataProssimaSpedizione(vo.getDataProssimaSpedizione());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		if(vo.getIdRpt() != null)
			dto.setIdRpt(vo.getIdRpt().getId());
		if(vo.getIdRr() != null)
			dto.setIdRr(vo.getIdRr().getId());
		dto.setStato(StatoSpedizione.valueOf(vo.getStato()));
		dto.setTentativiSpedizione(vo.getTentativiSpedizione());
		dto.setTipo(TipoNotifica.valueOf(vo.getTipoEsito()));
		return dto;
	}

}
