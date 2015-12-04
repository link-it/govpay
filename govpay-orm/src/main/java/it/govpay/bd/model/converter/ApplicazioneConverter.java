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

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Applicazione.Versione;
import it.govpay.orm.ApplicazioneTributo;
import it.govpay.orm.IdTributo;

import java.util.ArrayList;
import java.util.List;

public class ApplicazioneConverter {

	public static List<Applicazione> toDTOList(List<it.govpay.orm.Applicazione> applicazioneLst) {
		List<Applicazione> lstDTO = new ArrayList<Applicazione>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.Applicazione applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Applicazione toDTO(it.govpay.orm.Applicazione vo) {
		Applicazione dto = new Applicazione();
		dto.setId(vo.getId());
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setPrincipal(vo.getPrincipal());
		dto.setVersione(Versione.valueOf(vo.getVersione()));
		dto.setPolicyRispedizione(vo.getPolicyRispedizione());
		dto.setAbilitato(vo.isAbilitato());
		if(vo.getApplicazioneTributoList() != null && !vo.getApplicazioneTributoList().isEmpty()) {
			List<Long> idTributi = new ArrayList<Long>();
			for(ApplicazioneTributo tributo: vo.getApplicazioneTributoList()) {
				idTributi.add(tributo.getIdTributo().getId());
			}
			dto.setIdTributi(idTributi);
		}
		return dto;
	}

	public static it.govpay.orm.Applicazione toVO(Applicazione dto) {
		it.govpay.orm.Applicazione vo = new it.govpay.orm.Applicazione();
		vo.setId(dto.getId());
		vo.setCodApplicazione(dto.getCodApplicazione());
		vo.setAbilitato(dto.isAbilitato());
		
		if(dto.getConnettoreEsito()!= null) {
			dto.getConnettoreEsito().setIdConnettore(dto.getCodApplicazione() + "_ESITO");
			vo.setCodConnettoreEsito(dto.getConnettoreEsito().getIdConnettore());
		}

		if(dto.getConnettoreVerifica()!= null) {
			dto.getConnettoreVerifica().setIdConnettore(dto.getCodApplicazione() + "_VERIFICA");
			vo.setCodConnettoreVerifica(dto.getConnettoreVerifica().getIdConnettore());
		}
		
		vo.setPrincipal(dto.getPrincipal());
		vo.setVersione(dto.getVersione().toString());
		vo.setPolicyRispedizione(dto.getPolicyRispedizione());
		
		if(dto.getIdTributi() != null && !dto.getIdTributi().isEmpty()) {
			List<ApplicazioneTributo> idTributi = new ArrayList<ApplicazioneTributo>();
			for(Long tributo: dto.getIdTributi()) {
				ApplicazioneTributo applicazioneTributo = new ApplicazioneTributo();
				IdTributo idTributo = new IdTributo();
				idTributo.setId(tributo);
				applicazioneTributo.setIdTributo(idTributo);
				idTributi.add(applicazioneTributo);
			}
			vo.setApplicazioneTributoList(idTributi);
		}
		
		return vo;
	}
}
