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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdSingoloVersamento;


public class PagamentoConverter {
	
	public static List<Pagamento> toDTO(List<it.govpay.orm.Pagamento> singoliPagamenti) throws ServiceException {
		List<Pagamento> dto = new ArrayList<Pagamento>();
		for(it.govpay.orm.Pagamento vo : singoliPagamenti) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
	
	public static Pagamento toDTO(it.govpay.orm.Pagamento vo) throws ServiceException {
		Pagamento dto = new Pagamento();
		
		dto.setId(vo.getId());
		//TODO GP-439.3 Bussu scommentare
		//dto.setCodDominio(vo.getCodDominio());
		//dto.setIuv(vo.getIuv());
		dto.setIur(vo.getIur());
		
		dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setDataAcquisizione(vo.getDataAcquisizione());
		dto.setDataPagamento(vo.getDataPagamento());
		if(vo.getCommissioniPsp() != null)
			dto.setCommissioniPsp(BigDecimal.valueOf(vo.getCommissioniPsp()));
		if(vo.getTipoAllegato() != null)
			dto.setTipoAllegato(TipoAllegato.valueOf(vo.getTipoAllegato()));
		dto.setAllegato(vo.getAllegato());
		dto.setIbanAccredito(vo.getIbanAccredito());
		
		if(vo.getIdRPT() != null) 
			dto.setIdRpt(vo.getIdRPT().getId());
		if(vo.getIdSingoloVersamento() != null)
			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		if(vo.getIdRr() != null)
			dto.setIdRr(vo.getIdRr().getId());
		
		dto.setDataAcquisizioneRevoca(vo.getDataAcquisizioneRevoca());
		dto.setCausaleRevoca(vo.getCausaleRevoca());
		dto.setDatiRevoca(vo.getDatiRevoca());
		dto.setEsitoRevoca(vo.getEsitoRevoca());
		dto.setDatiEsitoRevoca(vo.getDatiEsitoRevoca());
		if(vo.getImportoRevocato() != null)
			dto.setImportoRevocato(BigDecimal.valueOf(vo.getImportoRevocato()));
		
		return dto;
	}
	
	public static it.govpay.orm.Pagamento toVO(Pagamento dto) {
		it.govpay.orm.Pagamento vo = new it.govpay.orm.Pagamento();
		
		vo.setId(dto.getId());
		//TODO GP-439.3 Bussu scommentare
		//vo.setCodDominio(dto.getCodDominio());
		//vo.setIuv(dto.getIuv());
		vo.setIur(dto.getIur());
		
		if(dto.getImportoPagato() != null)
			vo.setImportoPagato(dto.getImportoPagato().doubleValue());
		vo.setDataAcquisizione(dto.getDataAcquisizione());
		vo.setDataPagamento(dto.getDataPagamento());
		if(dto.getCommissioniPsp() != null)
			vo.setCommissioniPsp(dto.getCommissioniPsp().doubleValue());
		if(dto.getTipoAllegato() != null)
			vo.setTipoAllegato(dto.getTipoAllegato().toString());
		vo.setAllegato(dto.getAllegato());
		vo.setIbanAccredito(dto.getIbanAccredito());
		
		if(dto.getIdRpt() != null) {
			IdRpt idRpt = new IdRpt();
			idRpt.setId(dto.getIdRpt());
			vo.setIdRPT(idRpt);
		}
		if(dto.getIdRr() != null) {
			IdRr idRr = new IdRr();
			idRr.setId(dto.getIdRr());
			vo.setIdRr(idRr);
		}
		if(dto.getIdSingoloVersamento() != null) {
			IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
			idSingoloVersamento.setId(dto.getIdSingoloVersamento());
			vo.setIdSingoloVersamento(idSingoloVersamento);
		}
		
		vo.setDataAcquisizioneRevoca(dto.getDataAcquisizioneRevoca());
		vo.setCausaleRevoca(dto.getCausaleRevoca());
		vo.setDatiRevoca(dto.getDatiRevoca());
		vo.setEsitoRevoca(dto.getEsitoRevoca());
		vo.setDatiEsitoRevoca(dto.getDatiEsitoRevoca());
		
		if(dto.getImportoRevocato() != null)
			vo.setImportoRevocato(dto.getImportoRevocato().doubleValue());
		
		return vo;
	}
	
}
