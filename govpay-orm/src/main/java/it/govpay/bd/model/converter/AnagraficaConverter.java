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

import it.govpay.bd.model.Anagrafica;

import java.util.ArrayList;
import java.util.List;

public class AnagraficaConverter {

	public static List<Anagrafica> toDTOList(List<it.govpay.orm.Anagrafica> anagraficaLst) {
		List<Anagrafica> lstDTO = new ArrayList<Anagrafica>();
		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
			for(it.govpay.orm.Anagrafica anagrafica: anagraficaLst){
				lstDTO.add(toDTO(anagrafica));
			}
		}
		return lstDTO;
	}

	public static Anagrafica toDTO(it.govpay.orm.Anagrafica vo) {
		Anagrafica dto = new Anagrafica();
		dto.setId(vo.getId());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setCodUnivoco(vo.getCodUnivoco());
		dto.setIndirizzo(vo.getIndirizzo());
		dto.setCivico(vo.getCivico());
		dto.setCap(vo.getCap());
		dto.setLocalita(vo.getLocalita());
		dto.setProvincia(vo.getProvincia());
		dto.setNazione(vo.getNazione());
		dto.setEmail(vo.getEmail());
		dto.setTelefono(vo.getTelefono());
		dto.setCellulare(vo.getCellulare());
		dto.setFax(vo.getFax());
		
		return dto;
	}

	public static it.govpay.orm.Anagrafica toVO(Anagrafica dto) {
		it.govpay.orm.Anagrafica vo = new it.govpay.orm.Anagrafica();
		vo.setId(dto.getId());
		vo.setRagioneSociale(dto.getRagioneSociale());
		vo.setCodUnivoco(dto.getCodUnivoco());
		vo.setIndirizzo(dto.getIndirizzo());
		vo.setCivico(dto.getCivico());
		vo.setCap(dto.getCap());
		vo.setLocalita(dto.getLocalita());
		vo.setProvincia(dto.getProvincia());
		vo.setNazione(dto.getNazione());
		vo.setEmail(dto.getEmail());
		vo.setTelefono(dto.getTelefono());
		vo.setCellulare(dto.getCellulare());
		vo.setFax(dto.getFax());

		return vo;
	}

}
