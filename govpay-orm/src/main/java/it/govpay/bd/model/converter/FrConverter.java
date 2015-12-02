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

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Fr.StatoFr;
import it.govpay.orm.IdPsp;
import it.govpay.orm.IdTracciato;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class FrConverter {

	public static List<Fr> toDTOList(List<it.govpay.orm.FR> lst) throws ServiceException {
		List<Fr> lstDTO = new ArrayList<Fr>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.FR vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static Fr toDTO(it.govpay.orm.FR vo) throws ServiceException {
		Fr dto = new Fr();
		dto.setAnnoRiferimento(vo.getAnnoRiferimento());
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());
		dto.setStato(StatoFr.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setIur(vo.getIur());
		dto.setCodFlusso(vo.getCodFlusso());
		dto.setIdPsp(vo.getIdPsp().getId());
		dto.setDataOraFlusso(vo.getDataOraFlusso());
		dto.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
		dto.setImportoTotalePagamenti(vo.getImportoTotalePagamenti());
		dto.setDataRegolamento(vo.getDataRegolamento());
		return dto;
	}

	public static it.govpay.orm.FR toVO(Fr dto) {
		it.govpay.orm.FR vo = new it.govpay.orm.FR();
		vo.setAnnoRiferimento(dto.getAnnoRiferimento());
		vo.setNumeroPagamenti(dto.getNumeroPagamenti());
		vo.setStato(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		vo.setIur(dto.getIur());
		vo.setCodFlusso(dto.getCodFlusso());
		
		IdPsp idPsp = new IdPsp();
		idPsp.setId(dto.getIdPsp());
		vo.setIdPsp(idPsp);
		
		vo.setDataOraFlusso(dto.getDataOraFlusso());
		
		IdTracciato idTracciato = new IdTracciato();
		idTracciato.setId(dto.getIdTracciatoXML());
		vo.setIdTracciatoXML(idTracciato);

		vo.setImportoTotalePagamenti(dto.getImportoTotalePagamenti());
		vo.setDataRegolamento(dto.getDataRegolamento());
		return vo;
	}

}
