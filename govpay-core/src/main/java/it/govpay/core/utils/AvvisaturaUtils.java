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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import gov.telematici.pagamenti.ws.CtAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtDatiSingoloVersamento;
import gov.telematici.pagamenti.ws.CtEsitoAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtEsitoPresaInCarico;
import gov.telematici.pagamenti.ws.CtIdentificativoUnivocoPersonaFG;
import gov.telematici.pagamenti.ws.CtSoggettoPagatore;
import gov.telematici.pagamenti.ws.ListaEsitoAvvisiDigitali;
import gov.telematici.pagamenti.ws.ObjectFactory;
import gov.telematici.pagamenti.ws.StTipoIdentificativoUnivocoPersFG;
import gov.telematici.pagamenti.ws.StTipoOperazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.rs.v1.beans.base.TassonomiaAvviso;
import it.govpay.model.Anagrafica.TIPO;

public class AvvisaturaUtils {

	private static JAXBContext jaxbContext;
	private static Schema schema;

	public static void scriviHeaderTracciatoAvvisatura(OutputStream out) throws IOException {
		out.write("<listaAvvisiDigitali xmlns=\"http://ws.pagamenti.telematici.gov/\"><versioneOggetto>1.0</versioneOggetto>".getBytes());
	}

	public static void scriviVersamentoTracciatoAvvisatura(OutputStream out, Versamento versamento) throws Exception {
		
		CtAvvisoDigitale avviso = new ObjectFactory().createCtAvvisoDigitale();
		avviso.setIdentificativoDominio(versamento.getDominio(null).getCodDominio());
		avviso.setAnagraficaBeneficiario(versamento.getDominio(null).getRagioneSociale());
		avviso.setIdentificativoMessaggioRichiesta(versamento.getCodAvvisatura());
		TassonomiaAvviso tassonomia = TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso());
		
		String tassonomiaString = null;
		switch(tassonomia) {
		case CARTELLE_ESATTORIALI: tassonomiaString = "00"; 
			break;
		case DIRITTI_E_CONCESSIONI: tassonomiaString = "01";
			break;
		case IMPOSTE_E_TASSE: tassonomiaString = "02";
			break;
		case IMU_TASI_E_ALTRE_TASSE_COMUNALI: tassonomiaString = "03";
			break;
		case INGRESSI_A_MOSTRE_E_MUSEI: tassonomiaString = "04";
			break;
		case MULTE_E_SANZIONI_AMMINISTRATIVE: tassonomiaString = "05";
			break;
		case PREVIDENZA_E_INFORTUNI: tassonomiaString = "06";
			break;
		case SERVIZI_EROGATI_DAL_COMUNE: tassonomiaString = "07";
			break;
		case SERVIZI_EROGATI_DA_ALTRI_ENTI: tassonomiaString = "08";
			break;
		case SERVIZI_SCOLASTICI: tassonomiaString = "09";
			break;
		case TASSA_AUTOMOBILISTICA: tassonomiaString = "10";
			break;
		case TICKET_E_PRESTAZIONI_SANITARIE: tassonomiaString = "11";
			break;
		case TRASPORTI_MOBILIT_E_PARCHEGGI: tassonomiaString = "12";
			break;
		default:
			break;
		
		}
		
		avviso.setTassonomiaAvviso(tassonomiaString);
		avviso.setCodiceAvviso(versamento.getNumeroAvviso());
		
		CtSoggettoPagatore soggettoPagatore = new CtSoggettoPagatore();
		soggettoPagatore.setAnagraficaPagatore(versamento.getAnagraficaDebitore().getRagioneSociale());
		
		CtIdentificativoUnivocoPersonaFG identificativoUnivocoPersonaFG = new CtIdentificativoUnivocoPersonaFG();
		identificativoUnivocoPersonaFG.setCodiceIdentificativoUnivoco(versamento.getAnagraficaDebitore().getCodUnivoco());

