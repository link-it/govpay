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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.CtAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtDatiSingoloVersamento;
import gov.telematici.pagamenti.ws.CtEsitoPresaInCarico;
import gov.telematici.pagamenti.ws.CtIdentificativoUnivocoPersonaFG;
import gov.telematici.pagamenti.ws.CtSoggettoPagatore;
import gov.telematici.pagamenti.ws.ObjectFactory;
import gov.telematici.pagamenti.ws.StTipoIdentificativoUnivocoPersFG;
import gov.telematici.pagamenti.ws.StTipoOperazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica.TIPO;

public class AvvisaturaUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(AvvisaturaUtils.class);
	private static JAXBContext jaxbContext;
	
	public static void scriviHeaderTracciatoAvvisatura(OutputStream out) throws IOException {
		out.write("<listaAvvisiDigitali xmlns=\"http://ws.pagamenti.telematici.gov/\"><versioneOggetto>1.0</versioneOggetto>".getBytes());
	}

	public static void scriviVersamentoTracciatoAvvisatura(OutputStream out, Versamento versamento) throws Exception {
		
		CtAvvisoDigitale avviso = new ObjectFactory().createCtAvvisoDigitale();
		avviso.setIdentificativoDominio(versamento.getDominio(null).getCodDominio());
		avviso.setAnagraficaBeneficiario(versamento.getDominio(null).getRagioneSociale());
		avviso.setIdentificativoMessaggioRichiesta(versamento.getCodAvvisatura());
		avviso.setTassonomiaAvviso("00");//TODO versamento.getTassonomiaAvviso());
		avviso.setCodiceAvviso(versamento.getNumeroAvviso());
		
		CtSoggettoPagatore soggettoPagatore = new CtSoggettoPagatore();
		soggettoPagatore.setAnagraficaPagatore(versamento.getAnagraficaDebitore().getRagioneSociale());
		
		CtIdentificativoUnivocoPersonaFG identificativoUnivocoPersonaFG = new CtIdentificativoUnivocoPersonaFG();
		identificativoUnivocoPersonaFG.setCodiceIdentificativoUnivoco(versamento.getAnagraficaDebitore().getCodUnivoco());
		if(versamento.getAnagraficaDebitore().getTipo()!=null) {
			StTipoIdentificativoUnivocoPersFG tipoIdentificativo = versamento.getAnagraficaDebitore().getTipo().equals(TIPO.F) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G;
			identificativoUnivocoPersonaFG.setTipoIdentificativoUnivoco(tipoIdentificativo);
		} else {
			identificativoUnivocoPersonaFG.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F); //TODO non puo' essere null
		}
		
		soggettoPagatore.setIdentificativoUnivocoPagatore(identificativoUnivocoPersonaFG);
		
		avviso.setSoggettoPagatore(soggettoPagatore);
		
		if(versamento.getDataValidita()!=null) {
			avviso.setDataScadenzaPagamento(toXmlGregorianCalendar(versamento.getDataValidita()));
		} else {
			avviso.setDataScadenzaPagamento(toXmlGregorianCalendar(new Date())); //TODO default?
		}
		
		if(versamento.getDataScadenza()!=null) {
			avviso.setDataScadenzaAvviso(toXmlGregorianCalendar(versamento.getDataScadenza())); //TODO sono scambiate le due date?
		} else {
			avviso.setDataScadenzaAvviso(toXmlGregorianCalendar(new Date())); //TODO default?
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
		
		avviso.setTipoPagamento("0");//TODO versamento.getTipoPagamento() + "");
		avviso.setTipoOperazione(StTipoOperazione.fromValue(versamento.getAvvisatura()));
		
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(new ObjectFactory().createCtEsitoAvvisoDigitale(avviso), out);
	}

	private static void init() throws JAXBException {
		if(jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(CtAvvisoDigitale.class.getPackage().getName());
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
	
	public static CtEsitoPresaInCarico leggiEsitoPresaInCaricoAvvisoDigitale(InputStream is) throws JAXBException, IOException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		IOUtils.copy(is, baos);
		byte[] byteArray = baos.toByteArray();
		JAXBElement<CtEsitoPresaInCarico> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArray)), CtEsitoPresaInCarico.class);
		return root.getValue();
	}
}
