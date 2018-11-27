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
package it.govpay.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.xml.sax.SAXException;

import gov.telematici.pagamenti.ws.ppthead.IntestazioneCarrelloPPT;
import it.gov.agenziaentrate._2014.marcadabollo.MarcaDaBollo;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.ObjectFactory;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.ER;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.RR;
import it.gov.digitpa.schemas._2011.pagamenti.informativa.InformativaContoAccredito;

public class JaxbUtils {

	private static JAXBContext jaxbContext, jaxbContextIntestazioneCarrelloPPT, jaxbBolloContext;
	private static Schema RPT_RT_schema, RR_ER_schema;
	public static Schema  GP_PA_schema;
	private static GpEventHandler gpEventHandler;

	public static void init() throws JAXBException, SAXException {
		if(jaxbContext == null || RPT_RT_schema==null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			RPT_RT_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/RPT_RT_6_0_2_FR_1_0_4.xsd"))); 
			Source[] GP_PA_sources = {new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/govpay_commons.xsd")), new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/govpay_pa.xsd"))};
			GP_PA_schema = schemaFactory.newSchema(GP_PA_sources); 
			RR_ER_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/RR_ER_1_0_0.xsd"))); 
			
			jaxbBolloContext = JAXBContext.newInstance("it.gov.agenziaentrate._2014.marcadabollo");
			jaxbContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti:it.gov.digitpa.schemas._2011.pagamenti.revoche:it.gov.digitpa.schemas._2011.ws.paa:it.gov.digitpa.schemas._2011.psp:gov.telematici.pagamenti.ws.ppthead:it.govpay.servizi.pa");
			jaxbContextIntestazioneCarrelloPPT = JAXBContext.newInstance(IntestazioneCarrelloPPT.class);
			gpEventHandler = new JaxbUtils().new GpEventHandler();
		}
	}
	
	public static byte[] toByte(CtRichiestaPagamentoTelematico rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRPT(rpt), baos);
		return baos.toByteArray();
	}
	
//	public static byte[] toByte(InformativaControparte informativa) throws JAXBException, SAXException, XMLStreamException, IOException  {
//		init();
//        ByteArrayOutputStream baos = null;
//        try {
//        	baos = new ByteArrayOutputStream();
//	        JAXBElement<InformativaControparte> informativaj = new JAXBElement<>(new QName("informativaControparte"), InformativaControparte.class, informativa);
//	        marshal(informativaj, baos);
//			return baos.toByteArray();
//        } finally {
//        	if(baos != null) {
//    	        baos.flush();
//    	        baos.close();
//        	} 
//        }
//	}
	
	public static byte[] toByte(InformativaContoAccredito informativa) throws JAXBException, SAXException, XMLStreamException, IOException  {
		init();
        ByteArrayOutputStream baos = null;
        try {
        	baos = new ByteArrayOutputStream();
	        marshal(informativa, baos);
			return baos.toByteArray();
        } finally {
        	if(baos != null) {
    	        baos.flush();
    	        baos.close();
        	} 
        }
	}
	
	public static byte[] toByte(RR rr) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(rr, baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(CtRicevutaTelematica rt) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRT(rt), baos);
		return baos.toByteArray();
	}
	
	public static void marshal(Object jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static void marshalIntestazioneCarrelloPPT(IntestazioneCarrelloPPT jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContextIntestazioneCarrelloPPT.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static void marshal(JAXBElement<?> jaxb, StringWriter sw) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, sw);
	}
	
	public static Object unmarshal(XMLStreamReader xsr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public static Object unmarshal(XMLStreamReader xsr, Schema schema) throws JAXBException, SAXException {
		if(schema == null) return unmarshal(xsr);
		
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(gpEventHandler);
		return jaxbUnmarshaller.unmarshal(xsr);
	}
    
	public static CtRichiestaPagamentoTelematico toRPT(byte[] rpt) throws JAXBException, SAXException {
		return toRPT(rpt, true);
	}
	
	public static CtRichiestaPagamentoTelematico toRPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(RPT_RT_schema);
	    JAXBElement<CtRichiestaPagamentoTelematico> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), CtRichiestaPagamentoTelematico.class);
		return root.getValue();
	}
	
	public static CtRicevutaTelematica toRT(byte[] rt) throws JAXBException, SAXException {
		return toRT(rt, true);
	}
	
	public static CtRicevutaTelematica toRT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtRicevutaTelematica> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), CtRicevutaTelematica.class);
		return root.getValue();
	}
	
	public static RR toRR(byte[] rr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RR_ER_schema);
		JAXBElement<RR> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rr)), RR.class);
		return root.getValue();
	}
	
	public static ER toER(byte[] er) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RR_ER_schema);
		JAXBElement<ER> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(er)), ER.class);
		return root.getValue();
	}
	
	public static FlussoRiversamento toFR(byte[] fr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<FlussoRiversamento> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(fr)), FlussoRiversamento.class);
		return root.getValue();
	}
	
	public class GpEventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent ve) {
			if(ve.getSeverity() == 0) {
				LoggerWrapperFactory.getLogger(JaxbUtils.class).warn("Ricevuto warning di validazione durante il marshalling del messaggio: " + ve.getMessage());
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static MarcaDaBollo toMarcaDaBollo(byte[] marca) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbBolloContext.createUnmarshaller();
	    JAXBElement<MarcaDaBollo> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(marca)), MarcaDaBollo.class);
		return root.getValue();
	}

}
