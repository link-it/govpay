/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Tracciato;
import it.govpay.orm.IdOperatore;

public class TracciatoConverter {

	public static List<Tracciato> toDTOList(List<it.govpay.orm.Tracciato> tracciatoLst) throws ServiceException {
		List<Tracciato> lstDTO = new ArrayList<Tracciato>();
		if(tracciatoLst != null && !tracciatoLst.isEmpty()) {
			for(it.govpay.orm.Tracciato tracciato: tracciatoLst){
				lstDTO.add(toDTO(tracciato));
			}
		}
		return lstDTO;
	}

	public static Tracciato toDTO(it.govpay.orm.Tracciato vo) throws ServiceException {
		Tracciato dto = new Tracciato();

		dto.setId(vo.getId());
		dto.setStato(vo.getStato());
		dto.setDescrizioneStato(vo.getDescrizioneStato());

		dto.setIdOperatore(vo.getIdOperatore().getId());
		
		dto.setNomeFile(vo.getNomeFile());
		dto.setDataCaricamento(vo.getDataCaricamento());
		dto.setDataUltimoAggiornamento(vo.getDataUltimoAggiornamento());

		dto.setLineaElaborazione(vo.getLineaElaborazione());
		dto.setNumLineeTotali(vo.getNumLineeTotali());
		dto.setNumOperazioniOk(vo.getNumOperazioniOk());
		dto.setNumOperazioniKo(vo.getNumOperazioniKo());

		dto.setRawDataRichiesta(vo.getRawDataRichiesta());
		dto.setRawDataRisposta(vo.getRawDataRisposta());
		
		return dto;
	}

	public static it.govpay.orm.Tracciato toVO(Tracciato dto) throws ServiceException {
		it.govpay.orm.Tracciato vo = new it.govpay.orm.Tracciato();
		
		vo.setId(dto.getId());
		vo.setStato(dto.getStato());
		vo.setDescrizioneStato(dto.getDescrizioneStato());

		IdOperatore idOperatore = new IdOperatore();
		idOperatore.setId(dto.getIdOperatore());
		
		vo.setIdOperatore(idOperatore);
		
		vo.setNomeFile(dto.getNomeFile());
		vo.setDataCaricamento(dto.getDataCaricamento());
		vo.setDataUltimoAggiornamento(dto.getDataUltimoAggiornamento());

		vo.setLineaElaborazione(dto.getLineaElaborazione());
		vo.setNumLineeTotali(dto.getNumLineeTotali());
		vo.setNumOperazioniOk(dto.getNumOperazioniOk());
		vo.setNumOperazioniKo(dto.getNumOperazioniKo());

		vo.setRawDataRichiesta(dto.getRawDataRichiesta());
		vo.setRawDataRisposta(dto.getRawDataRisposta());

		return vo;
	}

}
