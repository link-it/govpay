package it.govpay.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.xml.sax.SAXException;

import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.QuotaContabilita;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtDatiVersamentoRT;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtDettagliImporto;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtDettaglioImporto;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtEnteBeneficiario;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtEsito;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtIdentificativoUnivoco;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtIdentificativoUnivocoPersonaFG;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtIdentificativoUnivocoPersonaG;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtIstitutoAttestante;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtParametriPSP;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtRicevutaTelematica;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtSoggettoPagatore;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.CtSoggettoVersante;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.RecuperaRTRisposta;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.StTipoIdentificativoUnivoco;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.StTipoIdentificativoUnivocoPersFG;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.StTipoIdentificativoUnivocoPersG;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.StTipoVersamento;

public class MaggioliJPPAUtils {

	// root element elemento di input
	public static final String INVIA_ESITO_PAGAMENTO_RICHIESTA_ROOT_ELEMENT_NAME = "InviaEsitoPagamentoRichiesta"; 
	public static final String RECUPERA_RT_RISPOSTA_ROOT_ELEMENT_NAME = "RecuperaRTRisposta"; 
	public static final String CDATA_TOKEN_START = "<![CDATA["; 
	public static final String CDATA_TOKEN_END = "]]>"; 

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

	public static void writeJPPAPdPExternalMessage(JAXBElement<?> body, Object header, OutputStream baos) throws JAXBException, SAXException, IOException {
		baos.write("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">".getBytes());
		if(header != null) {
			baos.write("<soap:Header>".getBytes());
			JaxbUtils.marshalJPPAPdPExternalService(header, baos);
			baos.write("</soap:Header>".getBytes());
		}
		baos.write("<soap:Body>".getBytes());
		JaxbUtils.marshalJPPAPdPExternalService(body, baos);
		baos.write("</soap:Body>".getBytes());
		baos.write("</soap:Envelope>".getBytes());
	}

	public static Object unmarshalJPPAPdPExternal(InputStream is, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {

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
			return JaxbUtils.unmarshalJPPAPdPExternalService(xsr, schema);
		}
	}

	public static JAXBElement<?> toJaxbJPPAPdPExternal(byte[] msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		String s = new String(msg);
		InputStream is = IOUtils.toInputStream(s,Charset.defaultCharset());
		return  (JAXBElement<?>) unmarshalJPPAPdPExternal(is, schema);
	}

	public static Object unmarshalJPPAPdPExternal(byte[] msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		String s = new String(msg);
		InputStream is = IOUtils.toInputStream(s,Charset.defaultCharset());
		return  unmarshalJPPAPdPExternal(is, schema);
	}

	public static Object unmarshalJPPAPdPExternal(String msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		InputStream is = IOUtils.toInputStream(msg,Charset.defaultCharset());
		XMLStreamReader xsr = xif.createXMLStreamReader(is);
		return JaxbUtils.unmarshalJPPAPdPExternalService(xsr, schema);
	}

	public static JAXBElement<?> toJaxbJPPAPdPExternal2(String msg, Schema schema) throws JAXBException, SAXException, IOException, XMLStreamException {
		InputStream is = IOUtils.toInputStream(msg,Charset.defaultCharset());
		XMLStreamReader xsr = xif.createXMLStreamReader(is);
		return (JAXBElement<?>) JaxbUtils.unmarshalJPPAPdPExternalService(xsr, schema);
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

	public static RecuperaRTRisposta buildRTRisposta(it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.external.schema._1_0.ObjectFactory objectFactory, Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, ValidationException, DatatypeConfigurationException, JAXBException, SAXException {
		RecuperaRTRisposta recuperaRTRisposta = objectFactory.createRecuperaRTRisposta();
		Versamento versamento = rpt.getVersamento(configWrapper);
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);


		CtEsito esito = new CtEsito();
		esito.setCode("OK");
		recuperaRTRisposta.setEsito(esito );

		CtRicevutaTelematica ricevutaTelematica = new CtRicevutaTelematica();
		switch (rpt.getVersione()) {
		case SANP_230:
		{
			popolaRicevutaDaRPT23(ricevutaTelematica, rpt, versamento, singoliVersamenti, configWrapper);
		}
		break;
		case SANP_240:
		{
			popolaRicevutaDaRPT24(ricevutaTelematica, rpt, versamento, singoliVersamenti, configWrapper);
		}
		break;
		}

		recuperaRTRisposta.setRicevutaTelematica(ricevutaTelematica);

		popolaDettaglioImportoRT(recuperaRTRisposta, singoliVersamenti);

		return recuperaRTRisposta;
	}

	private static void popolaDettaglioImportoRT(RecuperaRTRisposta recuperaRTRisposta, List<SingoloVersamento> singoliVersamenti)	throws ServiceException, ValidationException {
		CtDettagliImporto dettagliImporto = new CtDettagliImporto();
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			if(singoloVersamento.getContabilita() != null) {
				it.govpay.model.Contabilita contabilita = ConverterUtils.parse(singoloVersamento.getContabilita(), it.govpay.model.Contabilita.class);
				if(contabilita.getQuote() != null && !contabilita.getQuote().isEmpty()) {
					for (QuotaContabilita quota : contabilita.getQuote()) {
						CtDettaglioImporto dettaglioImporto = new CtDettaglioImporto();
						dettaglioImporto.setAnno(quota.getAnnoEsercizio()+"");
						dettaglioImporto.setCapitoloBilancio(quota.getCapitolo());
						dettaglioImporto.setDescrizione(singoloVersamento.getDescrizione());
						dettaglioImporto.setImporto(quota.getImporto());
						dettagliImporto.getDettaglioImporto().add(dettaglioImporto );
					}
				}
			}
		}

		if(!dettagliImporto.getDettaglioImporto().isEmpty()) {
			recuperaRTRisposta.setDettagliImporto(dettagliImporto );
		}
	}

	private static void popolaRicevutaDaRPT23(CtRicevutaTelematica ricevutaTelematica, Rpt rpt, Versamento versamento, List<SingoloVersamento> singoliVersamenti, BDConfigWrapper configWrapper) throws DatatypeConfigurationException, JAXBException, SAXException, ServiceException {
		it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);

		ricevutaTelematica.setDataOraMessaggioRicevuta(impostaDataOperazione(rpt.getDataMsgRicevuta()));
		popolaDatiVersamentoRT(ricevutaTelematica, rpt);
		popolaEnteBeneficiarioRT(ricevutaTelematica, rpt, versamento, configWrapper);
		ricevutaTelematica.setIdDominio(rpt.getCodDominio());
		ricevutaTelematica.setIdentificativoMessaggioRicevuta(rt.getIdentificativoMessaggioRicevuta());
		popolaIstitutoAttestanteRT23(ricevutaTelematica, rt);
		popoloaParametriPSP(ricevutaTelematica, rpt);
		ricevutaTelematica.setRicevutaXML(new String(rpt.getXmlRt()));
		ricevutaTelematica.setRiferimentoDataRichiesta(impostaDataOperazione(rt.getRiferimentoDataRichiesta()));
		ricevutaTelematica.setRiferimentoMessaggioRichiesta(rt.getRiferimentoMessaggioRichiesta());
		popolaSoggettoPagatoreRT23(ricevutaTelematica, rt);
		popolaSoggettoVersanteRT23(ricevutaTelematica, rt);
	}

