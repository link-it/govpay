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
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento;
import it.govpay.model.PagamentoExt;
import it.govpay.model.Pagamento.EsitoRendicontazione;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.orm.IdFrApplicazione;
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
		dto.setAllegato(vo.getAllegato());
		dto.setAnnoRiferimento(vo.getAnnoRiferimento());
		dto.setAnnoRiferimentoRevoca(vo.getAnnoRiferimentoRevoca());
		dto.setCausaleRevoca(vo.getCausaleRevoca());
		dto.setCodFlussoRendicontazione(vo.getCodflussoRendicontazione());
		dto.setCodFlussoRendicontazioneRevoca(vo.getCodFlussoRendicontazioneRevoca());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		if(vo.getCommissioniPsp() != null)
			dto.setCommissioniPsp(BigDecimal.valueOf(vo.getCommissioniPsp()));
		dto.setDataPagamento(vo.getDataPagamento());
		dto.setIbanAccredito(vo.getIbanAccredito());
		dto.setDataRendicontazione(vo.getRendicontazioneData());
		dto.setDataRendicontazioneRevoca(vo.getRendicontazioneDataRevoca());
		dto.setDatiEsitoRevoca(vo.getDatiEsitoRevoca());
		dto.setDatiRevoca(vo.getDatiRevoca());
		if(vo.getRendicontazioneEsito() != null)
			dto.setEsitoRendicontazione(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsito()));
		if(vo.getRendicontazioneEsitoRevoca() != null)
			dto.setEsitoRendicontazioneRevoca(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsitoRevoca()));
		dto.setEsitoRevoca(vo.getEsitoRevoca());
		dto.setId(vo.getId());
		if(vo.getIdFrApplicazione() != null)
			dto.setIdFrApplicazione(vo.getIdFrApplicazione().getId());
		if(vo.getIdFrApplicazioneRevoca() != null)
			dto.setIdFrApplicazioneRevoca(vo.getIdFrApplicazioneRevoca().getId());
		dto.setIdRpt(vo.getIdRPT().getId());
		if(vo.getIdRr() != null)
			dto.setIdRr(vo.getIdRr().getId());
		dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setIndice(vo.getIndiceSingoloPagamento());
		dto.setIndiceRevoca(vo.getIndiceSingoloPagamentoRevoca());
		if(vo.getImportoRevocato() != null)
			dto.setImportoRevocato(BigDecimal.valueOf(vo.getImportoRevocato()));
		dto.setIur(vo.getIur());
		if(vo.getRendicontazioneEsito() != null)
			dto.setEsitoRendicontazione(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsito()));
		if(vo.getTipoAllegato() != null)
			dto.setTipoAllegato(TipoAllegato.valueOf(vo.getTipoAllegato()));
		
		dto.setDataAcquisizione(vo.getDataAcquisizione());
		dto.setDataAcquisizioneRevoca(vo.getDataAcquisizioneRevoca());
		return dto;
	}

	public static it.govpay.orm.Pagamento toVO(Pagamento dto) {
		it.govpay.orm.Pagamento vo = new it.govpay.orm.Pagamento();
		vo.setAllegato(dto.getAllegato());
		vo.setAnnoRiferimento(dto.getAnnoRiferimento());
		vo.setAnnoRiferimentoRevoca(dto.getAnnoRiferimentoRevoca());
		vo.setCausaleRevoca(dto.getCausaleRevoca());
		vo.setCodflussoRendicontazione(dto.getCodFlussoRendicontazione());
		vo.setCodFlussoRendicontazioneRevoca(dto.getCodFlussoRendicontazioneRevoca());
		vo.setCodSingoloVersamentoEnte(dto.getCodSingoloVersamentoEnte());
		if(dto.getCommissioniPsp() != null)
			vo.setCommissioniPsp(dto.getCommissioniPsp().doubleValue());
		vo.setDataPagamento(dto.getDataPagamento());
		vo.setIbanAccredito(dto.getIbanAccredito());
		vo.setDatiEsitoRevoca(dto.getDatiEsitoRevoca());
		vo.setDatiRevoca(dto.getDatiRevoca());
		vo.setEsitoRevoca(dto.getEsitoRevoca());
		vo.setId(dto.getId());
		if(dto.getIdFrApplicazione() != null) {
			IdFrApplicazione idFrApplicazione = new IdFrApplicazione();
			idFrApplicazione.setId(dto.getIdFrApplicazione());
			vo.setIdFrApplicazione(idFrApplicazione);
		}
		if(dto.getIdFrApplicazioneRevoca() != null) {
			IdFrApplicazione idFrApplicazioneRevoca = new IdFrApplicazione();
			idFrApplicazioneRevoca.setId(dto.getIdFrApplicazioneRevoca());
			vo.setIdFrApplicazioneRevoca(idFrApplicazioneRevoca);
		}
		IdRpt idRpt = new IdRpt();
		idRpt.setId(dto.getIdRpt());
		vo.setIdRPT(idRpt);
		if(dto.getIdRr() != null) {
			IdRr idRr = new IdRr();
			idRr.setId(dto.getIdRr());
			vo.setIdRr(idRr);
		}
		IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
		idSingoloVersamento.setId(dto.getIdSingoloVersamento());
		vo.setIdSingoloVersamento(idSingoloVersamento);
		if(dto.getImportoPagato() != null)
			vo.setImportoPagato(dto.getImportoPagato().doubleValue());
		if(dto.getImportoRevocato() != null)
			vo.setImportoRevocato(dto.getImportoRevocato().doubleValue());
		vo.setIndiceSingoloPagamento(dto.getIndice());
		vo.setIndiceSingoloPagamentoRevoca(dto.getIndiceRevoca());
		vo.setIur(dto.getIur());
		vo.setRendicontazioneData(dto.getDataRendicontazione());
		vo.setRendicontazioneDataRevoca(dto.getDataRendicontazioneRevoca());
		if(dto.getEsitoRendicontazione() != null)
			vo.setRendicontazioneEsito(dto.getEsitoRendicontazione().getCodifica());
		if(dto.getEsitoRendicontazioneRevoca() != null)
			vo.setRendicontazioneEsitoRevoca(dto.getEsitoRendicontazioneRevoca().getCodifica());
		if(dto.getTipoAllegato() != null)
			vo.setTipoAllegato(dto.getTipoAllegato().toString());
		
		vo.setDataAcquisizione(dto.getDataAcquisizione());
		vo.setDataAcquisizioneRevoca(dto.getDataAcquisizioneRevoca());
		return vo;
	}
	
	public static PagamentoExt toDTOExt(it.govpay.orm.Pagamento vo,Map<String, Object> map) throws ServiceException {
		PagamentoExt dto = new PagamentoExt();
		dto.setAllegato(vo.getAllegato());
		dto.setAnnoRiferimento(vo.getAnnoRiferimento());
		dto.setAnnoRiferimentoRevoca(vo.getAnnoRiferimentoRevoca());
		dto.setCausaleRevoca(vo.getCausaleRevoca());
		dto.setCodFlussoRendicontazione(vo.getCodflussoRendicontazione());
		dto.setCodFlussoRendicontazioneRevoca(vo.getCodFlussoRendicontazioneRevoca());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		if(vo.getCommissioniPsp() != null)
			dto.setCommissioniPsp(BigDecimal.valueOf(vo.getCommissioniPsp()));
		dto.setDataPagamento(vo.getDataPagamento());
		dto.setDataRendicontazione(vo.getRendicontazioneData());
		dto.setDataRendicontazioneRevoca(vo.getRendicontazioneDataRevoca());
		dto.setDatiEsitoRevoca(vo.getDatiEsitoRevoca());
		dto.setDatiRevoca(vo.getDatiRevoca());
		if(vo.getRendicontazioneEsito() != null)
			dto.setEsitoRendicontazione(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsito()));
		if(vo.getRendicontazioneEsitoRevoca() != null)
			dto.setEsitoRendicontazioneRevoca(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsitoRevoca()));
		dto.setEsitoRevoca(vo.getEsitoRevoca());
		dto.setId(vo.getId());
		if(vo.getIdFrApplicazione() != null)
			dto.setIdFrApplicazione(vo.getIdFrApplicazione().getId());
		if(vo.getIdFrApplicazioneRevoca() != null)
			dto.setIdFrApplicazioneRevoca(vo.getIdFrApplicazioneRevoca().getId());
		if(vo.getIdRPT() != null)
			dto.setIdRpt(vo.getIdRPT().getId());
		if(vo.getIdRr() != null)
			dto.setIdRr(vo.getIdRr().getId());
		if(vo.getIdSingoloVersamento() != null)
			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		dto.setIndice(vo.getIndiceSingoloPagamento());
		dto.setIndiceRevoca(vo.getIndiceSingoloPagamentoRevoca());
		if(vo.getImportoRevocato() != null)
			dto.setImportoRevocato(BigDecimal.valueOf(vo.getImportoRevocato()));
		dto.setIur(vo.getIur());
		if(vo.getRendicontazioneEsito() != null)
			dto.setEsitoRendicontazione(EsitoRendicontazione.toEnum(vo.getRendicontazioneEsito()));
		if(vo.getTipoAllegato() != null)
			dto.setTipoAllegato(TipoAllegato.valueOf(vo.getTipoAllegato()));
		
		String codSingoloVersamentoEnte = (String) getObjectFromMap(map, "idSingoloVersamento."+it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldName());
		dto.setCodSingoloVersamentoEnte(codSingoloVersamentoEnte);
		
//		String note = (String) getObjectFromMap(map, "idSingoloVersamento."+it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE.getFieldName());
//		dto.setNote(note); 
		
		String codTributo = (String) getObjectFromMap(map, "idSingoloVersamento.idTributo."+it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO.COD_TRIBUTO.getFieldName());
		dto.setCodTributo(codTributo);
		
		return dto;
	}
	
	public static Object getObjectFromMap(Map<String,Object> map,String name){
		if(map==null){
			return null;
		}
		else if(map.containsKey(name)){
			Object o = map.get(name);
			if(o instanceof org.apache.commons.lang.ObjectUtils.Null){
				return null;
			}
			else{
				return o;
			}
		}
		else{
			return null;
		}
	}
}
