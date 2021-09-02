package it.govpay.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import it.govpay.core.utils.client.exception.ClientException;

public class MaggioliJPPAUtils {

	private static XMLInputFactory xif = XMLInputFactory.newInstance();

	public static void writeJPPAPdPInternalMessage(JAXBElement<?> body, Object header, OutputStream baos) throws JAXBException, SAXException, IOException {
		baos.write("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">".getBytes());
		if(header != null) {
			baos.write("<soap:Header>".getBytes());
			JaxbUtils.marshalJPPAPdPInternalService(header, baos);
			baos.write("</soap:Header>".getBytes());
		}
		baos.write("<soap:Body>".getBytes());
		JaxbUtils.marshalJPPAPdPInternalService(body, baos);
		baos.write("</soap:Body>".getBytes());
		baos.write("</soap:Envelope>".getBytes());
	}
	
	public static Object unmarshalJPPAPdPInternal(InputStream is, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		
        XMLStreamReader xsr = xif.createXMLStreamReader(is);
        
        boolean isBody = false;
        while(!isBody) {
        	xsr.nextTag();
        	if(xsr.isStartElement()) {
        		String local = xsr.getLocalName();
        		isBody = local.equals("Body");
        	}
        }
        
        xsr.nextTag();
        if(!xsr.isStartElement()) {
        	// Body vuoto
        	return null;
        } else {
        	return JaxbUtils.unmarshalJPPAPdPInternalService(xsr, schema);
        }
	}
	
	public static JAXBElement<?> toJaxbJPPAPdPInternal(byte[] msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		String s = new String(msg);
		InputStream is = IOUtils.toInputStream(s,Charset.defaultCharset());
		return  (JAXBElement<?>) unmarshalJPPAPdPInternal(is, schema);
	}
	
	public static Object unmarshalJPPAPdPInternal(byte[] msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		String s = new String(msg);
		InputStream is = IOUtils.toInputStream(s,Charset.defaultCharset());
		return  unmarshalJPPAPdPInternal(is, schema);
	}
	
	public static byte[] getBody(boolean soap, JAXBElement<?> body, Object header) throws ClientException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			if(soap) {
				MaggioliJPPAUtils.writeJPPAPdPInternalMessage(body, header, baos);
			} else {
				JaxbUtils.marshalJPPAPdPInternalService(body, baos);
			}
		}catch(Exception e) {
			throw new ClientException(e);
		}

		return baos.toByteArray();
	}
	
	public static String getBodyAsString(boolean soap, JAXBElement<?> body, Object header) throws ClientException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			if(soap) {
				MaggioliJPPAUtils.writeJPPAPdPInternalMessage(body, header, baos);
			} else {
				JaxbUtils.marshalJPPAPdPInternalService(body, baos);
			}
		}catch(Exception e) {
			throw new ClientException(e);
		}

		return baos.toString();
	}
	
	public static XMLGregorianCalendar impostaDataOperazione(Date data) throws DatatypeConfigurationException {
		GregorianCalendar dataOperazioneGC = new GregorianCalendar();
		dataOperazioneGC.setTime(data);
		XMLGregorianCalendar dataOperazione = DatatypeFactory.newInstance().newXMLGregorianCalendar(dataOperazioneGC);
		return dataOperazione;
	}
}