	private static void popolaSoggettoPagatoreRT23(CtRicevutaTelematica ricevutaTelematica,
			it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica rt) {
		CtSoggettoPagatore soggettoPagatore = new CtSoggettoPagatore();
		soggettoPagatore.setAnagraficaPagatore(rt.getSoggettoPagatore().getAnagraficaPagatore());
		soggettoPagatore.setCapPagatore(rt.getSoggettoPagatore().getCapPagatore());
		soggettoPagatore.setCivicoPagatore(rt.getSoggettoPagatore().getCivicoPagatore());
		soggettoPagatore.setEMailPagatore(rt.getSoggettoPagatore().getEMailPagatore());
		CtIdentificativoUnivocoPersonaFG identificativoUnivocoPagatore = new CtIdentificativoUnivocoPersonaFG();
		identificativoUnivocoPagatore.setCodiceIdentificativoUnivoco(rt.getSoggettoPagatore().getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		switch (rt.getSoggettoPagatore().getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco()) {
		case F:
			identificativoUnivocoPagatore.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
			break;
		case G:
			identificativoUnivocoPagatore.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.G);
			break;
		}
		soggettoPagatore.setIdentificativoUnivocoPagatore(identificativoUnivocoPagatore );
		soggettoPagatore.setIndirizzoPagatore(rt.getSoggettoPagatore().getIndirizzoPagatore());
		soggettoPagatore.setLocalitaPagatore(rt.getSoggettoPagatore().getLocalitaPagatore());
		soggettoPagatore.setNazionePagatore(rt.getSoggettoPagatore().getNazionePagatore());
		soggettoPagatore.setProvinciaPagatore(rt.getSoggettoPagatore().getProvinciaPagatore());
		ricevutaTelematica.setSoggettoPagatore(soggettoPagatore);
	}

