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

import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FaultNodo;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.orm.IdCanale;
import it.govpay.orm.IdPortale;
import it.govpay.orm.IdPsp;
import it.govpay.orm.IdStazione;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.IdVersamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RptConverter {

	public static List<Rpt> toDTOList(List<it.govpay.orm.RPT> applicazioneLst) throws ServiceException {
		List<Rpt> lstDTO = new ArrayList<Rpt>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.RPT applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Rpt toDTO(it.govpay.orm.RPT vo) throws ServiceException {
		Rpt dto = new Rpt();
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setCodDominio(vo.getCodDominio());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		if(vo.getIdTracciatoXML() != null)
			dto.setIdTracciatoXML(vo.getIdTracciatoXML().getId());
		
		if(vo.getIdPsp() != null)
			dto.setIdPsp(vo.getIdPsp().getId());
		
		if(vo.getIdCanale() != null)
			dto.setIdCanale(vo.getIdCanale().getId());
		
		if(vo.getIdPortale() != null)
			dto.setIdPortale(vo.getIdPortale().getId());
		
		if(vo.getIdStazione() != null)
			dto.setIdStazione(vo.getIdStazione().getId());
		
		dto.setCodCarrello(vo.getCodCarrello());
		dto.setCcp(vo.getCcp());
		dto.setCodSessione(vo.getCodSessione());
		dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		dto.setDataOraMsgRichiesta(vo.getDataOraMsgRichiesta());
		dto.setDataOraCreazione(vo.getDataOraCreazione());
		dto.setCodMsgRichiesta(vo.getCodMsgRichiesta());
		dto.setIbanAddebito(vo.getIbanAddebito());
		dto.setAutenticazioneSoggetto(Autenticazione.toEnum(vo.getAutenticazioneSoggetto()));
		dto.setFirmaRichiesta(FirmaRichiesta.toEnum(vo.getFirmaRT()));
		dto.setStatoRpt(StatoRpt.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		if(vo.getCodFault() != null)
			dto.setFaultCode(FaultNodo.valueOf(vo.getCodFault()));
		dto.setCallbackURL(vo.getCallbackURL());

		return dto;
	}

	public static it.govpay.orm.RPT toVO(Rpt dto) {
		it.govpay.orm.RPT vo = new it.govpay.orm.RPT();
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setCodDominio(dto.getCodDominio());
		
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		
		if(dto.getIdTracciatoXML() != null) {
			IdTracciato idTracciato = new IdTracciato();
			idTracciato.setId(dto.getIdTracciatoXML());
			vo.setIdTracciatoXML(idTracciato);
		}
		
		
		
		IdPsp idPsp = new IdPsp();
		idPsp.setId(dto.getIdPsp());
		vo.setIdPsp(idPsp);
		
		if(dto.getIdPortale() != null) {
			IdPortale idPortale = new IdPortale();
			idPortale.setId(dto.getIdPortale());
			vo.setIdPortale(idPortale);
		}
		
		
		IdStazione idStazione = new IdStazione();
		idStazione.setId(dto.getIdStazione());
		vo.setIdStazione(idStazione);
		
		vo.setCodCarrello(dto.getCodCarrello());
		vo.setCcp(dto.getCcp());
		vo.setCodSessione(dto.getCodSessione());
		if(dto.getTipoVersamento() != null) {
			vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		} else {
			vo.setTipoVersamento(null);
		}
		vo.setDataOraMsgRichiesta(dto.getDataOraMsgRichiesta());
		vo.setDataOraCreazione(dto.getDataOraCreazione());
		vo.setCodMsgRichiesta(dto.getCodMsgRichiesta());
		vo.setIbanAddebito(dto.getIbanAddebito());
		if(dto.getAutenticazioneSoggetto() != null) {
			vo.setAutenticazioneSoggetto(dto.getAutenticazioneSoggetto().getCodifica());
		} else {
			vo.setAutenticazioneSoggetto(null);
		}
		if(dto.getFirmaRichiesta() != null) {
			vo.setFirmaRT(dto.getFirmaRichiesta().getCodifica());
		} else {
			vo.setFirmaRT(null);
		}
		if(dto.getStatoRpt() != null) {
			vo.setStato(dto.getStatoRpt().name());
		} else {
			vo.setStato(null);
		}
		
		if(dto.getIdCanale() != null) {
			IdCanale idCanale = new IdCanale();
			idCanale.setId(dto.getIdCanale());
			vo.setIdCanale(idCanale);
		}
		
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		if(dto.getFaultCode() != null)
			vo.setCodFault(dto.getFaultCode().name());
		else
			vo.setCodFault(null);
		vo.setCallbackURL(dto.getCallbackURL());

		return vo;
	}

}
