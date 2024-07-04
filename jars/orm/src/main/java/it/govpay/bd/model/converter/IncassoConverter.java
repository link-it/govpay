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

import it.govpay.bd.model.Incasso;
import it.govpay.model.Incasso.StatoIncasso;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdOperatore;

public class IncassoConverter {
	
	private IncassoConverter() {}

	public static Incasso toDTO(it.govpay.orm.Incasso vo) {
			Incasso dto = new Incasso();
			dto.setCausale(vo.getCausale());
			dto.setCodDominio(vo.getCodDominio());
			dto.setDataContabile(vo.getDataContabile());
			dto.setDataIncasso(vo.getDataOraIncasso());
			dto.setDataValuta(vo.getDataValuta());
			dto.setDispositivo(vo.getNomeDispositivo());
			dto.setId(vo.getId());
			if(vo.getIdApplicazione() != null)
				dto.setIdApplicazione(vo.getIdApplicazione().getId());
			if(vo.getIdOperatore() != null)
				dto.setIdApplicazione(vo.getIdOperatore().getId());
			
			dto.setImporto(vo.getImporto());
			dto.setTrn(vo.getTrn());
			dto.setIbanAccredito(vo.getIbanAccredito());
			dto.setSct(vo.getSct());
			dto.setIuv(vo.getIuv());
			dto.setIdRiconciliazione(vo.getIdentificativo());
			dto.setIdFlussoRendicontazione(vo.getCodFlussoRendicontazione());
			dto.setStato(StatoIncasso.valueOf(vo.getStato()));
			dto.setDescrizioneStato(vo.getDescrizioneStato());
			return dto;
	}

	public static it.govpay.orm.Incasso toVO(Incasso dto) {
		it.govpay.orm.Incasso vo = new it.govpay.orm.Incasso();
		vo.setCausale(dto.getCausale());
		vo.setCodDominio(dto.getCodDominio());
		vo.setDataContabile(dto.getDataContabile());
		vo.setDataOraIncasso(dto.getDataIncasso());
		vo.setDataValuta(dto.getDataValuta());
		vo.setNomeDispositivo(dto.getDispositivo());
		vo.setId(dto.getId());
		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApplicazione);
		}
		if(dto.getIdOperatore() != null) {
			IdOperatore idOperatore = new IdOperatore();
			idOperatore.setId(dto.getIdOperatore());
			vo.setIdOperatore(idOperatore);
		}
		vo.setImporto(dto.getImporto());
		vo.setTrn(dto.getTrn());
		vo.setIbanAccredito(dto.getIbanAccredito());
		vo.setSct(dto.getSct());
		vo.setIuv(dto.getIuv());
		vo.setIdentificativo(dto.getIdRiconciliazione());
		vo.setCodFlussoRendicontazione(dto.getIdFlussoRendicontazione());
		vo.setStato(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		
		return vo;
}
}
