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
package it.govpay.ndp.util;

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
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.GatewayPagamentoModel;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.SoggettoModel;
import it.govpay.ejb.utils.DataTypeUtils;
import it.govpay.ejb.utils.IuvUtils;
import it.govpay.ndp.model.DominioEnteModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class NdpUtils {

	// uso locale "ENGLISH" perche il separatore decimale deve essere il "."
	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	private static JAXBContext jaxbContext;
	private static Schema RPT_RT_schema;
	private static void init() throws JAXBException, SAXException {
		if(jaxbContext == null || RPT_RT_schema==null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			RPT_RT_schema = schemaFactory.newSchema(new StreamSource(ValidatoreNdP.class.getResourceAsStream("/wsdl/PagInf_RPT_RT_6_0_1.xsd"))); 
			jaxbContext = JAXBContext.newInstance("it.gov.digitpa.schemas._2011.pagamenti:it.gov.digitpa.schemas._2011.ws.paa");
		}
	}
	
	public static byte[] toByte(CtRichiestaPagamentoTelematico rpt) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRPT(rpt), baos);
		return baos.toByteArray();
	}
	
	public static byte[] toByte(CtRichiestaRevoca rr) throws JAXBException, SAXException  {
		init();
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(new ObjectFactory().createRR(rr), baos);
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
	
	public static CtRichiestaPagamentoTelematico buildRPT(DominioEnteModel dominioEnte, GatewayPagamentoModel gw, DistintaModel distinta, List<PendenzaModel> pendenze) throws GovPayException  {

		CtRichiestaPagamentoTelematico rpt = new CtRichiestaPagamentoTelematico();
		rpt.setVersioneOggetto(NdpConfiguration.getInstance().getVersioneRPT());
		
		CtDominio dominio = new CtDominio();
		dominio.setIdentificativoDominio(dominioEnte.getIdDominio());
		dominio.setIdentificativoStazioneRichiedente(dominioEnte.getIntermediario().getIdIntermediarioPA());
		rpt.setDominio(dominio);
		rpt.setIdentificativoMessaggioRichiesta(distinta.getCodTransazione());
		rpt.setDataOraMessaggioRichiesta(DataTypeUtils.dateToXmlGregorianCalendar(distinta.getDataOraRichiesta()));
	
		switch (distinta.getAutenticazione()) {
		case CNS:
			rpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.CNS);
			break;
		case USR:
			rpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.USR);
			break;
		case OTH:
			rpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.OTH);
			break;
		default:
			rpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.N_A);
		}
		
		

		PendenzaModel pendenza = pendenze.get(0);
		SoggettoModel debitore = pendenza.getDebitore();

		if(distinta.getSoggettoVersante() != null && !debitore.getIdFiscale().equals(distinta.getSoggettoVersante().getIdFiscale()))
			rpt.setSoggettoVersante(buildSoggettoVersante(distinta.getSoggettoVersante()));

		rpt.setSoggettoPagatore(buildSoggettoPagatore(debitore));

		rpt.setEnteBeneficiario(buildEnteBeneficiario(dominioEnte));
		
		// Ordino le pendenze per idPendenza (ordinamento lessicografico)
		java.util.Collections.sort(pendenze, pendenza.new PendenzaComparator());
		rpt.setDatiVersamento(buildDatiVersamento(distinta, pendenze, gw));


		return rpt;
	}

	private static CtSoggettoVersante buildSoggettoVersante(SoggettoModel versante) {
		CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
		CtIdentificativoUnivocoPersonaFG idUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = versante.getIdFiscale();
		idUnivocoVersante.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoVersante.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoVersante.setAnagraficaVersante(versante.getAnagrafica());
		soggettoVersante.setCapVersante(versante.getCap());
		soggettoVersante.setCivicoVersante(versante.getCivico());
		soggettoVersante.setEMailVersante(versante.geteMail());
		soggettoVersante.setIdentificativoUnivocoVersante(idUnivocoVersante);
		soggettoVersante.setIndirizzoVersante(versante.getIndirizzo());
		soggettoVersante.setLocalitaVersante(versante.getLocalita());
		soggettoVersante.setNazioneVersante(versante.getNazione());
		soggettoVersante.setProvinciaVersante(versante.getProvincia());
		return soggettoVersante;
	}

	private static CtSoggettoPagatore buildSoggettoPagatore(SoggettoModel debitore) {
		CtSoggettoPagatore soggettoDebitore = new CtSoggettoPagatore();
		CtIdentificativoUnivocoPersonaFG idUnivocoDebitore = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = debitore.getIdFiscale();
		idUnivocoDebitore.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoDebitore.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoDebitore.setAnagraficaPagatore(debitore.getAnagrafica());
		soggettoDebitore.setCapPagatore(debitore.getCap());
		soggettoDebitore.setCivicoPagatore(debitore.getCivico());
		soggettoDebitore.setEMailPagatore(debitore.geteMail());
		soggettoDebitore.setIdentificativoUnivocoPagatore(idUnivocoDebitore);
		soggettoDebitore.setIndirizzoPagatore(debitore.getIndirizzo());
		soggettoDebitore.setLocalitaPagatore(debitore.getLocalita());
		soggettoDebitore.setNazionePagatore(debitore.getNazione());
		soggettoDebitore.setProvinciaPagatore(debitore.getProvincia());
		return soggettoDebitore;
	}

	private static CtEnteBeneficiario buildEnteBeneficiario(DominioEnteModel dominioEnte) {
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominioEnte.getEnteCreditore().getIdFiscale());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominioEnte.getEnteCreditore().getDenominazione());
			
		if(dominioEnte.getEnteCreditore().getIndirizzo() != null && dominioEnte.getEnteCreditore().getIndirizzo().trim().length()>0)
			enteBeneficiario.setIndirizzoBeneficiario(dominioEnte.getEnteCreditore().getIndirizzo());
		if(dominioEnte.getEnteCreditore().getLocalita() != null && dominioEnte.getEnteCreditore().getLocalita().trim().length()>0)
			enteBeneficiario.setLocalitaBeneficiario(dominioEnte.getEnteCreditore().getLocalita());
		if(dominioEnte.getEnteCreditore().getProvincia() != null && dominioEnte.getEnteCreditore().getProvincia().trim().length()>0)
			enteBeneficiario.setProvinciaBeneficiario(dominioEnte.getEnteCreditore().getProvincia());
		
		return enteBeneficiario;
	}

	private static CtDatiVersamentoRPT buildDatiVersamento(DistintaModel distinta, List<PendenzaModel> pendenze, GatewayPagamentoModel gwPagamento) throws GovPayException {
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(DataTypeUtils.toData(DataTypeUtils.dateToXmlGregorianCalendar(distinta.getDataOraRichiesta())));
		datiVersamento.setImportoTotaleDaVersare(distinta.getImportoTotale());
		switch (gwPagamento.getModalitaPagamento()) {
		case ADDEBITO_DIRETTO:
			datiVersamento.setTipoVersamento(StTipoVersamento.AD);
			break;
		case BOLLETTINO_POSTALE:
			datiVersamento.setTipoVersamento(StTipoVersamento.BP);
			break;
		case BONIFICO_BANCARIO_TESORERIA:
			datiVersamento.setTipoVersamento(StTipoVersamento.BBT);
			break;
		case CARTA_PAGAMENTO:
			datiVersamento.setTipoVersamento(StTipoVersamento.CP);
			break;
		case ATTIVATO_PRESSO_PSP:
			datiVersamento.setTipoVersamento(StTipoVersamento.PO);
			break;
		}
		
		datiVersamento.setIdentificativoUnivocoVersamento(distinta.getIuv());
		datiVersamento.setCodiceContestoPagamento(distinta.getCodTransazionePsp());
		
		String ibanAddebito = distinta.getIbanAddebito();
		
		if (ibanAddebito != null)
			datiVersamento.setIbanAddebito(ibanAddebito);
		
		switch (distinta.getFirma()) {
		case CA_DES:
			datiVersamento.setFirmaRicevuta("1");
			break;
		case XA_DES:
			datiVersamento.setFirmaRicevuta("2");
			break;
		case AVANZATA:
			datiVersamento.setFirmaRicevuta("3");
			break;
		default:
			datiVersamento.setFirmaRicevuta("0");
			break;
		}
		
		for (PendenzaModel pendenza : pendenze) {
			datiVersamento.getDatiSingoloVersamento().add(buildDatiSingoloVersamento(distinta, pendenza, gwPagamento));
		}
		
		return datiVersamento;
	}

	private static CtDatiSingoloVersamentoRPT buildDatiSingoloVersamento(DistintaModel distinta, PendenzaModel pendenza, GatewayPagamentoModel gwPagamento)  {

		CtDatiSingoloVersamentoRPT datiSingoloVersamento = new CtDatiSingoloVersamentoRPT();

		datiSingoloVersamento.setImportoSingoloVersamento(pendenza.getImportoTotale());
		datiSingoloVersamento.setIbanAccredito(pendenza.getCondizioniPagamento().get(0).getIbanBeneficiario());
		datiSingoloVersamento.setCausaleVersamento(buildCausaleSingoloVersamento(distinta.getIuv(), pendenza.getImportoTotale()));
		datiSingoloVersamento.setDatiSpecificiRiscossione(buildDatiSpecificiRiscossore());
		

		return datiSingoloVersamento;
	}

	public static String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale) {
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		String reference = iuv.substring(4);
		String check;
		try {
			check = IuvUtils.getCheckDigit(reference);
		} catch (GovPayException e) {
			check = "";
		}
		if(iuv.equals("RF" + check + reference))
			return "/RFS/" + iuv + "/" + nFormatter.format(importoTotale);
		else 
			return "/RFB/" + iuv + "/" + nFormatter.format(importoTotale);
	}
	
	public static String buildDatiSpecificiRiscossore() {
		return NdpConfiguration.getInstance().getDatiSpecificiRiscossione();
	}

	public static CtRichiestaRevoca buildRR(CtRicevutaTelematica rt, String idTransazioneRevoca, String causale) {
		CtRichiestaRevoca rr = new CtRichiestaRevoca();
		rr.setDataOraMessaggioRevoca(DataTypeUtils.getXMLGregorianCalendarAdesso());
		
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
}
