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

import it.govpay.bd.model.Rilevamento;
import it.govpay.orm.IdEvento;
import it.govpay.orm.IdSla;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RilevamentoConverter {

	public static List<Rilevamento> toDTOList(List<it.govpay.orm.Rilevamento> enteLst) throws ServiceException {
		List<Rilevamento> lstDTO = new ArrayList<Rilevamento>();
		if(enteLst != null && !enteLst.isEmpty()) {
			for(it.govpay.orm.Rilevamento ente: enteLst){
				lstDTO.add(toDTO(ente));
			}
		}
		return lstDTO;
	}

	public static Rilevamento toDTO(it.govpay.orm.Rilevamento vo) throws ServiceException {
		Rilevamento dto = new Rilevamento();
		dto.setIdSLA(vo.getIdSLA().getId());
		dto.setIdApplicazione(vo.getIdApplicazione());
		dto.setIdEventoIniziale(vo.getIdEventoIniziale().getId());
		dto.setIdEventoFinale(vo.getIdEventoFinale().getId());
		dto.setDataRilevamento(vo.getDataRilevamento());
		dto.setDurata(vo.getDurata());
		return dto;
	}

	public static it.govpay.orm.Rilevamento toVO(Rilevamento dto) {
		it.govpay.orm.Rilevamento vo = new it.govpay.orm.Rilevamento();
		
		IdSla idSla = new IdSla();
		idSla.setId(dto.getIdSLA());
		vo.setIdSLA(idSla);
		
		vo.setIdApplicazione(dto.getIdApplicazione());
		
		IdEvento eventoIniziale = new IdEvento();
		eventoIniziale.setId(dto.getIdEventoIniziale());
		vo.setIdEventoIniziale(eventoIniziale);
		
		IdEvento eventoFinale = new IdEvento();
		eventoFinale.setId(dto.getIdEventoFinale());
		vo.setIdEventoFinale(eventoFinale);
		
		vo.setDataRilevamento(dto.getDataRilevamento());
		vo.setDurata(dto.getDurata());

		return vo;
	}

}
