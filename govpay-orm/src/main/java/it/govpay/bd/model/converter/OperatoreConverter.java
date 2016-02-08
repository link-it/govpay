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

import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdEnte;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.OperatoreEnte;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class OperatoreConverter {

	public static List<Operatore> toDTOList(List<it.govpay.orm.Operatore> operatoreLst) throws ServiceException {
		List<Operatore> lstDTO = new ArrayList<Operatore>();
		if(operatoreLst != null && !operatoreLst.isEmpty()) {
			for(it.govpay.orm.Operatore operatore: operatoreLst){
				lstDTO.add(toDTO(operatore));
			}
		}
		return lstDTO;
	}

	public static Operatore toDTO(it.govpay.orm.Operatore vo) throws ServiceException {
		Operatore dto = new Operatore();
		dto.setId(vo.getId());
		dto.setPrincipal(vo.getPrincipal());
		dto.setNome(vo.getNome());
		dto.setProfilo(ProfiloOperatore.toEnum(vo.getProfilo()));
		dto.setAbilitato(vo.isAbilitato());
		if(vo.getOperatoreEnteList() != null && !vo.getOperatoreEnteList().isEmpty()) {
			List<Long> operatoreEnteId = new ArrayList<Long>();
			for(OperatoreEnte operatoreEnte: vo.getOperatoreEnteList()) {
				operatoreEnteId.add(operatoreEnte.getIdEnte().getId());
			}
			dto.setIdEnti(operatoreEnteId);
		}

		if(vo.getOperatoreApplicazioneList() != null && !vo.getOperatoreApplicazioneList().isEmpty()) {
			List<Long> operatoreApplicazioneId = new ArrayList<Long>();
			for(OperatoreApplicazione operatoreApplicazione: vo.getOperatoreApplicazioneList()) {
				operatoreApplicazioneId.add(operatoreApplicazione.getIdApplicazione().getId());
			}
			dto.setIdApplicazioni(operatoreApplicazioneId);
		}

		return dto;
	}

	public static it.govpay.orm.Operatore toVO(Operatore dto) {
		it.govpay.orm.Operatore vo = new it.govpay.orm.Operatore();
		vo.setId(dto.getId());
		vo.setPrincipal(dto.getPrincipal());
		vo.setNome(dto.getNome());
		vo.setProfilo(dto.getProfilo().getCodifica());
		vo.setAbilitato(dto.isAbilitato());
		if(dto.getIdEnti() != null && dto.getIdEnti().size() > 0) {
			List<OperatoreEnte> operatoreEnteLst = new ArrayList<OperatoreEnte>();
			for(Long ente: dto.getIdEnti()) {
				OperatoreEnte operatoreEnte = new OperatoreEnte();
				IdEnte idEnte = new IdEnte();
				idEnte.setId(ente);
				
				operatoreEnte.setIdEnte(idEnte);
				operatoreEnteLst.add(operatoreEnte);
			}
			vo.setOperatoreEnteList(operatoreEnteLst);
		}
		
		if(dto.getIdApplicazioni() != null && dto.getIdApplicazioni().size() > 0) {
			List<OperatoreApplicazione> operatoreApplicazioneLst = new ArrayList<OperatoreApplicazione>();
			for(Long applicazione: dto.getIdApplicazioni()) {
				OperatoreApplicazione operatoreApplicazione = new OperatoreApplicazione();
				IdApplicazione idApplicazione = new IdApplicazione();
				idApplicazione.setId(applicazione);
				
				operatoreApplicazione.setIdApplicazione(idApplicazione);
				operatoreApplicazioneLst.add(operatoreApplicazione);
			}
			vo.setOperatoreApplicazioneList(operatoreApplicazioneLst);
		}
		
		return vo;
	}

}
