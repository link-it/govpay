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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Rpt;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdTracciatoNotificaPagamenti;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.orm.IdVersamento;

public class RptConverter {

	public static List<Rpt> toDTOList(List<it.govpay.orm.RPT> applicazioneLst) throws ServiceException {
		List<Rpt> lstDTO = new ArrayList<>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.RPT applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Rpt toDTO(it.govpay.orm.RPT vo) throws ServiceException {
		Rpt dto = new Rpt();
		dto.setCallbackURL(vo.getCallbackURL());
		dto.setCcp(vo.getCcp());
		dto.setCodCarrello(vo.getCodCarrello());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodMsgRichiesta(vo.getCodMsgRichiesta());
		dto.setCodMsgRicevuta(vo.getCodMsgRicevuta());
		dto.setCodSessione(vo.getCodSessione());
		dto.setCodSessionePortale(vo.getCodSessionePortale());
		dto.setCodStazione(vo.getCodStazione());
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataMsgRichiesta(vo.getDataMsgRichiesta());
		dto.setDataMsgRicevuta(vo.getDataMsgRicevuta());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		if(vo.getCodEsitoPagamento() != null)
			dto.setEsitoPagamento(EsitoPagamento.toEnum(vo.getCodEsitoPagamento()));
		dto.setId(vo.getId());
		dto.setIdTransazioneRpt(vo.getCodTransazioneRPT());
		dto.setIdTransazioneRt(vo.getCodTransazioneRT());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		if(vo.getImportoTotalePagato() != null)
			dto.setImportoTotalePagato(BigDecimal.valueOf(vo.getImportoTotalePagato()));
		dto.setIuv(vo.getIuv());
		dto.setModelloPagamento(ModelloPagamento.toEnum(Integer.parseInt(vo.getModelloPagamento())));
		dto.setPspRedirectURL(vo.getPspRedirectURL());
		dto.setStato(StatoRpt.toEnum(vo.getStato()));
		dto.setXmlRpt(vo.getXmlRPT());
		dto.setXmlRt(vo.getXmlRT());
		
		dto.setDataConservazione(vo.getDataConservazione());
		
		if(vo.getStatoConservazione() != null)
			dto.setStatoConservazione(it.govpay.model.Rpt.StatoConservazione.valueOf(vo.getStatoConservazione()));
		
		dto.setDescrizioneStatoConservazione(vo.getDescrizioneStatoCons());
		
		if(vo.getIdPagamentoPortale() != null)
			dto.setIdPagamentoPortale(vo.getIdPagamentoPortale().getId());

		dto.setCodCanale(vo.getCodCanale());
		dto.setCodIntermediarioPsp(vo.getCodIntermediarioPsp());
		dto.setCodPsp(vo.getCodPsp());
		if(vo.getTipoVersamento() != null)
			dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		if(vo.getTipoIdentificativoAttestante() != null)
			dto.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.valueOf(vo.getTipoIdentificativoAttestante()));
		dto.setIdentificativoAttestante(vo.getIdentificativoAttestante());
		dto.setDenominazioneAttestante(vo.getDenominazioneAttestante());
		dto.setBloccante(vo.isBloccante()); 
		
		if(vo.getIdTracciatoMyPivot() != null) { 
			dto.setIdTracciatoMyPivot(vo.getIdTracciatoMyPivot().getId());
		}
		if(vo.getIdTracciatoSecim() != null) { 
			dto.setIdTracciatoSecim(vo.getIdTracciatoSecim().getId());
		}
		
		return dto;
	}

	public static it.govpay.orm.RPT toVO(Rpt dto) {
		it.govpay.orm.RPT vo = new it.govpay.orm.RPT();
		vo.setCallbackURL(dto.getCallbackURL());
		vo.setCcp(dto.getCcp());
		vo.setCodCarrello(dto.getCodCarrello());
		vo.setCodDominio(dto.getCodDominio());
		if(dto.getEsitoPagamento() != null)
			vo.setCodEsitoPagamento(dto.getEsitoPagamento().getCodifica());
		vo.setCodMsgRichiesta(dto.getCodMsgRichiesta());
		vo.setCodMsgRicevuta(dto.getCodMsgRicevuta());
		vo.setCodSessione(dto.getCodSessione());
		vo.setCodSessionePortale(dto.getCodSessionePortale());
		vo.setCodStazione(dto.getCodStazione());
		vo.setCodTransazioneRPT(dto.getIdTransazioneRpt());
		vo.setCodTransazioneRT(dto.getIdTransazioneRt());
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataMsgRichiesta(dto.getDataMsgRichiesta());
		vo.setDataMsgRicevuta(dto.getDataMsgRicevuta());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		if(dto.getImportoTotalePagato() != null)
			vo.setImportoTotalePagato(dto.getImportoTotalePagato().doubleValue());
		vo.setIuv(dto.getIuv());
		
		if(dto.getModelloPagamento() != null)
			vo.setModelloPagamento(Integer.toString(dto.getModelloPagamento().getCodifica()));
		
		vo.setPspRedirectURL(dto.getCallbackURL());
		vo.setStato(dto.getStato().toString());
		vo.setXmlRPT(dto.getXmlRpt());
		vo.setXmlRT(dto.getXmlRt());
		
		vo.setDataConservazione(dto.getDataConservazione());
		
		if(dto.getStatoConservazione() != null)
			vo.setStatoConservazione(dto.getStatoConservazione().name());
		
		vo.setDescrizioneStatoCons(dto.getDescrizioneStatoConservazione());
		
		if(dto.getIdPagamentoPortale() != null) {
			IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
			idPagamentoPortale.setId(dto.getIdPagamentoPortale());
			vo.setIdPagamentoPortale(idPagamentoPortale );
		}
		

		vo.setCodCanale(dto.getCodCanale());
		vo.setCodIntermediarioPsp(dto.getCodIntermediarioPsp());
		vo.setCodPsp(dto.getCodPsp());
		if(dto.getTipoVersamento() != null)
			vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		if(dto.getTipoIdentificativoAttestante() != null)
			vo.setTipoIdentificativoAttestante(dto.getTipoIdentificativoAttestante().name());
		vo.setIdentificativoAttestante(dto.getIdentificativoAttestante());
		vo.setDenominazioneAttestante(dto.getDenominazioneAttestante());
		vo.setBloccante(dto.isBloccante());
		
		if(dto.getIdTracciatoMyPivot() != null) {
			IdTracciatoNotificaPagamenti idTracciatoMyPivot = new IdTracciatoNotificaPagamenti();
			idTracciatoMyPivot.setId(dto.getIdTracciatoMyPivot());
			vo.setIdTracciatoMyPivot(idTracciatoMyPivot );
		}
		
		if(dto.getIdTracciatoSecim() != null) {
			IdTracciatoNotificaPagamenti idTracciatoSecim = new IdTracciatoNotificaPagamenti();
			idTracciatoSecim.setId(dto.getIdTracciatoSecim());
			vo.setIdTracciatoSecim(idTracciatoSecim );
		}
		
		return vo;
	}

}
