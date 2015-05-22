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
package it.govpay.web.utils;

import it.govpay.ejb.utils.rs.GovPayEventHandler;
import it.govpay.rs.RichiestaPagamento;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;


@Provider
@Consumes(MediaType.APPLICATION_XML)
public class ValidatingReader implements MessageBodyReader<RichiestaPagamento> {

	private Schema schema;
	private JAXBContext jaxbContext;

	public ValidatingReader() {
		try {
			jaxbContext = JAXBContext.newInstance(RichiestaPagamento.class);
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = sf.newSchema(new StreamSource(ValidatingReader.class.getClassLoader().getResourceAsStream("/rest.xsd")));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return arg0 == RichiestaPagamento.class;
	}
	
	@Override
	public RichiestaPagamento readFrom(Class<RichiestaPagamento> arg0, Type arg1, Annotation[] arg2, MediaType arg3, MultivaluedMap<String, String> arg4, InputStream arg5)
			throws IOException, WebApplicationException {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(schema);
			unmarshaller.setEventHandler(new GovPayEventHandler());
			return (RichiestaPagamento) unmarshaller.unmarshal(arg5);
		} catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}

