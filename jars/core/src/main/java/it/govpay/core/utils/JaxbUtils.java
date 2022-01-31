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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.xml.sax.SAXException;

import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazioneCarrelloPPT;
import it.gov.agenziaentrate._2014.marcadabollo.MarcaDaBollo;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.ObjectFactory;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.ER;
import it.gov.digitpa.schemas._2011.pagamenti.revoche.RR;
import it.gov.digitpa.schemas._2011.pagamenti.riversamento.FlussoRiversamento;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.servizi.pa.PaVerificaVersamentoResponse;

public class JaxbUtils {

	private static JAXBContext jaxbBolloContext, jaxbRptRtContext, jaxbRrErContext, jaxbFrContext, jaxbWsRptContext, jaxbPaForNodeContext, jaxbPaContext;
	private static Schema RPT_RT_schema, RR_ER_schema, FR_schema, PAForNode_Schema;
	public static Schema GP_PA_schema;
	private static boolean initialized = false;

	public static void init() throws JAXBException, SAXException {
		if(!initialized) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			RPT_RT_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/PagInf_RPT_RT_6_2_0.xsd"))); 
			FR_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/FlussoRiversamento_1_0_4.xsd"))); 
			RR_ER_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/RR_ER_1_0_0.xsd")));
			PAForNode_Schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/comuni/paForNode.xsd"))); 
			Source[] GP_PA_sources = {new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/govpay_commons.xsd")), new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/govpay_pa.xsd"))};
			GP_PA_schema = schemaFactory.newSchema(GP_PA_sources);


			jaxbBolloContext = JAXBContext.newInstance("it.gov.agenziaentrate._2014.marcadabollo");
			jaxbWsRptContext = JAXBContext.newInstance("gov.telematici.pagamenti.ws.rpt:gov.telematici.pagamenti.ws.rpt.ppthead");
			jaxbRptRtContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti");
			jaxbRrErContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti.revoche");
			jaxbFrContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti.riversamento");
			jaxbPaForNodeContext = JAXBContext.newInstance("it.gov.pagopa.pagopa_api.pa.pafornode");
			jaxbPaContext = JAXBContext.newInstance("it.govpay.servizi.pa");
			initialized = true;
		}
	}

	public static byte[] toByte(CtRichiestaPagamentoTelematico rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbRptRtContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRPT(rpt), baos);
		return baos.toByteArray();
	}

	public static byte[] toByte(RR rr) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbRrErContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(rr, baos);
		return baos.toByteArray();
	}

	public static byte[] toByte(CtRicevutaTelematica rt) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbRptRtContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRT(rt), baos);
		return baos.toByteArray();
	}

	public static void toIntestazioneCarrelloPPT(IntestazioneCarrelloPPT jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbWsRptContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}

	public static CtRichiestaPagamentoTelematico toRPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRptRtContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtRichiestaPagamentoTelematico> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), CtRichiestaPagamentoTelematico.class);
		return root.getValue();
	}

	public static CtRicevutaTelematica toRT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRptRtContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtRicevutaTelematica> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), CtRicevutaTelematica.class);
		return root.getValue();
	}

	public static RR toRR(byte[] rr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRrErContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RR_ER_schema);
		JAXBElement<RR> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rr)), RR.class);
		return root.getValue();
	}

	public static ER toER(byte[] er) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbRrErContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RR_ER_schema);
		JAXBElement<ER> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(er)), ER.class);
		return root.getValue();
	}

	public static FlussoRiversamento toFR(byte[] fr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbFrContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(FR_schema);
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
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}

	public static String marshalRptService(Object jaxb) throws JAXBException, SAXException {
		if(jaxb == null) return null;
		init();
		Marshaller jaxbMarshaller = jaxbWsRptContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
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
				LoggerWrapperFactory.getLogger(JaxbUtils.class).warn("Ricevuto warning di validazione durante il marshalling del messaggio: " + ve.getMessage());
				return true;
			} else {
				return false;
			}
		}
	}

	public static byte[] toByte(PaGetPaymentRes rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaGetPaymentRes(rpt), baos);
		return baos.toByteArray();
	}

	public static byte[] toByte(PaSendRTReq rt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaForNodeContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new it.gov.pagopa.pagopa_api.pa.pafornode.ObjectFactory().createPaSendRTReq(rt), baos);
		return baos.toByteArray();
	}

	public static PaGetPaymentRes toPaGetPaymentRes_RPT(byte[] rpt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(PAForNode_Schema);
		JAXBElement<PaGetPaymentRes> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), PaGetPaymentRes.class);
		return root.getValue();
	}

	public static PaSendRTReq toPaSendRTReq_RT(byte[] rt, boolean validate) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaForNodeContext.createUnmarshaller();
		if(validate) jaxbUnmarshaller.setSchema(PAForNode_Schema);
		JAXBElement<PaSendRTReq> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), PaSendRTReq.class);
		return root.getValue();
	}

	public static void marshalPA(Object jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbPaContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(jaxb, os);
	}

	public static Object unmarshalPA(XMLStreamReader xsr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(xsr);
	}

	public static Object unmarshalPA(XMLStreamReader xsr, Schema schema) throws JAXBException, SAXException {
		if(schema == null) return unmarshalPA(xsr);

		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		jaxbUnmarshaller.setEventHandler(new JaxbUtils().new GpEventHandler());
		return jaxbUnmarshaller.unmarshal(xsr);
	}


	public static PaVerificaVersamentoResponse toPaVerificaVersamentoResponse(byte[] paVerificaVersamentoResponse) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbPaContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(GP_PA_schema);
		JAXBElement<PaVerificaVersamentoResponse> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(paVerificaVersamentoResponse)), PaVerificaVersamentoResponse.class);
		return root.getValue();
	}

}
