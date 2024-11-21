/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.pagopa.beans.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazioneCarrelloPPT;
import it.gov.agenziaentrate._2014.marcadabollo.MarcaDaBollo;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.ObjectFactory;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;

/**
 * Utilities di conversione JAXB per gli elementi XML delle API PagoPA.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 * 
 */
public class JaxbUtils {

	private static final String PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION = "com.sun.xml.bind.xmlDeclaration";
	private static JAXBContext jaxbBolloContext;
	private static JAXBContext jaxbRptRtContext;
	private static JAXBContext jaxbFrContext;
	private static JAXBContext jaxbWsRptContext;
	private static JAXBContext jaxbPaForNodeContext;
	private static Schema rptRtSchema;
	private static Schema frSchema;
	private static Schema paForNodeSchema;
	private static boolean initialized = false;

	public static void init() throws JAXBException, SAXException {
		if(!initialized) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			rptRtSchema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/PagInf_RPT_RT_6_2_0.xsd"))); 
			frSchema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/FlussoRiversamento_1_0_4.xsd"))); 

			javax.xml.transform.Source [] sources = {new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/sac-common-types-1.0.xsd")), new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/paForNode.xsd"))};
			paForNodeSchema = schemaFactory.newSchema(sources); 
			
			jaxbBolloContext = JAXBContext.newInstance("it.gov.agenziaentrate._2014.marcadabollo");
			jaxbWsRptContext = JAXBContext.newInstance("gov.telematici.pagamenti.ws.rpt:gov.telematici.pagamenti.ws.rpt.ppthead");
			jaxbRptRtContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti");
			jaxbFrContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti.riversamento");
			jaxbPaForNodeContext = JAXBContext.newInstance("it.gov.pagopa.pagopa_api.pa.pafornode");
			initialized = true;
		}
	}
	
	public static byte[] toByte(CtRichiestaPagamentoTelematico rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbRptRtContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRPT(rpt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(CtRicevutaTelematica rt) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbRptRtContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRT(rt), baos);
		return baos.toByteArray();
	}
	
	public static void toIntestazioneCarrelloPPT(IntestazioneCarrelloPPT jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbWsRptContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static CtRichiestaPagamentoTelematico toRPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRptRtContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(rptRtSchema);
	    JAXBElement<CtRichiestaPagamentoTelematico> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), CtRichiestaPagamentoTelematico.class);
		return root.getValue();
	}
	
	public static CtRicevutaTelematica toRT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRptRtContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(rptRtSchema);
		JAXBElement<CtRicevutaTelematica> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), CtRicevutaTelematica.class);
		return root.getValue();
	}
	
	public static FlussoRiversamento toFR(byte[] fr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbFrContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(frSchema);
		JAXBElement<FlussoRiversamento> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(fr)), FlussoRiversamento.class);
		return root.getValue();
	}
	
	public static MarcaDaBollo toMarcaDaBollo(byte[] marca) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbBolloContext.createUnmarshaller();
	    JAXBElement<MarcaDaBollo> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(marca)), MarcaDaBollo.class);
		return root.getValue();
	}
	
	public static void marshalRptService(Object jaxb, OutputStream os) throws JAXBException, SAXException {
		if(jaxb == null) return;
		init();
		Marshaller jaxbMarshaller = jaxbWsRptContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}
	
	public static String marshalRptService(Object jaxb) throws JAXBException, SAXException {
		if(jaxb == null) return null;
		init();
		Marshaller jaxbMarshaller = jaxbWsRptContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(jaxb, baos);
		return new String(baos.toByteArray());
	}
	
	public static Object unmarshalRptService(XMLStreamReader xsr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsRptContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public static Object unmarshalRptService(XMLStreamReader xsr, Schema schema) throws JAXBException, SAXException {
		if(schema == null) return unmarshalRptService(xsr);
		
		init();
		Unmarshaller jaxbUnmarshaller = jaxbWsRptContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(new JaxbUtils().new GpEventHandler());
		return jaxbUnmarshaller.unmarshal(xsr);
	}
	
	public class GpEventHandler implements ValidationEventHandler {
		@Override
		public boolean handleEvent(ValidationEvent ve) {
			if(ve.getSeverity() == 0) {
				LoggerFactory.getLogger(JaxbUtils.class).warn("Ricevuto warning di validazione durante il marshalling del messaggio: {}" , ve.getMessage());
				return true;
			} else {
				return false;
			}
		}
	}

	public static byte[] toByte(PaGetPaymentRes rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaGetPaymentRes(rpt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(PaSendRTReq rt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaSendRTReq(rt), baos);
		return baos.toByteArray();
	}
	
	public static PaGetPaymentRes toPaGetPaymentResRPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(paForNodeSchema);
	    JAXBElement<PaGetPaymentRes> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), PaGetPaymentRes.class);
		return root.getValue();
	}
	
	public static PaSendRTReq toPaSendRTReqRT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(paForNodeSchema);
	    JAXBElement<PaSendRTReq> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), PaSendRTReq.class);
		return root.getValue();
	}
	
	public static byte[] toByte(PaGetPaymentV2Response rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaGetPaymentV2Response(rpt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(PaSendRTV2Request rt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty(PROPERTY_NAME_COM_SUN_XML_BIND_XML_DECLARATION, Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaSendRTV2Request(rt), baos);
		return baos.toByteArray();
	}
	
	public static PaGetPaymentV2Response toPaGetPaymentV2ResponseRPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(paForNodeSchema);
	    JAXBElement<PaGetPaymentV2Response> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), PaGetPaymentV2Response.class);
		return root.getValue();
	}
	
	public static PaSendRTV2Request toPaSendRTV2RequestRT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(paForNodeSchema);
	    JAXBElement<PaSendRTV2Request> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), PaSendRTV2Request.class);
		return root.getValue();
	}
}
