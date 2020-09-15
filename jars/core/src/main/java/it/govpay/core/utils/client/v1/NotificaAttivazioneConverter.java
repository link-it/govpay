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

import java.util.List;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.ec.v1.beans.Notifica;

public class NotificaAttivazioneConverter extends NotificaConverter {
	
	@Override
	public Notifica toRsModel(it.govpay.bd.model.Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, JAXBException, SAXException {
		return super.toRsModel(notifica, rpt, applicazione, versamento, pagamenti);	
	}
}
