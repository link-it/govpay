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

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.xml.sax.SAXException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ec.v1.beans.Allegato;
import it.govpay.ec.v1.beans.Allegato.TipoEnum;
import it.govpay.ec.v1.beans.Notifica;
import it.govpay.ec.v1.beans.Riscossione;
import it.govpay.ec.v1.beans.Riscossione.StatoEnum;
import it.govpay.ec.v1.beans.TipoRiscossione;

public class NotificaConverter {
	
	public Notifica toRsModel(it.govpay.bd.model.Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, JAXBException, SAXException {
		Notifica notificaRsModel = new Notifica();
		notificaRsModel.setIdA2A(applicazione.getCodApplicazione());
		notificaRsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		notificaRsModel.setRpt(new RawObject(ConverterUtils.getRptJson(rpt))); 
		return notificaRsModel;
	}
	
	protected Riscossione toRiscossione(Pagamento pagamento, int idx, String urlPendenza, String urlRpt) throws ServiceException {
		Riscossione riscossione = new Riscossione();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			if(pagamento.getTipoAllegato() != null) {
				switch (pagamento.getTipoAllegato()) {
				case BD:
					allegato.setTipo(TipoEnum.MARCA_DA_BOLLO);
					break;
				case ES:
					allegato.setTipo(TipoEnum.ESITO_PAGAMENTO);
					break;
				}
			}
			
			allegato.setTesto(Base64.encodeBase64String(pagamento.getAllegato()));
			riscossione.setAllegato(allegato);
		}
		riscossione.setCommissioni(pagamento.getCommissioniPsp());
		riscossione.setData(pagamento.getDataPagamento());
		riscossione.setIdDominio(pagamento.getCodDominio());
		riscossione.setIdVocePendenza(pagamento.getSingoloVersamento().getCodSingoloVersamentoEnte());
		riscossione.setImporto(pagamento.getImportoPagato());
		riscossione.setIndice(new BigDecimal(idx));
		riscossione.setIur(pagamento.getIur());
		riscossione.setIuv(pagamento.getIuv()); 
		riscossione.setPendenza(urlPendenza);
		riscossione.setRpp(urlRpt);
		riscossione.setStato(StatoEnum.RISCOSSA);
		riscossione.setTipo(TipoRiscossione.fromValue(pagamento.getTipo().name()));
		
		return riscossione;
	}
}
