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
package it.govpay.ragioneria.v1.beans.converter;

import java.math.BigDecimal;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.ragioneria.v1.beans.Allegato;
import it.govpay.ragioneria.v1.beans.Allegato.TipoEnum;
import it.govpay.ragioneria.v1.beans.Riscossione;
import it.govpay.ragioneria.v1.beans.RiscossioneIndex;
import it.govpay.ragioneria.v1.beans.StatoRiscossione;
import it.govpay.ragioneria.v1.beans.TipoRiscossione;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {

	public static Riscossione toRsModel(Pagamento input) throws NotFoundException, IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));

			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			Stato stato = input.getStato();
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

			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			}

			rsModel.setCommissioni(input.getCommissioniPsp());

			if(input.getAllegato() != null) {

				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromValue(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
			}

			if(input.getSingoloVersamento(null) != null) {
				rsModel.setPendenza(PendenzeConverter.toRsIndexModel(input.getSingoloVersamento(null).getVersamentoBD(null)));
				rsModel.setVocePendenza(PendenzeConverter.toRsModelVocePendenza(input.getSingoloVersamento(null), input.getIndiceDati(), configWrapper));
				if(input.getRpt(null) != null)
					rsModel.setRpp(RptConverter.toRsModelIndex(input.getRpt(null), input.getSingoloVersamento(null).getVersamentoBD(null), input.getSingoloVersamento(null).getVersamentoBD(null).getApplicazione(configWrapper)));
			}

			if(input.getIncasso(null) != null)
				rsModel.setIncasso(IncassiConverter.toRsIndexModel(input.getIncasso(null)));

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

	public static RiscossioneIndex toRsModelIndexOld(Pagamento input) throws NotFoundException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));

			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			Stato stato = input.getStato();
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

			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			}

			rsModel.setCommissioni(input.getCommissioniPsp());
			if(input.getAllegato() != null) {
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromValue(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
			}

			rsModel.setCommissioni(input.getCommissioniPsp());

			if(input.getSingoloVersamento(null) != null)
				rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(input.getSingoloVersamento(null).getVersamentoBD(null).getApplicazione(configWrapper).getCodApplicazione(), input.getSingoloVersamento(null).getVersamentoBD(null).getCodVersamentoEnte()));
			if(input.getRpt(null) != null)
				rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(input.getRpt(null).getCodDominio(), input.getRpt(null).getIuv(), input.getRpt(null).getCcp()));
			if(input.getIncasso(null)!=null)
				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));

		} catch(ServiceException e) {}

		return rsModel;
	}

	public static RiscossioneIndex toRsModelIndex(it.govpay.bd.viste.model.Pagamento dto) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			Pagamento input = dto.getPagamento();
			Versamento versamento = dto.getVersamento();
			Rpt rpt = dto.getRpt();
			Incasso incasso = dto.getIncasso();

			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));

			rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte()));
			if(rpt!= null)
				rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			Stato stato = input.getStato();
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

			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			}

			rsModel.setCommissioni(input.getCommissioniPsp());
			if(input.getAllegato() != null) {
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromValue(input.getTipoAllegato().toString()));
				rsModel.setAllegato(allegato);
			}

			if(incasso !=null)
				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(incasso.getCodDominio(), incasso.getTrn()));

		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}

}