	private static void popolaSoggettoVersanteRT23(CtRicevutaTelematica ricevutaTelematica,
			it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica rt) {
		if(rt.getSoggettoVersante() != null) {
			CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
			soggettoVersante.setAnagraficaVersante(rt.getSoggettoVersante().getAnagraficaVersante());
			soggettoVersante.setCapVersante(rt.getSoggettoVersante().getCapVersante());
			soggettoVersante.setCivicoVersante(rt.getSoggettoVersante().getCivicoVersante());
			soggettoVersante.setEMailVersante(rt.getSoggettoVersante().getEMailVersante());
			CtIdentificativoUnivocoPersonaFG identificativoUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
			identificativoUnivocoVersante.setCodiceIdentificativoUnivoco(rt.getSoggettoVersante().getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
			switch (rt.getSoggettoVersante().getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco()) {
			case F:
				identificativoUnivocoVersante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
				break;
			case G:
				identificativoUnivocoVersante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.G);
				break;
			}
			soggettoVersante.setIdentificativoUnivocoVersante(identificativoUnivocoVersante );
			soggettoVersante.setIndirizzoVersante(rt.getSoggettoVersante().getIndirizzoVersante());
			soggettoVersante.setLocalitaVersante(rt.getSoggettoVersante().getLocalitaVersante());
			soggettoVersante.setNazioneVersante(rt.getSoggettoVersante().getNazioneVersante());
			soggettoVersante.setProvinciaVersante(rt.getSoggettoVersante().getProvinciaVersante());
			ricevutaTelematica.setSoggettoVersante(soggettoVersante);
		}
	}

	private static void popoloaParametriPSP(CtRicevutaTelematica ricevutaTelematica, Rpt rpt) {
		CtParametriPSP parametriPSP = new CtParametriPSP();
		parametriPSP.setIdentificativoCanale(rpt.getCodCanale());
		parametriPSP.setIdentificativoIntermediarioPSP(rpt.getCodIntermediarioPsp());
		parametriPSP.setIdentificativoPSP(rpt.getCodPsp());
		ricevutaTelematica.setParametriPSP(parametriPSP);
	}

