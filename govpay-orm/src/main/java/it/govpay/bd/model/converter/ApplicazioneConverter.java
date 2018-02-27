/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Acl;
import it.govpay.model.Applicazione;
import it.govpay.model.Connettore;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Versionabile.Versione;
import it.govpay.orm.IdUtenza;

public class ApplicazioneConverter {

	public static Applicazione toDTO(it.govpay.orm.Applicazione vo, Connettore connettoreNotifica, Connettore connettoreVerifica, List<Acl> acls) throws ServiceException {
		Applicazione dto = new Applicazione();
		dto.setAutoIuv(vo.getAutoIUV());
		dto.setAbilitato(vo.isAbilitato());
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setConnettoreNotifica(connettoreNotifica);
		dto.setConnettoreVerifica(connettoreVerifica);
		dto.setFirmaRichiesta(FirmaRichiesta.toEnum(vo.getFirmaRicevuta()));
		dto.setId(vo.getId());
		dto.setPrincipal(vo.getIdUtenza().getPrincipal());
		dto.setTrusted(vo.getTrusted());
		dto.setAcls(acls);
		dto.setVersione(Versione.toEnum(vo.getVersione()));
		dto.setCodApplicazioneIuv(vo.getCodApplicazioneIuv());
		dto.setRegExp(vo.getRegExp());
		return dto;
	}

	public static it.govpay.orm.Applicazione toVO(Applicazione dto) {
		it.govpay.orm.Applicazione vo = new it.govpay.orm.Applicazione();
		vo.setId(dto.getId());
		vo.setAutoIUV(dto.isAutoIuv());
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
		IdUtenza idUtenza = new IdUtenza();
		idUtenza.setPrincipal(dto.getPrincipal());
		vo.setIdUtenza(idUtenza);
		vo.setTrusted(dto.isTrusted());
		vo.setVersione(dto.getVersione().getLabel()); 
		vo.setCodApplicazioneIuv(dto.getCodApplicazioneIuv());
		vo.setRegExp(dto.getRegExp());
		return vo;
	}
}
