/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.ec.v1.converter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.xml.sax.SAXException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ec.v1.beans.Notifica;
import it.govpay.ec.v1.beans.Riscossione;

public class NotificaTerminazioneConverter extends NotificaConverter {

	@Override
	public Notifica toRsModel(it.govpay.bd.model.Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException, JAXBException, SAXException {
		Notifica notificaRsModel = super.toRsModel(notifica, rpt, applicazione, versamento, pagamenti, convertiMessaggioPagoPAV2InPagoPAV1);	
		// rt
		if(rpt.getXmlRt() != null) {
			notificaRsModel.setRt(new RawObject(ConverterUtils.getRtJson(rpt, convertiMessaggioPagoPAV2InPagoPAV1)));
		}
		// elenco pagamenti
		if(pagamenti != null && pagamenti.size() > 0) {
			List<Riscossione> riscossioni = new ArrayList<>();
			int indice = 1;
			String urlPendenza = UriBuilderUtils.getPendenzaByIdA2AIdPendenza(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte());
			String urlRpt = UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
			for(Pagamento pagamento : pagamenti) {
				riscossioni.add(super.toRiscossione(pagamento, indice, urlPendenza, urlRpt));
				indice ++;
			}
			notificaRsModel.setRiscossioni(riscossioni);
		}
		return notificaRsModel;
	}
}
