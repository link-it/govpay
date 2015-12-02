/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.utils;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingolaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtFlussoRiversamento;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.ObjectFactory;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoVersamento;
import it.gov.digitpa.schemas._2011.psp.CtInformativaContoAccredito;
import it.gov.digitpa.schemas._2011.psp.InformativaControparte;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.exception.GovPayException;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.VerificaPagamento;
import it.govpay.servizi.pa.PaInviaEsitoPagamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class JaxbUtils {

	// uso locale "ENGLISH" perche il separatore decimale deve essere il "."
	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	private static JAXBContext jaxbContext;
	private static Schema RPT_RT_schema, RS_V1_schema;
	private static XMLOutputFactory xof;

	public static void init() throws JAXBException, SAXException {
		if(jaxbContext == null || RPT_RT_schema==null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			RPT_RT_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/Merge.xsd"))); 
			RS_V1_schema = schemaFactory.newSchema(new StreamSource(JaxbUtils.class.getResourceAsStream("/xsd/RestScadenzario_v1.xsd")));
			jaxbContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti:it.gov.digitpa.schemas._2011.ws.paa:it.gov.digitpa.schemas._2011.psp:it.govpay.rs:it.govpay.servizi.pa");
			xof = XMLOutputFactory.newFactory();
		}
	}
	
	public static Object toRestObject(InputStream is) throws JAXBException, SAXException {
		init();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(RS_V1_schema);
		unmarshaller.setEventHandler(new GovPayEventHandler());
		return unmarshaller.unmarshal(is);
	}
	
	public static byte[] toByte(CtRichiestaPagamentoTelematico rpt) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRPT(rpt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(RichiestaPagamento richiestaPagamento) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(richiestaPagamento, baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(InformativaControparte informativa) throws JAXBException, SAXException, XMLStreamException, GovPayException, IOException  {
		init();
        ByteArrayOutputStream baos = null;
        XMLStreamWriter xsw = null;
        try {
        	baos = new ByteArrayOutputStream();
	        xsw = xof.createXMLStreamWriter(baos);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setListener(new DisclaimerMarshaller(xsw, GovPayConfiguration.newInstance().getSourceCodeDisclaimer()));
	        marshaller.marshal(informativa, xsw);
			return baos.toByteArray();
        } finally {
        	if(xsw != null) {xsw.close();}
        	if(baos != null) {
    	        baos.flush();
    	        baos.close();
        	} 
        }
	}
	
	public static byte[] toByte(CtInformativaContoAccredito informativa) throws JAXBException, SAXException, XMLStreamException, GovPayException, IOException  {
		init();
        ByteArrayOutputStream baos = null;
        XMLStreamWriter xsw = null;
        try {
        	baos = new ByteArrayOutputStream();
	        xsw = xof.createXMLStreamWriter(baos);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setListener(new DisclaimerMarshaller(xsw, GovPayConfiguration.newInstance().getSourceCodeDisclaimer()));
	        JAXBElement<CtInformativaContoAccredito> jbElement = new JAXBElement<CtInformativaContoAccredito> (new QName("ctInformativaContoAccredito"), CtInformativaContoAccredito.class, informativa);
	        marshaller.marshal(jbElement, xsw);
			return baos.toByteArray();
        } finally {
        	if(xsw != null) {xsw.close();}
        	if(baos != null) {
    	        baos.flush();
    	        baos.close();
        	} 
        }
	}
	
	public static byte[] toByte(CtRichiestaRevoca rr) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRR(rr), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(CtRicevutaTelematica rt) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRT(rt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(VerificaPagamento verifica) throws JAXBException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(verifica, baos);
		return baos.toByteArray();
	}
	
	public static void marshal(JAXBElement<?> jaxb, OutputStream os) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.marshal(jaxb, os);
	}
    
	public static CtRichiestaPagamentoTelematico toRPT(byte[] rpt) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
	    JAXBElement<CtRichiestaPagamentoTelematico> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rpt)), CtRichiestaPagamentoTelematico.class);
		return root.getValue();
	}
	
	public static CtRicevutaTelematica toRT(byte[] rt) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtRicevutaTelematica> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rt)), CtRicevutaTelematica.class);
		return root.getValue();
	}
	
	public static CtEsitoRevoca toER(byte[] er) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtEsitoRevoca> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(er)), CtEsitoRevoca.class);
		return root.getValue();
	}
	
	public static CtFlussoRiversamento toFR(byte[] fr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtFlussoRiversamento> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(fr)), CtFlussoRiversamento.class);
		return root.getValue();
	}
	
	public static CtRichiestaRevoca toRR(byte[] rr) throws JAXBException, SAXException {
		init();
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setSchema(RPT_RT_schema);
		JAXBElement<CtRichiestaRevoca> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rr)), CtRichiestaRevoca.class);
		return root.getValue();
	}
	
	public static CtRichiestaPagamentoTelematico buildRPT(String codDominio, Anagrafica anagraficaEnte, String codPortale, Versamento versamento, Rpt rpt) throws GovPayException {

		CtRichiestaPagamentoTelematico ctRpt = new CtRichiestaPagamentoTelematico();
		ctRpt.setVersioneOggetto(GovPayConfiguration.newInstance().getVersioneRPT());
		
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(codDominio);
		ctDominio.setIdentificativoStazioneRichiedente(codPortale);
		ctRpt.setDominio(ctDominio);
		ctRpt.setIdentificativoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRpt.setDataOraMessaggioRichiesta(rpt.getDataOraMsgRichiesta());
	
		switch (rpt.getAutenticazioneSoggetto()) {
			case CNS:
				ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.CNS);
				break;
			case USR:
				ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.USR);
				break;
			case OTH:
				ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.OTH);
				break;
			default:
				ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.N_A);
		}

		ctRpt.setSoggettoVersante(buildSoggettoVersante(rpt.getAnagraficaVersante()));
		ctRpt.setSoggettoPagatore(buildSoggettoPagatore(versamento.getAnagraficaDebitore()));
		ctRpt.setEnteBeneficiario(buildEnteBeneficiario(anagraficaEnte));
		
		// Ordino le pendenze per idPendenza (ordinamento lessicografico)
		ctRpt.setDatiVersamento(buildDatiVersamento(rpt, versamento));


		return ctRpt;
	}

	private static CtSoggettoVersante buildSoggettoVersante(Anagrafica versante) {
		if(versante == null) return null;
		CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
		CtIdentificativoUnivocoPersonaFG idUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = versante.getCodUnivoco();
		idUnivocoVersante.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoVersante.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoVersante.setAnagraficaVersante(versante.getRagioneSociale());
		soggettoVersante.setCapVersante(versante.getCap());
		soggettoVersante.setCivicoVersante(versante.getCivico());
		soggettoVersante.setEMailVersante(versante.getEmail());
		soggettoVersante.setIdentificativoUnivocoVersante(idUnivocoVersante);
		soggettoVersante.setIndirizzoVersante(versante.getIndirizzo());
		soggettoVersante.setLocalitaVersante(versante.getLocalita());
		soggettoVersante.setNazioneVersante(versante.getNazione());
		soggettoVersante.setProvinciaVersante(versante.getProvincia());
		return soggettoVersante;
	}

	private static CtSoggettoPagatore buildSoggettoPagatore(Anagrafica debitore) {
		CtSoggettoPagatore soggettoDebitore = new CtSoggettoPagatore();
		CtIdentificativoUnivocoPersonaFG idUnivocoDebitore = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = debitore.getCodUnivoco();
		idUnivocoDebitore.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoDebitore.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoDebitore.setAnagraficaPagatore(debitore.getRagioneSociale());
		soggettoDebitore.setCapPagatore(debitore.getCap());
		soggettoDebitore.setCivicoPagatore(debitore.getCivico());
		soggettoDebitore.setEMailPagatore(debitore.getEmail());
		soggettoDebitore.setIdentificativoUnivocoPagatore(idUnivocoDebitore);
		soggettoDebitore.setIndirizzoPagatore(debitore.getIndirizzo());
		soggettoDebitore.setLocalitaPagatore(debitore.getLocalita());
		soggettoDebitore.setNazionePagatore(debitore.getNazione());
		soggettoDebitore.setProvinciaPagatore(debitore.getProvincia());
		return soggettoDebitore;
	}

	private static CtEnteBeneficiario buildEnteBeneficiario(Anagrafica ente) {
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(ente.getCodUnivoco());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(ente.getRagioneSociale());
			
		if(ente.getIndirizzo() != null && ente.getIndirizzo().trim().length()>0)
			enteBeneficiario.setIndirizzoBeneficiario(ente.getIndirizzo());
		if(ente.getLocalita() != null && ente.getLocalita().trim().length()>0)
			enteBeneficiario.setLocalitaBeneficiario(ente.getLocalita());
		if(ente.getProvincia() != null && ente.getProvincia().trim().length()>0)
			enteBeneficiario.setProvinciaBeneficiario(ente.getProvincia());
		
		return enteBeneficiario;
	}

	private static CtDatiVersamentoRPT buildDatiVersamento(Rpt rpt, Versamento versamento) throws GovPayException {
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(rpt.getDataOraMsgRichiesta());
		datiVersamento.setImportoTotaleDaVersare(versamento.getImportoTotale());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(rpt.getTipoVersamento().getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(rpt.getFirmaRichiesta().getCodifica());
		datiVersamento.setIbanAddebito(rpt.getIbanAddebito() != null ? rpt.getIbanAddebito() : null);
		
		for (SingoloVersamento singoloVersamento : versamento.getSingoliVersamenti()) {
			datiVersamento.getDatiSingoloVersamento().add(buildDatiSingoloVersamento(rpt, singoloVersamento));
		}
		
		return datiVersamento;
	}

	private static CtDatiSingoloVersamentoRPT buildDatiSingoloVersamento(Rpt rpt, SingoloVersamento singoloVersamento)  {

		CtDatiSingoloVersamentoRPT datiSingoloVersamento = new CtDatiSingoloVersamentoRPT();

		datiSingoloVersamento.setImportoSingoloVersamento(singoloVersamento.getImportoSingoloVersamento());
		datiSingoloVersamento.setIbanAccredito(singoloVersamento.getIbanAccredito());
		datiSingoloVersamento.setCausaleVersamento(singoloVersamento.getCausaleVersamento());
		datiSingoloVersamento.setDatiSpecificiRiscossione(singoloVersamento.getDatiSpecificiRiscossione());

		return datiSingoloVersamento;
	}

	public static String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale) {
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		if(IuvUtils.checkISO11640(iuv))
			return "/RFS/" + iuv + "/" + nFormatter.format(importoTotale);
		else 
			return "/RFB/" + iuv + "/" + nFormatter.format(importoTotale);
	}
	

	public static CtRichiestaRevoca buildRR(CtRicevutaTelematica rt, String idTransazioneRevoca, String causale) {
		CtRichiestaRevoca rr = new CtRichiestaRevoca();
		rr.setDataOraMessaggioRevoca(new Date());
		
		CtDatiRevoca datiRevoca = new CtDatiRevoca();
		datiRevoca.setCodiceContestoPagamento(rt.getDatiPagamento().getCodiceContestoPagamento());
		datiRevoca.setIdentificativoUnivocoVersamento(rt.getDatiPagamento().getIdentificativoUnivocoVersamento());
		datiRevoca.setImportoTotaleRevocato(rt.getDatiPagamento().getImportoTotalePagato());
		for(CtDatiSingoloPagamentoRT datiSingoloPagamento : rt.getDatiPagamento().getDatiSingoloPagamento()) {
			datiRevoca.getDatiSingolaRevoca().add(buildDatiSingolaRevoca(datiSingoloPagamento, causale));
		}
		rr.setDatiRevoca(datiRevoca);
		rr.setDominio(rt.getDominio());
		rr.setIdentificativoMessaggioRevoca(idTransazioneRevoca);
		rr.setIstitutoAttestante(rt.getIstitutoAttestante());
		rr.setSoggettoPagatore(rt.getSoggettoPagatore());
		rr.setSoggettoVersante(rt.getSoggettoVersante());
		rr.setVersioneOggetto(rt.getVersioneOggetto());
		return rr;
	}

	private static CtDatiSingolaRevoca buildDatiSingolaRevoca(CtDatiSingoloPagamentoRT datiSingoloPagamento, String causale) {
		CtDatiSingolaRevoca datiSingolaRevoca = new CtDatiSingolaRevoca();
		datiSingolaRevoca.setCausaleRevoca(causale);
		datiSingolaRevoca.setIdentificativoUnivocoRiscossione(datiSingoloPagamento.getIdentificativoUnivocoRiscossione());
		datiSingolaRevoca.setSingoloImportoRevocato(datiSingoloPagamento.getSingoloImportoPagato());
		return datiSingolaRevoca;
	}

	public static byte[] toByte(PaInviaEsitoPagamento req) throws JAXBException, SAXException {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(req, baos);
		return baos.toByteArray();
	}

}
