package it.govpay.utils;

import gov.telematici.pagamenti.ws.ppthead.IntestazioneCarrelloPPT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

public class SOAPUtils {
	
	private static XMLInputFactory xif = XMLInputFactory.newFactory();

	public static byte[] toMessage(JAXBElement<?> body, Object header) throws JAXBException, SAXException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">".getBytes());
		if(header != null) {
			baos.write("<soap:Header>".getBytes());
			if(header instanceof IntestazioneCarrelloPPT) {
				JaxbUtils.marshalIntestazioneCarrelloPPT((IntestazioneCarrelloPPT)header, baos);
			} else {
				JaxbUtils.marshal(header, baos);
			}
			baos.write("</soap:Header>".getBytes());
		}
		baos.write("<soap:Body>".getBytes());
		JaxbUtils.marshal(body, baos);
		baos.write("</soap:Body>".getBytes());
		baos.write("</soap:Envelope>".getBytes());
		return baos.toByteArray();
	}
	
	public static JAXBElement<?> toJaxb(InputStream is) throws JAXBException, SAXException, IOException, XMLStreamException {
		
		String s = new String(IOUtils.toByteArray(is));
		
		is = IOUtils.toInputStream(s);
		
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
        	return (JAXBElement<?>) JaxbUtils.unmarshal(xsr);
        }
	}
	
	public static JAXBElement<?> toJaxb(byte[] msg) throws JAXBException, SAXException, IOException, XMLStreamException {
		
		String s = new String(msg);
		
		InputStream is = IOUtils.toInputStream(s);
		
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
        	return (JAXBElement<?>) JaxbUtils.unmarshal(xsr);
        }
	}
}
