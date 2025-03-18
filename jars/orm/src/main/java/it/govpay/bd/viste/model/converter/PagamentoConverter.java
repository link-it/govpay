/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.model.Pagamento;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.model.Pagamento.TipoPagamento;

public class PagamentoConverter {
	
	private PagamentoConverter() {}

	public static Pagamento toDTO(it.govpay.orm.VistaPagamento vo) {
		Pagamento dto = new Pagamento();
		dto.setId(vo.getId());

		it.govpay.bd.model.Pagamento pagamento = new it.govpay.bd.model.Pagamento();

		pagamento.setId(vo.getId());
		pagamento.setCodDominio(vo.getCodDominio());
		pagamento.setIuv(vo.getIuv());
		pagamento.setIur(vo.getIur());
		pagamento.setIndiceDati(vo.getIndiceDati());
		pagamento.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		pagamento.setDataAcquisizione(vo.getDataAcquisizione());
		pagamento.setDataPagamento(vo.getDataPagamento());
		pagamento.setCommissioniPsp(vo.getCommissioniPsp());
		if(vo.getTipoAllegato() != null)
			pagamento.setTipoAllegato(TipoAllegato.valueOf(vo.getTipoAllegato()));
		pagamento.setAllegato(vo.getAllegato());
		if(vo.getIdRPT() != null) 
			pagamento.setIdRpt(vo.getIdRPT().getId());
		if(vo.getIdSingoloVersamento() != null)
			pagamento.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());

		pagamento.setDataAcquisizioneRevoca(vo.getDataAcquisizioneRevoca());
		pagamento.setCausaleRevoca(vo.getCausaleRevoca());
		pagamento.setDatiRevoca(vo.getDatiRevoca());
		pagamento.setEsitoRevoca(vo.getEsitoRevoca());
		pagamento.setDatiEsitoRevoca(vo.getDatiEsitoRevoca());
		pagamento.setImportoRevocato(vo.getImportoRevocato());
		if(vo.getStato() != null)
			pagamento.setStato(Stato.valueOf(vo.getStato()));

		if(vo.getIdIncasso() != null)
			pagamento.setIdIncasso(vo.getIdIncasso().getId());
		
		pagamento.setTipo(TipoPagamento.valueOf(vo.getTipo()));
		
		dto.setPagamento(pagamento);
		
		// Rpt
		
		if(vo.getRptIuv() != null) {
			Rpt rpt = new Rpt();
			rpt.setIuv(vo.getRptIuv());
			rpt.setCodDominio(vo.getCodDominio());
			rpt.setCcp(vo.getRptCcp());
			dto.setRpt(rpt);
		}
		
		// Incasso
		
		if(vo.getRncTrn() != null) {
			Incasso incasso = new Incasso();
			incasso.setCodDominio(vo.getCodDominio());
			incasso.setTrn(vo.getRncTrn());
			dto.setIncasso(incasso );
		}

		// SV

		SingoloVersamento singoloVersamento = new SingoloVersamento();

		singoloVersamento.setCodSingoloVersamentoEnte(vo.getSngCodSingVersEnte());

		dto.setSingoloVersamento(singoloVersamento);
		
		// V

		Versamento versamento = new Versamento();

		versamento.setId(vo.getId());
		versamento.setIdApplicazione(vo.getVrsIdApplicazione().getId());

		if(vo.getVrsIdUo() != null)
			versamento.setIdUo(vo.getVrsIdUo().getId());

		if(vo.getVrsIdDominio() != null)
			versamento.setIdDominio(vo.getVrsIdDominio().getId());

		if(vo.getVrsIdTipoVersamento() != null)
			versamento.setIdTipoVersamento(vo.getVrsIdTipoVersamento().getId());

		if(vo.getVrsIdTipoVersamentoDominio() != null)
			versamento.setIdTipoVersamentoDominio(vo.getVrsIdTipoVersamentoDominio().getId());
		versamento.setCodVersamentoEnte(vo.getVrsCodVersamentoEnte());
		versamento.setTassonomia(vo.getVrsTassonomia());
		versamento.setDirezione(vo.getVrsDirezione());
		versamento.setDivisione(vo.getVrsDivisione());
		
		if(vo.getVrsIdDocumento() != null)
			versamento.setIdDocumento(vo.getVrsIdDocumento().getId());

		dto.setVersamento(versamento );

		return dto;
	}



	public static List<Pagamento> toDTO(
			List<it.govpay.orm.VistaPagamento> pagamentoVOLst) {
		List<Pagamento> dto = new ArrayList<>();
		for(it.govpay.orm.VistaPagamento vo : pagamentoVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