		if(versamento.getAnagraficaDebitore().getTipo()!=null) {
			StTipoIdentificativoUnivocoPersFG tipoIdentificativo = versamento.getAnagraficaDebitore().getTipo().equals(TIPO.F) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G;
			identificativoUnivocoPersonaFG.setTipoIdentificativoUnivoco(tipoIdentificativo);
		} else {
			identificativoUnivocoPersonaFG.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F); //TODO default
		}
		
		soggettoPagatore.setIdentificativoUnivocoPagatore(identificativoUnivocoPersonaFG);
		
		avviso.setSoggettoPagatore(soggettoPagatore);
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		
		gregorianCalendar.set(Calendar.YEAR, 9999);
		gregorianCalendar.set(Calendar.MONTH, 12);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 31);
		gregorianCalendar.set(Calendar.HOUR, 23);
		gregorianCalendar.set(Calendar.MINUTE, 59);
		gregorianCalendar.set(Calendar.SECOND, 59);
		
		Date defaultDate = gregorianCalendar.getTime();
		
		if(versamento.getDataValidita()!=null) {
			avviso.setDataScadenzaPagamento(toXmlGregorianCalendar(versamento.getDataValidita()));
		} else {
			avviso.setDataScadenzaPagamento(toXmlGregorianCalendar(defaultDate));
		}
		
		if(versamento.getDataScadenza()!=null) {
			avviso.setDataScadenzaAvviso(toXmlGregorianCalendar(versamento.getDataScadenza()));
		} else {
			avviso.setDataScadenzaAvviso(toXmlGregorianCalendar(defaultDate));
		}
		
		avviso.setImportoAvviso(versamento.getImportoTotale());
		avviso.setEMailSoggetto(versamento.getAnagraficaDebitore().getEmail());
		avviso.setCellulareSoggetto(versamento.getAnagraficaDebitore().getCellulare());
		
		if(versamento.getCausaleVersamento() != null)
			avviso.setDescrizionePagamento(versamento.getCausaleVersamento().getSimple());
		
		for(SingoloVersamento singoloVersamento: versamento.getSingoliVersamenti(null)) {
			CtDatiSingoloVersamento datiSingoloVersamento = new CtDatiSingoloVersamento();
			if(singoloVersamento.getIbanAccredito(null)!=null)
				datiSingoloVersamento.setIbanAccredito(singoloVersamento.getIbanAccredito(null).getCodIban());
			
			if(singoloVersamento.getIbanAppoggio(null)!=null)
				datiSingoloVersamento.setIbanAppoggio(singoloVersamento.getIbanAppoggio(null).getCodIban());
			
			avviso.getDatiSingoloVersamento().add(datiSingoloVersamento);
		}
		
		if(versamento.getTipoPagamento()!=null)
			avviso.setTipoPagamento(versamento.getTipoPagamento() + "");
		else 
			avviso.setTipoPagamento("1"); //default pagamento non contestuale
		
		avviso.setTipoOperazione(StTipoOperazione.fromValue(versamento.getAvvisatura()));
		
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(new ObjectFactory().createCtAvvisoDigitale(avviso), out);
	}

	private static void init() throws JAXBException, SAXException {
		if(jaxbContext == null || schema == null) {
			jaxbContext = JAXBContext.newInstance(CtAvvisoDigitale.class.getPackage().getName());
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schema = schemaFactory.newSchema(new StreamSource(AvvisaturaUtils.class.getResourceAsStream("/xsd/avvisi-digitali-1.0.xsd"))); 
		}
		
	}

	private static XMLGregorianCalendar toXmlGregorianCalendar(Date data) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(data);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	}

	public static void scriviTailTracciatoAvvisatura(OutputStream out) throws IOException {
		out.write("</listaAvvisiDigitali>".getBytes());
	}
	
	
	private static boolean trovaProssimoEsitoAvvisoDigitale(XMLStreamReader xsr) throws IOException, XMLStreamException {

		int eventType = xsr.getEventType();
		do {
			if(eventType == XMLStreamReader.START_ELEMENT && xsr.getLocalName().equals(CtEsitoAvvisoDigitale.class.getAnnotation(XmlType.class).name())) {
				return true;
			}
			eventType = xsr.next();
		} while (xsr.hasNext());

		return false;
	}


	public static  CtEsitoAvvisoDigitale leggiProssimoEsitoAvvisoDigitale(XMLStreamReader xsr, Transformer t) throws JAXBException, IOException, XMLStreamException, TransformerException {

		if(trovaProssimoEsitoAvvisoDigitale(xsr)) {

			DOMResult result = new DOMResult();
			t.transform(new StAXSource(xsr), result);
			Node domNode = result.getNode();
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<CtEsitoAvvisoDigitale> root = jaxbUnmarshaller.unmarshal(domNode, CtEsitoAvvisoDigitale.class);
			return root.getValue();
		} else {
			return null;
		}

	}

	public static List<CtEsitoAvvisoDigitale> leggiListaAvvisiDigitali(InputStream is) throws JAXBException, IOException, XMLStreamException, TransformerException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		
		JAXBElement<ListaEsitoAvvisiDigitali> root = jaxbUnmarshaller.unmarshal(new StreamSource(is), ListaEsitoAvvisiDigitali.class);
		return root.getValue().getEsitoAvvisoDigitale();
	}


	public static void scriviEsitoPresaInCarico(CtEsitoPresaInCarico esito, OutputStream os) throws JAXBException, IOException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.marshal(new ObjectFactory().createEsitoPresaInCarico(esito), os);
	}

	public static CtEsitoPresaInCarico leggiEsitoPresaInCaricoAvvisoDigitale(InputStream is) throws JAXBException, IOException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(schema);
		JAXBElement<CtEsitoPresaInCarico> root = jaxbUnmarshaller.unmarshal(new StreamSource(is), CtEsitoPresaInCarico.class);
		return root.getValue();
	}
}
