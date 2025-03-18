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
package it.govpay.jppapdp.beans.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/***
 * Utilities di conversione JAXB degli elementi XML per le API JPAPdP
 * 
 * 
 * @author Pintori Giuliano (pintori@link.it)
 *
 */
public class JaxbUtils {

	private static JAXBContext jaxbWsJPPAPdPInternalContext, jaxbWsJPPAPdPExternalContext;
	private static boolean initialized = false;
	
	public static void init() throws JAXBException, SAXException {
		if(!initialized) {
			jaxbWsJPPAPdPInternalContext = JAXBContext.newInstance("it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal:it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.schema._1_0");
			jaxbWsJPPAPdPExternalContext = JAXBContext.newInstance("it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external:it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0");
			initialized = true;
		}
	}
	
	public static void marshalJPPAPdPInternalService(Object jaxb, OutputStream os) throws JAXBException, SAXException {
		if(jaxb == null) return;
		init();
		Marshaller jaxbMarshaller = jaxbWsJPPAPdPInternalContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static String marshalJPPAPdPInternalService(Object jaxb) throws JAXBException, SAXException {
		if(jaxb == null) return null;
		init();
		Marshaller jaxbMarshaller = jaxbWsJPPAPdPInternalContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(jaxb, baos);
		return new String(baos.toByteArray());
	}
	
	public static Object unmarshalJPPAPdPInternalService(XMLStreamReader xsr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsJPPAPdPInternalContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public static Object unmarshalJPPAPdPInternalService(XMLStreamReader xsr, Schema schema) throws JAXBException, SAXException {
		if(schema == null) return unmarshalJPPAPdPInternalService(xsr);
		
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsJPPAPdPInternalContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(new JaxbUtils().new GpEventHandler());
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public static void marshalJPPAPdPExternalService(Object jaxb, OutputStream os) throws JAXBException, SAXException {
		if(jaxb == null) return;
		init();
		Marshaller jaxbMarshaller = jaxbWsJPPAPdPExternalContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static String marshalJPPAPdPExternalService(Object jaxb) throws JAXBException, SAXException {
		if(jaxb == null) return null;
		init();
		Marshaller jaxbMarshaller = jaxbWsJPPAPdPExternalContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(jaxb, baos);
		return new String(baos.toByteArray());
	}
	
	public static Object unmarshalJPPAPdPExternalService(XMLStreamReader xsr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsJPPAPdPExternalContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public static Object unmarshalJPPAPdPExternalService(XMLStreamReader xsr, Schema schema) throws JAXBException, SAXException {
		if(schema == null) return unmarshalJPPAPdPExternalService(xsr);
		
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsJPPAPdPExternalContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(new JaxbUtils().new GpEventHandler());
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public class GpEventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent ve) {
			if(ve.getSeverity() == 0) {
				LoggerFactory.getLogger(JaxbUtils.class).warn("Ricevuto warning di validazione durante il marshalling del messaggio: " + ve.getMessage());
				return true;
			} else {
				return false;
			}
		}
	}
}
