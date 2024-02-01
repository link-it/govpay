/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.ragioneria.v3.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ragioneria.v3.beans.Allegato;
import it.govpay.ragioneria.v3.beans.Allegato.TipoEnum;
import it.govpay.ragioneria.v3.beans.Riscossione;
import it.govpay.ragioneria.v3.beans.RiscossioneVocePagata;
import it.govpay.ragioneria.v3.beans.StatoRiscossione;
import it.govpay.ragioneria.v3.beans.TipoRiscossione;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {

	public static Riscossione toRsModel(it.govpay.bd.model.Pagamento pagamento, SingoloVersamento singoloVersamento, Versamento versamento) throws IOException, UnsupportedEncodingException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riscossione rsModel = new Riscossione();
		try {
			if(pagamento != null) {
				rsModel.setDominio(DominiConverter.toRsModelIndex(pagamento.getDominio(configWrapper)));
				rsModel.setIuv(pagamento.getIuv());
				rsModel.setIur(pagamento.getIur());
				rsModel.setIndice(new BigDecimal(pagamento.getIndiceDati()));
				rsModel.setImporto(pagamento.getImportoPagato());
				rsModel.setData(pagamento.getDataPagamento());
				switch (pagamento.getTipo()) {
				case ALTRO_INTERMEDIARIO:
					rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
					break;
				case ENTRATA:
					rsModel.setTipo(TipoRiscossione.ENTRATA);
					break;
				case MBT:
					rsModel.setTipo(TipoRiscossione.MBT);
					break;
				case ENTRATA_PA_NON_INTERMEDIATA:
					rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
					break;
				}

				if(pagamento.getAllegato() != null) {
					Allegato allegato = new Allegato();
					allegato.setTesto(Base64.encodeBase64String(pagamento.getAllegato()));
					if(pagamento.getTipoAllegato() != null)
						allegato.setTipo(TipoEnum.fromCodifica(pagamento.getTipoAllegato().toString()));
					rsModel.setAllegato(allegato);
				}

				// solo per i pagamenti interni
				if(!pagamento.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
					Stato stato = pagamento.getStato();
					if(stato != null) {
						switch(stato) {
						case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
						break;
						case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
						break;
						case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
						break;
						default:
							break;
						}
					}

					if(singoloVersamento != null) {
						if(versamento == null)
							versamento = singoloVersamento.getVersamento(configWrapper);

						rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(singoloVersamento, versamento));
					}
				}
			} else {
				if(singoloVersamento != null) {
					if(versamento == null)
						versamento = singoloVersamento.getVersamento(configWrapper);

					rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(singoloVersamento, versamento));
				}
			}
		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

	public static RiscossioneVocePagata toRiscossioneVocePagataRsModel(it.govpay.bd.model.Pagamento input) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneVocePagata rsModel = new RiscossioneVocePagata();
		try {
			rsModel.setIdDominio(input.getDominio(configWrapper).getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			case ENTRATA_PA_NON_INTERMEDIATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
				break;
			}

			if(input.getAllegato() != null) {
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
			}

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
