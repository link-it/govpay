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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdStazione;

public class DominioConverter {

//	public static List<Dominio> toDTOList(List<it.govpay.orm.Dominio> anagraficaLst, BDConfigWrapper configWrapper) throws ServiceException {
//		List<Dominio> lstDTO = new ArrayList<>();
//		if(anagraficaLst != null && !anagraficaLst.isEmpty()) {
//			for(it.govpay.orm.Dominio anagrafica: anagraficaLst){
//				lstDTO.add(toDTO(anagrafica, configWrapper));
//			}
//		}
//		return lstDTO;
//	}

	public static Dominio toDTO(it.govpay.orm.Dominio vo, BDConfigWrapper configWrapper, ConnettoreNotificaPagamenti connettoreMyPivot, 
			ConnettoreNotificaPagamenti connettoreSecim, ConnettoreNotificaPagamenti connettoreGovPay, 
			ConnettoreNotificaPagamenti connettoreHyperSicAPKappa, ConnettoreNotificaPagamenti connettoreMaggioliJPPA) throws ServiceException {
		
		
		IdStazione idStazione = vo.getIdStazione(); 
		Long idStazioneLong = idStazione != null ? idStazione.getId() : null;
		Dominio dto = new Dominio(configWrapper, vo.getId(), idStazioneLong);
		if(vo.getIdApplicazioneDefault() != null) {
			dto.setIdApplicazioneDefault(vo.getIdApplicazioneDefault().getId());
		}
		dto.setCodDominio(vo.getCodDominio());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setGln(vo.getGln());
		dto.setAbilitato(vo.isAbilitato());
		dto.setAuxDigit(vo.getAuxDigit());
		dto.setIuvPrefix(vo.getIuvPrefix()); 
		dto.setSegregationCode(vo.getSegregationCode());
		dto.setLogo(vo.getLogo());
		dto.setCbill(vo.getCbill());
		dto.setAutStampaPoste(vo.getAutStampaPoste());
		dto.setConnettoreMyPivot(connettoreMyPivot);
		dto.setConnettoreSecim(connettoreSecim);
		dto.setConnettoreGovPay(connettoreGovPay);
		dto.setConnettoreHyperSicAPKappa(connettoreHyperSicAPKappa);
		dto.setConnettoreMaggioliJPPA(connettoreMaggioliJPPA);
		dto.setIntermediato(vo.isIntermediato());
		return dto;
	}

	public static it.govpay.orm.Dominio toVO(Dominio dto) throws ServiceException {
		it.govpay.orm.Dominio vo = new it.govpay.orm.Dominio();
		vo.setId(dto.getId());
		if(dto.getIdApplicazioneDefault() != null) {
			IdApplicazione idApplicazioneDefault = new IdApplicazione();
			idApplicazioneDefault.setId(dto.getIdApplicazioneDefault());
			vo.setIdApplicazioneDefault(idApplicazioneDefault);
		}
		vo.setCodDominio(dto.getCodDominio());
		vo.setRagioneSociale(dto.getRagioneSociale()); 
		vo.setGln(dto.getGln());
		vo.setAbilitato(dto.isAbilitato());
		vo.setAuxDigit(dto.getAuxDigit());
		vo.setIuvPrefix(dto.getIuvPrefix());
		
		if(dto.getIdStazione() != null) {
			IdStazione idStazione = new IdStazione();
			idStazione.setId(dto.getIdStazione());
			vo.setIdStazione(idStazione);
		}
		
		vo.setSegregationCode(dto.getSegregationCode());
		vo.setLogo(dto.getLogo());
		vo.setCbill(dto.getCbill());
		vo.setAutStampaPoste(dto.getAutStampaPoste());
		
		if(dto.getConnettoreMyPivot()!= null) {
			dto.getConnettoreMyPivot().setIdConnettore(DominiBD.getIDConnettoreMyPivot(dto.getCodDominio()));
			vo.setCodConnettoreMyPivot(dto.getConnettoreMyPivot().getIdConnettore());
		}
		
		if(dto.getConnettoreSecim()!= null) {
			dto.getConnettoreSecim().setIdConnettore(DominiBD.getIDConnettoreSecim(dto.getCodDominio()));
			vo.setCodConnettoreSecim(dto.getConnettoreSecim().getIdConnettore());
		}
		
		if(dto.getConnettoreGovPay()!= null) {
			dto.getConnettoreGovPay().setIdConnettore(DominiBD.getIDConnettoreGovPay(dto.getCodDominio()));
			vo.setCodConnettoreGovPay(dto.getConnettoreGovPay().getIdConnettore());
		}
		
		if(dto.getConnettoreHyperSicAPKappa()!= null) {
			dto.getConnettoreHyperSicAPKappa().setIdConnettore(DominiBD.getIDConnettoreHyperSicAPKappa(dto.getCodDominio()));
			vo.setCodConnettoreHyperSicAPK(dto.getConnettoreHyperSicAPKappa().getIdConnettore());
		}
		
		if(dto.getConnettoreMaggioliJPPA()!= null) {
			dto.getConnettoreMaggioliJPPA().setIdConnettore(DominiBD.getIDConnettoreMaggioliJPPA(dto.getCodDominio()));
			vo.setCodConnettoreMaggioliJPPA(dto.getConnettoreMaggioliJPPA().getIdConnettore());
		}
		
		vo.setIntermediato(dto.isIntermediato());
		
		return vo;
	}

}
