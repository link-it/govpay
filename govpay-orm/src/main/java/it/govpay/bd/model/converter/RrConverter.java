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

import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Rr.Stato;
import it.govpay.orm.IdRt;
import it.govpay.orm.IdTracciato;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RrConverter {

	public static List<Rr> toDTOList(List<it.govpay.orm.RR> applicazioneLst) throws ServiceException {
		List<Rr> lstDTO = new ArrayList<Rr>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.RR applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Rr toDTO(it.govpay.orm.RR vo) throws ServiceException {
		Rr dto = new Rr();
		dto.setId(vo.getId());
		dto.setDataOraMsgRevoca(vo.getDataOraMsgRevoca());
		dto.setCodMsgRevoca(vo.getCodMsgRevoca());
		dto.setStato(Stato.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
		dto.setDataOraCreazione(vo.getDataOraCreazione());
		dto.setIdRt(vo.getIdRT().getId());
		dto.setImportoTotaleRevocato(vo.getImportoTotaleRevocato());
		return dto;
	}

	public static it.govpay.orm.RR toVO(Rr dto) {
		it.govpay.orm.RR vo = new it.govpay.orm.RR();
		vo.setId(dto.getId());
		vo.setDataOraMsgRevoca(dto.getDataOraMsgRevoca());
		vo.setCodMsgRevoca(dto.getCodMsgRevoca());
		vo.setStato(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciatoXML());
		vo.setIdTracciatoXML(idTracciato);
		
		vo.setDataOraCreazione(dto.getDataOraCreazione());
		
		IdRt idRt = new IdRt();
		idRt.setId(dto.getIdRt());
		vo.setIdRT(idRt);
		
		vo.setImportoTotaleRevocato(dto.getImportoTotaleRevocato());
		return vo;
	}

}