	private static void popolaIstitutoAttestanteRT23(CtRicevutaTelematica ricevutaTelematica,
			it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica rt) {
		CtIstitutoAttestante istitutoAttestante = new CtIstitutoAttestante();
		istitutoAttestante.setCapAttestante(rt.getIstitutoAttestante().getCapAttestante());
		istitutoAttestante.setCivicoAttestante(rt.getIstitutoAttestante().getCivicoAttestante());
		istitutoAttestante.setCodiceUnitOperAttestante(rt.getIstitutoAttestante().getCodiceUnitOperAttestante());
		istitutoAttestante.setDenominazioneAttestante(rt.getIstitutoAttestante().getDenominazioneAttestante());
		istitutoAttestante.setDenomUnitOperAttestante(rt.getIstitutoAttestante().getDenomUnitOperAttestante());
		CtIdentificativoUnivoco identificativoUnivocoAttestante = new CtIdentificativoUnivoco();
		identificativoUnivocoAttestante.setCodiceIdentificativoUnivoco(rt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
		switch (rt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco()) {
		case A:
			identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
			break;
		case B:
			identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.B);
			break;
		case G:
			identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.G);
			break;
		}
		istitutoAttestante.setIdentificativoUnivocoAttestante(identificativoUnivocoAttestante );
		istitutoAttestante.setIndirizzoAttestante(rt.getIstitutoAttestante().getIndirizzoAttestante());
		istitutoAttestante.setLocalitaAttestante(rt.getIstitutoAttestante().getLocalitaAttestante());
		istitutoAttestante.setNazioneAttestante(rt.getIstitutoAttestante().getNazioneAttestante());
		istitutoAttestante.setProvinciaAttestante(rt.getIstitutoAttestante().getProvinciaAttestante());
		ricevutaTelematica.setIstitutoAttestante(istitutoAttestante);
	}

	private static void popolaEnteBeneficiarioRT(CtRicevutaTelematica ricevutaTelematica, Rpt rpt, Versamento versamento,
			BDConfigWrapper configWrapper) throws ServiceException {
		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		Dominio dominio = rpt.getDominio(configWrapper);
		enteBeneficiario.setCapBeneficiario(dominio.getAnagrafica().getCap());
		enteBeneficiario.setCivicoBeneficiario(dominio.getAnagrafica().getCivico());

		enteBeneficiario.setDenominazioneBeneficiario(dominio.getAnagrafica().getRagioneSociale());

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null) {
			if(!uo.getCodUo().equals(it.govpay.model.Dominio.EC)) {
				enteBeneficiario.setCodiceUnitOperBeneficiario(uo.getCodUo());
				enteBeneficiario.setDenomUnitOperBeneficiario(uo.getAnagrafica().getRagioneSociale());
			}
		}

		CtIdentificativoUnivocoPersonaG identificativoUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		identificativoUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getAnagrafica().getCodUnivoco());
		identificativoUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(identificativoUnivocoBeneficiario );
		enteBeneficiario.setIndirizzoBeneficiario(dominio.getAnagrafica().getIndirizzo());
		enteBeneficiario.setLocalitaBeneficiario(dominio.getAnagrafica().getLocalita());
		enteBeneficiario.setNazioneBeneficiario(dominio.getAnagrafica().getNazione());
		enteBeneficiario.setProvinciaBeneficiario(dominio.getAnagrafica().getProvincia());
		ricevutaTelematica.setEnteBeneficiario(enteBeneficiario );
	}

	private static void popolaRicevutaDaRPT24(CtRicevutaTelematica ricevutaTelematica, Rpt rpt, Versamento versamento, List<SingoloVersamento> singoliVersamenti, BDConfigWrapper configWrapper) throws DatatypeConfigurationException, JAXBException, SAXException, ServiceException {
		PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);

		ricevutaTelematica.setDataOraMessaggioRicevuta(impostaDataOperazione(rpt.getDataMsgRicevuta()));
		popolaDatiVersamentoRT(ricevutaTelematica, rpt);
		popolaEnteBeneficiarioRT(ricevutaTelematica, rpt, versamento, configWrapper);
		ricevutaTelematica.setIdDominio(rpt.getCodDominio());
		ricevutaTelematica.setIdentificativoMessaggioRicevuta(paSendRTReq_RT.getReceipt().getReceiptId());

		popolaIstitutoAttestanteRT24(ricevutaTelematica, paSendRTReq_RT);
		popoloaParametriPSP(ricevutaTelematica, rpt);
		ricevutaTelematica.setRicevutaXML(new String(rpt.getXmlRt()));
		ricevutaTelematica.setRiferimentoDataRichiesta(impostaDataOperazione(rpt.getDataMsgRichiesta()));
		ricevutaTelematica.setRiferimentoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		popolaSoggettoPagatoreRT24(ricevutaTelematica, paSendRTReq_RT);
		popolaSoggettoVersanteRT24(ricevutaTelematica, paSendRTReq_RT);
	}

	private static void popolaSoggettoPagatoreRT24(CtRicevutaTelematica ricevutaTelematica, PaSendRTReq paSendRTReq_RT) {
		CtSoggettoPagatore soggettoPagatore = new CtSoggettoPagatore();
		CtReceipt receipt = paSendRTReq_RT.getReceipt();
		CtSubject debtor = receipt.getDebtor();
		soggettoPagatore.setAnagraficaPagatore(debtor.getFullName());
		soggettoPagatore.setCapPagatore(debtor.getPostalCode());
		soggettoPagatore.setCivicoPagatore(debtor.getCivicNumber());
		soggettoPagatore.setEMailPagatore(debtor.getEMail());
		CtIdentificativoUnivocoPersonaFG identificativoUnivocoPagatore = new CtIdentificativoUnivocoPersonaFG();
		identificativoUnivocoPagatore.setCodiceIdentificativoUnivoco(debtor.getUniqueIdentifier().getEntityUniqueIdentifierValue());
		switch (debtor.getUniqueIdentifier().getEntityUniqueIdentifierType()) {
		case F:
			identificativoUnivocoPagatore.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
			break;
		case G:
			identificativoUnivocoPagatore.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.G);
			break;
		}
		soggettoPagatore.setIdentificativoUnivocoPagatore(identificativoUnivocoPagatore );
		soggettoPagatore.setIndirizzoPagatore(debtor.getStreetName());
		soggettoPagatore.setLocalitaPagatore(debtor.getCity());
		soggettoPagatore.setNazionePagatore(debtor.getCountry());
		soggettoPagatore.setProvinciaPagatore(debtor.getStateProvinceRegion());
		ricevutaTelematica.setSoggettoPagatore(soggettoPagatore);
	}

	private static void popolaSoggettoVersanteRT24(CtRicevutaTelematica ricevutaTelematica, PaSendRTReq paSendRTReq_RT) {
		CtReceipt receipt = paSendRTReq_RT.getReceipt();
		CtSubject payer = receipt.getPayer();
		if(payer != null) {
			CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
			soggettoVersante.setAnagraficaVersante(payer.getFullName());
			soggettoVersante.setCapVersante(payer.getPostalCode());
			soggettoVersante.setCivicoVersante(payer.getCivicNumber());
			soggettoVersante.setEMailVersante(payer.getEMail());
			CtIdentificativoUnivocoPersonaFG identificativoUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
			identificativoUnivocoVersante.setCodiceIdentificativoUnivoco(payer.getUniqueIdentifier().getEntityUniqueIdentifierValue());
			switch (payer.getUniqueIdentifier().getEntityUniqueIdentifierType()) {
			case F:
				identificativoUnivocoVersante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
				break;
			case G:
				identificativoUnivocoVersante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.G);
				break;
			}
			soggettoVersante.setIdentificativoUnivocoVersante(identificativoUnivocoVersante );
			soggettoVersante.setIndirizzoVersante(payer.getStreetName());
			soggettoVersante.setLocalitaVersante(payer.getCity());
			soggettoVersante.setNazioneVersante(payer.getCountry());
			soggettoVersante.setProvinciaVersante(payer.getStateProvinceRegion());
			ricevutaTelematica.setSoggettoVersante(soggettoVersante);
		}
	}

	private static void popolaIstitutoAttestanteRT24(CtRicevutaTelematica ricevutaTelematica, PaSendRTReq paSendRTReq_RT) {
		CtIstitutoAttestante istitutoAttestante = new CtIstitutoAttestante();
		istitutoAttestante.setDenominazioneAttestante(paSendRTReq_RT.getReceipt().getPSPCompanyName());
		CtIdentificativoUnivoco identificativoUnivocoAttestante = new CtIdentificativoUnivoco();
		identificativoUnivocoAttestante.setCodiceIdentificativoUnivoco(paSendRTReq_RT.getReceipt().getPspFiscalCode());
		//		switch (rt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco()) {
		//		case A:
		//			identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.A);
		//			break;
		//		case B:
		//			identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.B);
		//			break;
		//		case G:
		identificativoUnivocoAttestante.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivoco.G);
		//			break;
		//		}
		istitutoAttestante.setIdentificativoUnivocoAttestante(identificativoUnivocoAttestante );
		ricevutaTelematica.setIstitutoAttestante(istitutoAttestante);
	}

	private static void popolaDatiVersamentoRT(CtRicevutaTelematica ricevutaTelematica, Rpt rpt) {
		CtDatiVersamentoRT datiPagamento = new CtDatiVersamentoRT();
		datiPagamento.setCodiceContestoPagamento(rpt.getCcp());
		datiPagamento.setCodiceEsitoPagamento(rpt.getEsitoPagamento().getCodifica()+"");
		datiPagamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		datiPagamento.setImportoTotalePagato(rpt.getImportoTotalePagato());
		switch(rpt.getTipoVersamento()) {
		case ADDEBITO_DIRETTO:
			datiPagamento.setTipoVersamento(StTipoVersamento.AD);
			break;
		case ATTIVATO_PRESSO_PSP:
			datiPagamento.setTipoVersamento(StTipoVersamento.PO);
			break;
		case BOLLETTINO_POSTALE:
			datiPagamento.setTipoVersamento(StTipoVersamento.BP);
			break;
		case BONIFICO_BANCARIO_TESORERIA:
			datiPagamento.setTipoVersamento(StTipoVersamento.BBT);
			break;
		case CARTA_PAGAMENTO:
			datiPagamento.setTipoVersamento(StTipoVersamento.CP);
			break;
		case MYBANK:
			datiPagamento.setTipoVersamento(StTipoVersamento.OBEP);
			break;
		case OTHER:
			datiPagamento.setTipoVersamento(StTipoVersamento.OTH);
			break;
		}
		ricevutaTelematica.setDatiPagamento(datiPagamento);
	}
}
