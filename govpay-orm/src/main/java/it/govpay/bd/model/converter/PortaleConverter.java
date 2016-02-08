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

import it.govpay.bd.model.Portale;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.PortaleApplicazione;

import java.util.ArrayList;
import java.util.List;

public class PortaleConverter {

	public static List<Portale> toDTOList(List<it.govpay.orm.Portale> enteLst) {
		List<Portale> lstDTO = new ArrayList<Portale>();
		if(enteLst != null && !enteLst.isEmpty()) {
			for(it.govpay.orm.Portale ente: enteLst){
				lstDTO.add(toDTO(ente));
			}
		}
		return lstDTO;
	}

	public static Portale toDTO(it.govpay.orm.Portale vo) {
		Portale dto = new Portale();
		dto.setId(vo.getId());
		dto.setCodPortale(vo.getCodPortale());
		dto.setDefaultCallbackURL(vo.getDefaultCallbackURL());
		dto.setPrincipal(vo.getPrincipal());
		dto.setAbilitato(vo.isAbilitato());
		
		if(vo.getPortaleApplicazioneList() != null && !vo.getPortaleApplicazioneList().isEmpty()) {
			
			List<Long> idEntiLst = new ArrayList<Long>();
			for(PortaleApplicazione portaleEnte: vo.getPortaleApplicazioneList()) {
				idEntiLst.add(portaleEnte.getIdApplicazione().getId());
			}
			dto.setIdApplicazioni(idEntiLst);
		}		
		
		return dto;
	}

	public static it.govpay.orm.Portale toVO(Portale dto) {
		it.govpay.orm.Portale vo = new it.govpay.orm.Portale();
		vo.setId(dto.getId());
		vo.setCodPortale(dto.getCodPortale());
		vo.setDefaultCallbackURL(dto.getDefaultCallbackURL());
		vo.setPrincipal(dto.getPrincipal());
		vo.setAbilitato(dto.isAbilitato());
		
		if(dto.getIdApplicazioni() != null && dto.getIdApplicazioni().size() > 0) {
			List<PortaleApplicazione> portaleEnteLst = new ArrayList<PortaleApplicazione>();
			
			for(Long id: dto.getIdApplicazioni()) {
				PortaleApplicazione portaleEnte = new PortaleApplicazione();
				IdApplicazione idEnte = new IdApplicazione();
				idEnte.setId(id);
				
				portaleEnte.setIdApplicazione(idEnte);
				portaleEnteLst.add(portaleEnte);
			}
			
			vo.setPortaleApplicazioneList(portaleEnteLst);
		}
		
		return vo;
	}

}
