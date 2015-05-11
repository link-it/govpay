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
package it.govpay.report.utils;

import java.io.ByteArrayOutputStream;

import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.InformazioniVersamento;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxbUtils {

	private static Marshaller jaxbMarshaller;

	public static void init() throws JAXBException {
		if(jaxbMarshaller != null) return;
		JAXBContext jaxbContext = JAXBContext.newInstance(InformazioniVersamento.class);
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	}

	public synchronized static String toString(InformazioniVersamento informazioni) throws JAXBException {
		init();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(informazioni, baos);
		return baos.toString();
	}
}
