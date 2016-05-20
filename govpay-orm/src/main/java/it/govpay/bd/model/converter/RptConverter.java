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

import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Canale.ModelloPagamento;
import it.govpay.bd.model.Rpt.EsitoPagamento;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.orm.IdCanale;
import it.govpay.orm.IdPortale;
import it.govpay.orm.IdVersamento;

import java.math.BigDecimal;
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
		dto.setCallbackURL(vo.getCallbackURL());
		dto.setCcp(vo.getCcp());
		dto.setCodCarrello(vo.getCodCarrello());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodMsgRichiesta(vo.getCodMsgRichiesta());
		dto.setCodMsgRicevuta(vo.getCodMsgRicevuta());
		dto.setCodSessione(vo.getCodSessione());
		dto.setCodStazione(vo.getCodStazione());
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataMsgRichiesta(vo.getDataMsgRichiesta());
		dto.setDataMsgRicevuta(vo.getDataMsgRicevuta());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		if(vo.getCodEsitoPagamento() != null)
			dto.setEsitoPagamento(EsitoPagamento.toEnum(vo.getCodEsitoPagamento()));
		dto.setFirmaRichiesta(FirmaRichiesta.toEnum(vo.getFirmaRicevuta()));
		dto.setId(vo.getId());
		dto.setIdCanale(vo.getIdCanale().getId());
		if(vo.getIdPortale() != null)
			dto.setIdPortale(vo.getIdPortale().getId());
		dto.setIdVersamento(vo.getIdVersamento().getId());
		if(vo.getImportoTotalePagato() != null)
			dto.setImportoTotalePagato(BigDecimal.valueOf(vo.getImportoTotalePagato()));
		dto.setIuv(vo.getIuv());
		dto.setModelloPagamento(ModelloPagamento.toEnum(Integer.parseInt(vo.getModelloPagamento())));
		dto.setPspRedirectURL(vo.getPspRedirectURL());
		dto.setStato(StatoRpt.valueOf(vo.getStato()));
		dto.setXmlRpt(vo.getXmlRPT());
		dto.setXmlRt(vo.getXmlRT());
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
		vo.setCodStazione(dto.getCodStazione());
		vo.setDataAggiornamentoStato(dto.getDataAggiornamento());
		vo.setDataMsgRichiesta(dto.getDataMsgRichiesta());
		vo.setDataMsgRicevuta(dto.getDataMsgRicevuta());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setFirmaRicevuta(dto.getFirmaRichiesta().getCodifica());
		vo.setId(dto.getId());
		IdCanale idCanale = new IdCanale();
		idCanale.setId(dto.getIdCanale());
		vo.setIdCanale(idCanale);
		if(dto.getIdPortale() != null) {
			IdPortale idPortale = new IdPortale();
			idPortale.setId(dto.getIdPortale());
			vo.setIdPortale(idPortale);
		}
		IdVersamento idVersamento = new IdVersamento();
		idVersamento.setId(dto.getIdVersamento());
		vo.setIdVersamento(idVersamento);
		if(dto.getImportoTotalePagato() != null)
			vo.setImportoTotalePagato(dto.getImportoTotalePagato().doubleValue());
		vo.setIuv(dto.getIuv());
		vo.setModelloPagamento(Integer.toString(dto.getModelloPagamento().getCodifica()));
		vo.setPspRedirectURL(dto.getCallbackURL());
		vo.setStato(dto.getStato().toString());
		vo.setXmlRPT(dto.getXmlRpt());
		vo.setXmlRT(dto.getXmlRt());
		return vo;
	}

}
