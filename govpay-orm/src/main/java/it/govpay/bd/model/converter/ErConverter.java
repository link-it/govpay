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

import it.govpay.bd.model.Er;
import it.govpay.bd.model.Er.Stato;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdTracciato;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class ErConverter {

	public static List<Er> toDTOList(List<it.govpay.orm.ER> lst) throws ServiceException {
		List<Er> lstDTO = new ArrayList<Er>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.ER obj: lst){
				lstDTO.add(toDTO(obj));
			}
		}
		return lstDTO;
	}

	public static Er toDTO(it.govpay.orm.ER vo) throws ServiceException {
		Er dto = new Er();
		dto.setId(vo.getId());
		dto.setDataOraMsgEsito(vo.getDataOraMsgEsito());
		dto.setCodMsgEsito(vo.getCodMsgEsito());
		dto.setStato(Stato.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
		dto.setDataOraCreazione(vo.getDataOraCreazione());
		dto.setIdRr(vo.getIdRR().getId());
		dto.setImportoTotaleRevocato(vo.getImportoTotaleRevocato());
		return dto;
	}

	public static it.govpay.orm.ER toVO(Er dto) {
		it.govpay.orm.ER vo = new it.govpay.orm.ER();
		vo.setId(dto.getId());
		vo.setDataOraMsgEsito(dto.getDataOraMsgEsito());
		vo.setCodMsgEsito(dto.getCodMsgEsito());
		vo.setStato(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciatoXML());
		vo.setIdTracciatoXML(idTracciato);
		
		vo.setDataOraCreazione(dto.getDataOraCreazione());
		
		IdRr idRr = new IdRr();
		idRr.setId(dto.getIdRr());
		vo.setIdRR(idRr);
		
		vo.setImportoTotaleRevocato(dto.getImportoTotaleRevocato());
		return vo;
	}

}
