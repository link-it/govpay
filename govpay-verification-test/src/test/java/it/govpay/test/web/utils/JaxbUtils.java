/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.govpay.it
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
package it.govpay.test.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import it.govpay.rs.ErroreEnte;
import it.govpay.rs.ListaPsp;
import it.govpay.rs.ObjectFactory;
import it.govpay.rs.Pagamento;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.rs.avviso.RichiestaAvviso;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class JaxbUtils {

	private static Marshaller jaxbMarshaller;
	private static Unmarshaller jaxbUnmarshaller;

	public static void init() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance("it.govpay.rs");
		jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}

	public synchronized static String toString(RichiestaPagamento richiestaPagamento) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(richiestaPagamento, baos);
			return baos.toString();
		} catch (Exception e) {
			return "ERRORE: Messaggio non serializzabile: " + e;
		}
	}
	
	public synchronized static void toString(Pagamento pagamento, OutputStream out) throws JAXBException {
		jaxbMarshaller.marshal(new ObjectFactory().createPagamento(pagamento), out);
	}
	
	public synchronized static void toString(RichiestaAvviso richiestaAvviso, OutputStream out) throws JAXBException {
		jaxbMarshaller.marshal(richiestaAvviso, out);
	}
	
	public synchronized static void toString(ErroreEnte errore, OutputStream out) throws JAXBException {
		jaxbMarshaller.marshal(errore, out);
	}

	public synchronized static String toString(VerificaPagamento verifica) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(verifica, baos);
			return baos.toString();
		} catch (Exception e) {
			return "ERRORE: Messaggio non serializzabile: " + e;
		}
	}

	public synchronized static ListaPsp toListaPsp(byte[] bytes) throws JAXBException {
		return (ListaPsp) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(bytes));
	}
	
	public synchronized static ListaPsp toListaPsp(InputStream is) throws JAXBException {
		return (ListaPsp) jaxbUnmarshaller.unmarshal(is);
	}

	public synchronized static VerificaPagamento toVerificaPagamento(byte[] bytes) throws JAXBException {
		return toVerificaPagamento(new ByteArrayInputStream(bytes));
	}
	
	public synchronized static VerificaPagamento toVerificaPagamento(InputStream is) throws JAXBException {
		JAXBElement<VerificaPagamento> root = jaxbUnmarshaller.unmarshal(new StreamSource(is), VerificaPagamento.class);
		return root.getValue();
	}
}
