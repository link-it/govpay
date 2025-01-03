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
package it.govpay.pendenze.v1.beans.converter;


import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pendenze.v1.beans.Rpp;
import it.govpay.pendenze.v1.beans.RppIndex;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws IOException, ServiceException {
		Rpp rsModel = new Rpp();
		boolean convertiMessaggioPagoPAV2InPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsIndexModel(versamento));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		
		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione) throws IOException {
		RppIndex rsModel = new RppIndex();
		boolean convertiMessaggioPagoPAV2InPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte()));
		rsModel.setRpt(ConverterUtils.getRptJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		rsModel.setRt(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1));
		
		return rsModel;
	}
}
