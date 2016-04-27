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

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.orm.ApplicazioneDominio;
import it.govpay.orm.ApplicazioneTributo;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTributo;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class ApplicazioneConverter {

	public static Applicazione toDTO(it.govpay.orm.Applicazione vo, Connettore connettoreNotifica, Connettore connettoreVerifica) throws ServiceException {
		Applicazione dto = new Applicazione();
		dto.setAbilitato(vo.isAbilitato());
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setConnettoreNotifica(connettoreNotifica);
		dto.setConnettoreVerifica(connettoreVerifica);
		dto.setFirmaRichiesta(FirmaRichiesta.toEnum(vo.getFirmaRicevuta()));
		dto.setId(vo.getId());
		dto.setPrincipal(vo.getPrincipal());
		dto.setTrusted(vo.getTrusted());
		
		List<Long> idTributi = new ArrayList<Long>();
		if(vo.getApplicazioneTributoList() != null && !vo.getApplicazioneTributoList().isEmpty()) {
			for(ApplicazioneTributo tributo: vo.getApplicazioneTributoList()) {
				idTributi.add(tributo.getIdTributo().getId());
			}
		}
		dto.setIdTributi(idTributi);
		
		List<Long> idDomini = new ArrayList<Long>();
		if(vo.getApplicazioneDominioList() != null && !vo.getApplicazioneDominioList().isEmpty()) {
			for(ApplicazioneDominio dominio: vo.getApplicazioneDominioList()) {
				idDomini.add(dominio.getIdDominio().getId());
			}
		}
		dto.setIdDomini(idDomini);
		return dto;
	}

	public static it.govpay.orm.Applicazione toVO(Applicazione dto) {
		it.govpay.orm.Applicazione vo = new it.govpay.orm.Applicazione();
		vo.setId(dto.getId());
		vo.setCodApplicazione(dto.getCodApplicazione());
		vo.setAbilitato(dto.isAbilitato());
		
		if(dto.getConnettoreNotifica()!= null) {
			dto.getConnettoreNotifica().setIdConnettore(dto.getCodApplicazione() + "_ESITO");
			vo.setCodConnettoreEsito(dto.getConnettoreNotifica().getIdConnettore());
		}

		if(dto.getConnettoreVerifica()!= null) {
			dto.getConnettoreVerifica().setIdConnettore(dto.getCodApplicazione() + "_VERIFICA");
			vo.setCodConnettoreVerifica(dto.getConnettoreVerifica().getIdConnettore());
		}
		
		vo.setFirmaRicevuta(dto.getFirmaRichiesta().getCodifica());
		vo.setPrincipal(dto.getPrincipal());
		vo.setTrusted(dto.isTrusted());
		
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
		
		if(dto.getIdDomini() != null && !dto.getIdDomini().isEmpty()) {
			List<ApplicazioneDominio> idDomini = new ArrayList<ApplicazioneDominio>();
			for(Long dominio: dto.getIdDomini()) {
				ApplicazioneDominio applicazioneDominio = new ApplicazioneDominio();
				IdDominio idDominio = new IdDominio();
				idDominio.setId(dominio);
				applicazioneDominio.setIdDominio(idDominio);
				idDomini.add(applicazioneDominio);
			}
			vo.setApplicazioneDominioList(idDomini);
		}
		
		return vo;
	}
}
