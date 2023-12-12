/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.bd.viste.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.model.Versamento.TipoSogliaVersamento;

public class EntrataPrevistaConverter {

	public static List<EntrataPrevista> toDTOList(List<it.govpay.orm.VistaRiscossioni> listaVistaRiscossioni) throws UnsupportedEncodingException {
		List<EntrataPrevista> lstDTO = new ArrayList<>();
		if(listaVistaRiscossioni != null && !listaVistaRiscossioni.isEmpty()) {
			for(it.govpay.orm.VistaRiscossioni riscossione: listaVistaRiscossioni){
				lstDTO.add(toDTO(riscossione));
			}
		}
		return lstDTO;
	}

	public static EntrataPrevista toDTO(it.govpay.orm.VistaRiscossioni vo) throws UnsupportedEncodingException {
		EntrataPrevista dto = new EntrataPrevista();
		dto.setCodApplicazione(vo.getCodApplicazione());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodFlusso(vo.getCodFlusso());
		dto.setCodSingoloVersamentoEnte(vo.getCodSingoloVersamentoEnte());
		dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
		//		dto.setData(vo.getData());
		dto.setDataRegolamento(vo.getDataRegolamento());
		dto.setFrIur(vo.getFrIur());
		if(vo.getImportoPagato()!= null)
			dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		if(vo.getImportoTotalePagamenti()!= null)	
			dto.setImportoTotalePagamenti(BigDecimal.valueOf(vo.getImportoTotalePagamenti()));
		dto.setIndiceDati(vo.getIndiceDati());
		dto.setIur(vo.getIur());
		dto.setIuv(vo.getIuv());
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());
		dto.setDataPagamento(vo.getDataPagamento());
		dto.setCodTipoVersamento(vo.getCodTipoVersamento());
		dto.setCodEntrata(vo.getCodEntrata());
		dto.setIdentificativoDebitore(vo.getIdentificativoDebitore());
		dto.setAnno(vo.getAnno());

		dto.setDescrizioneTipoVersamento(vo.getDescrTipoVersamento());
		dto.setImportoVersamento(BigDecimal.valueOf(vo.getImportoVersamento()));
		dto.setAnagraficaDebitore(vo.getDebitoreAnagrafica());
		dto.setDataScadenza(vo.getDataScadenza());
		dto.setNumeroAvviso(vo.getNumeroAvviso());
		dto.setIuvPagamento(vo.getIuvPagamento());
		dto.setCausaleVersamento(vo.getCausaleVersamento());
		dto.setDataCreazione(vo.getDataCreazione());

		if(vo.getIdDocumento() != null)
			dto.setIdDocumento(vo.getIdDocumento().getId());
		if(vo.getCodRata() != null) {
			if(vo.getCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
				dto.setTipoSoglia(TipoSogliaVersamento.ENTRO);
				String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
				dto.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
				dto.setTipoSoglia(TipoSogliaVersamento.OLTRE);
				String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
				dto.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.RIDOTTO.toString())) {
				dto.setTipoSoglia(TipoSogliaVersamento.RIDOTTO);
				//					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.RIDOTTO.toString())+ TipoSogliaVersamento.RIDOTTO.toString().length());
				//					dto.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.SCONTATO.toString())) {
				dto.setTipoSoglia(TipoSogliaVersamento.SCONTATO);
				//					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.SCONTATO.toString())+ TipoSogliaVersamento.SCONTATO.toString().length());
				//					dto.setGiorniSoglia(Integer.parseInt(gg));
			} else {
				dto.setNumeroRata(Integer.parseInt(vo.getCodRata()));
			}
		}

		dto.setContabilita(vo.getContabilita());

		return dto;
	}
}
