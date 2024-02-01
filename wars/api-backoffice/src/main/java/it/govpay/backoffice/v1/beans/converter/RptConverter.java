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
package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.RppIndex;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt) throws ServiceException, IOException  {
		return toRsModel(rpt, false);
	}
	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException, IOException  {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(rpt.getVersamento()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setBloccante(rpt.isBloccante());

		if(rpt.getPagamentoPortale() != null) {
			if(rpt.getPagamentoPortale().getTipo() == 1) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.ENTE);
			} else if(rpt.getPagamentoPortale().getTipo() == 3) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.PSP);
			}
		}

		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException, IOException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(rpt.getVersamento()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setBloccante(rpt.isBloccante());

		if(rpt.getIdPagamentoPortale() != null) {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			PagamentoPortale pagamentoPortale = rpt.getPagamentoPortale(configWrapper);
			if(pagamentoPortale.getTipo() == 1) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.ENTE);
			} else if(pagamentoPortale.getTipo() == 3) {
				rsModel.setModello(it.govpay.backoffice.v1.beans.ModelloPagamento.PSP);
			}
		}

		return rsModel;
	}
}
