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
package it.govpay.core.utils.client.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDate;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.ec.v1.beans.Allegato;
import it.govpay.ec.v1.beans.Allegato.TipoEnum;
import it.govpay.ec.v1.beans.Notifica;
import it.govpay.ec.v1.beans.Riscossione;

public class NotificaConverter {
	
	public Notifica toRsModel(it.govpay.bd.model.Notifica notifica, Rpt rpt, BasicBD bd) throws ServiceException, JAXBException, SAXException {
		Notifica notificaRsModel = new Notifica();
		notificaRsModel.setIdA2A(notifica.getApplicazione(bd).getCodApplicazione());
		notificaRsModel.setIdPendenza(rpt.getVersamento(bd).getCodVersamentoEnte());
		// rpt
		notificaRsModel.setRpt(JaxbUtils.toRPT(rpt.getXmlRpt())); 
		// rt
		if(rpt.getXmlRt() != null) {
			CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt());
			notificaRsModel.setRt(rt);
		}
		// elenco pagamenti
		if(rpt.getPagamenti(bd) != null && rpt.getPagamenti(bd).size() > 0) {
			List<Riscossione> riscossioni = new ArrayList<>();
			int indice = 1;
			String urlPendenza = UriBuilderUtils.getPendenzaByIdA2AIdPendenza(notifica.getApplicazione(bd).getCodApplicazione(), rpt.getVersamento(bd).getCodVersamentoEnte());
			String urlRpt = UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
			for(Pagamento pagamento : rpt.getPagamenti(bd)) {
				riscossioni.add(toRiscossione(pagamento, bd, indice, urlPendenza, urlRpt));
				indice ++;
			}
			notificaRsModel.setRiscossioni(riscossioni);
		}
		
		return notificaRsModel;
		
	}
	
	private Riscossione toRiscossione(Pagamento pagamento, BasicBD bd, int idx, String urlPendenza, String urlRpt) throws ServiceException {
		Riscossione riscossione = new Riscossione();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(Base64.encodeBase64String(pagamento.getAllegato()));
			allegato.setTipo(TipoEnum.fromValue(pagamento.getTipoAllegato().toString()));
			riscossione.setAllegato(allegato);
		}
		
		riscossione.setIndice(new BigDecimal(idx));
		riscossione.setIdDominio(pagamento.getCodDominio());
		riscossione.setIuv(pagamento.getIuv()); 
		riscossione.setIdVocePendenza(pagamento.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		riscossione.setCommissioni(pagamento.getCommissioniPsp());
		riscossione.setData(new LocalDate(pagamento.getDataPagamento()));
		riscossione.setImporto(pagamento.getImportoPagato());
		riscossione.setIur(pagamento.getIur());
		return riscossione;
	}
	
}
