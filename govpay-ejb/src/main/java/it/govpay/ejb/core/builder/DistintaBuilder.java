/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.builder;

import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.model.CondizionePagamentoModel;
import it.govpay.ejb.model.CondizionePagamentoModel.EnumTipoPagamento;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.model.DistintaModel.EnumTipoAutenticazioneSoggetto;
import it.govpay.ejb.model.DistintaModel.EnumTipoFirma;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.PagamentoModel;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.PendenzaModel.EnumModalitaPagamento;
import it.govpay.ejb.model.PendenzaModel.EnumStatoPendenza;
import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.utils.DataTypeUtils;
import it.govpay.ejb.utils.EnumUtils;
import it.govpay.ejb.utils.rs.EjbUtils;
import it.govpay.orm.pagamenti.DistintaPagamento;
import it.govpay.orm.pagamenti.Pagamento;
import it.govpay.rs.DatiSingoloVersamento;
import it.govpay.rs.Soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class DistintaBuilder {

	public static DistintaModel fromDistinta(DistintaPagamento distinta) {

		if (distinta == null)
			return null;

		DistintaModel distintaModel = new DistintaModel();

		distintaModel.setIdDistinta(distinta.getId());
		distintaModel.setIuv(distinta.getIuv());
		distintaModel.setStato(EnumUtils.findByChiave(distinta.getStato(), EnumStatoDistinta.class));
		distintaModel.setCodTransazione(distinta.getCodTransazione());
		distintaModel.setCodTransazionePsp(distinta.getCodTransazionePSP());
		distintaModel.setIdentificativoFiscaleCreditore(distinta.getIdentificativoFiscaleCreditore());
		distintaModel.setIdGatewayPagamento(distinta.getCfgGatewayPagamento().getId());
		distintaModel.setFirma(EnumTipoFirma.valueOf(distinta.getTipoFirma()));
		distintaModel.setEmailNotifiche(distinta.getEmailVersante().trim().length() == 0 ? null : distinta.getEmailVersante().trim());
		distintaModel.setIbanAddebito(distinta.getIbanAddebito());
		distintaModel.setImportoTotale(distinta.getImporto());
		distintaModel.setDataOraRichiesta(distinta.getDataSpedizione());
		distintaModel.setAutenticazione(EnumTipoAutenticazioneSoggetto.valueOf(distinta.getAutenticazioneSoggetto()));
		distintaModel.setIdGruppo(distinta.getIdGruppo());

		if (distinta.getPagamenti() != null) {
			distintaModel.setPagamenti(new ArrayList<PagamentoModel>());
			for (Pagamento pagamento : distinta.getPagamenti()) {
				PagamentoModel pagamentoModel = new PagamentoModel();
				pagamentoModel.setIdPagamento(pagamento.getId());
				pagamentoModel.setStato(PagamentoModel.EnumStatoPagamento.valueOf(pagamento.getStPagamento()));
				pagamentoModel.setDataPagamento(pagamento.getTsInserimento());
				pagamentoModel.setIdRiscossionePSP(pagamento.getIdRiscossionePSP());
				pagamentoModel.setImportoPagato(pagamento.getImPagato());
				pagamentoModel.setIdCondizionePagamento(pagamento.getCondPagamento().getIdCondizione());

				distintaModel.getPagamenti().add(pagamentoModel);
			}
		}
		distintaModel.setSoggettoVersante(SoggettoBuilder.fromAnagraficaVersante(distinta.getDatiAnagraficiVersante(), distinta.getUtentecreatore()));

		return distintaModel;
	}

	public static PendenzaModel buildPendenza(EnteCreditoreModel creditore, TributoModel tributo, Soggetto pagatore, DatiSingoloVersamento datiSingoloVersamento)
			throws GovPayException {
		PendenzaModel pendenza = new PendenzaModel();

		// Se non mi forniscono l'anno di riferimento, ci metto quello corrente.
		XMLGregorianCalendar annoRiferimento = datiSingoloVersamento.getAnnoRiferimento();
		if (annoRiferimento != null)
			pendenza.setAnnoRiferimento(annoRiferimento.getYear());
		else
			pendenza.setAnnoRiferimento(DataTypeUtils.getXMLGregorianCalendarAdesso().getYear());

		pendenza.setIdTributo(tributo.getIdTributo());
		pendenza.setCodiceTributo(tributo.getCodiceTributo());

		List<CondizionePagamentoModel> condizioniPagamento = new ArrayList<CondizionePagamentoModel>();
		CondizionePagamentoModel condizionePagamento = new CondizionePagamentoModel();
		condizionePagamento.setCausale(datiSingoloVersamento.getCausaleVersamento());
		condizionePagamento.setCodiceTributo(tributo.getIdTributo());
		condizionePagamento.setDataFineValidita(new Date());
		condizionePagamento.setDataInizioValidita(new Date());
		condizionePagamento.setDataScadenza(DataTypeUtils.getGiorniDopo(new Date(), 1));
		condizionePagamento.setIbanBeneficiario(datiSingoloVersamento.getIbanAccredito());
		condizionePagamento.setIdCreditore(creditore.getIdEnteCreditore());
		condizionePagamento.setIdPagamentoEnte(datiSingoloVersamento.getIusv());
		condizionePagamento.setImportoTotale(datiSingoloVersamento.getImportoSingoloVersamento());
		condizionePagamento.setRagioneSocaleBeneficiario(creditore.getDenominazione());
		condizionePagamento.setTipoPagamento(EnumTipoPagamento.S);
		condizionePagamento.setDataDecorrenza(new Date());
		condizioniPagamento.add(condizionePagamento);
		pendenza.setCondizioniPagamento(condizioniPagamento);
		pendenza.setCausale(datiSingoloVersamento.getCausaleVersamento());
		pendenza.setDebitore(EjbUtils.toEjbSoggettoPagatore(pagatore));
		pendenza.setIdEnteCreditore(creditore.getIdEnteCreditore());
		pendenza.setIdDebitoEnte(datiSingoloVersamento.getIusv());
		pendenza.setImportoTotale(datiSingoloVersamento.getImportoSingoloVersamento());
		pendenza.setStatoPendenza(EnumStatoPendenza.A);
		pendenza.setModalitaPagamento(EnumModalitaPagamento.S);
		pendenza.setDataCreazione(new Date());
		pendenza.setDataDecorrenza(new Date());
		pendenza.setDataEmissione(new Date());
		pendenza.setDataPrescrizione(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		return pendenza;
	}

}
