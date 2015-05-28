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
package it.govpay.ejb.utils.rs;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import it.govpay.rs.ErroreEnte;
import it.govpay.rs.EsitoRevoca;
import it.govpay.rs.Pagamento;
import it.govpay.rs.Errore;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.VerificaPagamento;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class JaxbUtils {
	private static Schema schema, schemaScadenzario;
	private static JAXBContext jaxbContext; 
	
	public static void init() throws Exception {
		jaxbContext = JAXBContext.newInstance(EsitoRevoca.class, RichiestaPagamento.class, VerificaPagamento.class, Errore.class, ErroreEnte.class, Pagamento.class);
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schema = sf.newSchema(new StreamSource(JaxbUtils.class.getClassLoader().getResourceAsStream("/rest.xsd")));
		schemaScadenzario = sf.newSchema(new StreamSource(JaxbUtils.class.getClassLoader().getResourceAsStream("/restScadenzario.xsd")));
	}

	public static String toString(RichiestaPagamento richiestaPagamento) throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(richiestaPagamento, baos);
		return baos.toString();
	}
	
	public static String toString(EsitoRevoca esitoRevoca) throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(esitoRevoca, baos);
		return baos.toString();
	}
	
	public static byte[] toBytes(RichiestaPagamento richiestaPagamento) throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(richiestaPagamento, baos);
		return baos.toByteArray();
	}

	public static String toString(VerificaPagamento verifica) throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(verifica, baos);
		return baos.toString();
	}
	
	public static RichiestaPagamento toRichiestaPagamento(String s) throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new GovPayEventHandler());
		return (RichiestaPagamento) unmarshaller.unmarshal(new StringReader(s));
	}
	
	public static Object unMarshall(InputStream is) throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schemaScadenzario);
		unmarshaller.setEventHandler(new GovPayEventHandler());
		return unmarshaller.unmarshal(is);
	}
	
	public synchronized static Errore readErrore(InputStream is) throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new GovPayEventHandler());
		JAXBElement<Errore> root = unmarshaller.unmarshal(new StreamSource(is), Errore.class);
		return root.getValue();
	}
}
