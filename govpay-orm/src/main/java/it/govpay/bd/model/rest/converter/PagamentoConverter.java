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
package it.govpay.bd.model.rest.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento.EsitoRendicontazione;
import it.govpay.bd.model.Pagamento.TipoAllegato;


public class PagamentoConverter extends it.govpay.bd.model.converter.PagamentoConverter  {
	
	public static it.govpay.bd.model.rest.Pagamento toRestDTO(it.govpay.orm.Pagamento vo,Map<String, Object> map) throws ServiceException {
		it.govpay.bd.model.rest.Pagamento dto = new it.govpay.bd.model.rest.Pagamento();
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

		String note = (String) getObjectFromMap(map, "idSingoloVersamento."+it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE.getFieldName());
		dto.setNote(note); 
		
		String codTributo = (String) getObjectFromMap(map, "idSingoloVersamento.idTributo."+it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.COD_TRIBUTO.getFieldName());
		dto.setCodTributo(codTributo);
		
		String iuv = (String) getObjectFromMap(map, "idRPT."+it.govpay.orm.Pagamento.model().ID_RPT.IUV.getFieldName());
		dto.setIuv(iuv);
		
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
