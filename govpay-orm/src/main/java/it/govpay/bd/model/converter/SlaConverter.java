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

import it.govpay.bd.model.Sla;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class SlaConverter {

	public static List<Sla> toDTOList(List<it.govpay.orm.SLA> applicazioneLst) throws ServiceException {
		List<Sla> lstDTO = new ArrayList<Sla>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.SLA applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Sla toDTO(it.govpay.orm.SLA vo) throws ServiceException {
		Sla dto = new Sla();
		dto.setId(vo.getId());
		dto.setDescrizione(vo.getDescrizione());
		dto.setTipoEventoIniziale(vo.getTipoEventoIniziale());
		dto.setSottotipoEventoIniziale(vo.getSottotipoEventoIniziale());
		dto.setTipoEventoFinale(vo.getTipoEventoFinale());
		dto.setSottotipoEventoFinale(vo.getSottotipoEventoFinale());
		dto.setTempoA(vo.getTempoA());
		dto.setTempoB(vo.getTempoB());
		dto.setTolleranzaA(vo.getTolleranzaA());
		dto.setTolleranzaB(vo.getTolleranzaB());
		return dto;
	}

	public static it.govpay.orm.SLA toVO(Sla dto) {
		it.govpay.orm.SLA vo = new it.govpay.orm.SLA();
		vo.setId(dto.getId());
		vo.setDescrizione(dto.getDescrizione());
		vo.setTipoEventoIniziale(dto.getTipoEventoIniziale());
		vo.setSottotipoEventoIniziale(dto.getSottotipoEventoIniziale());
		vo.setTipoEventoFinale(dto.getTipoEventoFinale());
		vo.setSottotipoEventoFinale(dto.getSottotipoEventoFinale());
		vo.setTempoA(dto.getTempoA());
		vo.setTempoB(dto.getTempoB());
		vo.setTolleranzaA(dto.getTolleranzaA());
		vo.setTolleranzaB(dto.getTolleranzaB());
		return vo;
	}

}
