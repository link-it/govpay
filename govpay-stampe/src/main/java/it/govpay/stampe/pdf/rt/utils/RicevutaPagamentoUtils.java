package it.govpay.stampe.pdf.rt.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.model.Anagrafica;
import it.govpay.model.Dominio;
import it.govpay.model.Pagamento;
import it.govpay.model.RicevutaPagamento;
import it.govpay.model.Versamento;
import it.govpay.model.Versamento.Causale;
import it.govpay.stampe.pdf.rt.IRicevutaPagamento;
import it.govpay.stampe.pdf.rt.factory.RicevutaPagamentoFactory;

public class RicevutaPagamentoUtils {

	public static List<String> getPdfRicevutaPagamento(byte[] logoDominio, CtRicevutaTelematica rt,Versamento v,String auxDigit, String applicationCode, OutputStream osAPPdf, Logger log) throws Exception {

		RicevutaPagamento ricevuta = new RicevutaPagamento();
		
		CtEnteBeneficiario enteBeneficiario = rt.getEnteBeneficiario();
		Dominio dominioCreditore = new Dominio();
		dominioCreditore.setCodDominio(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
		dominioCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
		Anagrafica anagraficaCreditore = new Anagrafica();
		anagraficaCreditore.setCap(enteBeneficiario.getCapBeneficiario());
		anagraficaCreditore.setCivico(enteBeneficiario.getCivicoBeneficiario());
		anagraficaCreditore.setCodUnivoco(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
		anagraficaCreditore.setIndirizzo(enteBeneficiario.getIndirizzoBeneficiario());
		anagraficaCreditore.setLocalita(enteBeneficiario.getLocalitaBeneficiario());
		anagraficaCreditore.setNazione(enteBeneficiario.getNazioneBeneficiario());
		anagraficaCreditore.setProvincia(enteBeneficiario.getProvinciaBeneficiario());
		anagraficaCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
		
		ricevuta.setDominioCreditore(dominioCreditore);
		ricevuta.setCodDominio(dominioCreditore.getCodDominio());
		
		ricevuta.setAnagraficaCreditore(anagraficaCreditore);
		
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		ricevuta.setCodAvviso(auxDigit+applicationCode+ datiPagamento.getIdentificativoUnivocoVersamento());
		ricevuta.setIuv(datiPagamento.getIdentificativoUnivocoVersamento());
		
		ricevuta.setCcp(datiPagamento.getCodiceContestoPagamento());
		ricevuta.setImportoPagato(datiPagamento.getImportoTotalePagato());
		
		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
		if(datiSingoloPagamento!= null && datiSingoloPagamento.size() >0){
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0);
			ricevuta.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
			ricevuta.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione()); 
			ricevuta.setCausale(ctDatiSingoloPagamentoRT.getCausaleVersamento()); 
			ricevuta.setCommissioni(ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP());
		}
		
		ricevuta.setDataScadenza(v.getDataScadenza());
		ricevuta.setImportoDovuto(v.getImportoTotale());
		ricevuta.setDescrizioneCausale(v.getCausaleVersamento() != null ? v.getCausaleVersamento().getSimple() : ""); 
		
		if(rt.getIstitutoAttestante() != null){
			CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();
			Anagrafica anagraficaArttestante = new Anagrafica();
			anagraficaArttestante.setCodUnivoco(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			anagraficaArttestante.setRagioneSociale(istitutoAttestante.getDenominazioneAttestante());
			ricevuta.setAnagraficaAttestante(anagraficaArttestante);
			ricevuta.setPsp(istitutoAttestante.getDenominazioneAttestante());
		}
		
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		anagraficaDebitore.setRagioneSociale(soggettoPagatore.getAnagraficaPagatore());
		ricevuta.setAnagraficaDebitore(anagraficaDebitore);
		
		if(rt.getSoggettoVersante() != null){
			CtSoggettoVersante soggettoVersante = rt.getSoggettoVersante();
			Anagrafica anagraficaVersante = new Anagrafica();
			anagraficaVersante.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
			anagraficaVersante.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
			ricevuta.setAnagraficaVersante(anagraficaVersante);
		}
		ricevuta.setLogoDominioCreditore(logoDominio);
		
		return getPdfRicevutaPagamento( ricevuta, osAPPdf, log);
	}
	
	public static List<String> getPdfRicevutaPagamento(byte[] logoDominio, Causale causale, CtRicevutaTelematica rt,Pagamento p,String auxDigit, String applicationCode, OutputStream osAPPdf, Logger log) throws Exception {

		RicevutaPagamento ricevuta = new RicevutaPagamento();
		
		CtEnteBeneficiario enteBeneficiario = rt.getEnteBeneficiario();
		Dominio dominioCreditore = new Dominio();
		dominioCreditore.setCodDominio(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
		dominioCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
		Anagrafica anagraficaCreditore = new Anagrafica();
		anagraficaCreditore.setCap(enteBeneficiario.getCapBeneficiario());
		anagraficaCreditore.setCivico(enteBeneficiario.getCivicoBeneficiario());
		anagraficaCreditore.setCodUnivoco(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
		anagraficaCreditore.setIndirizzo(enteBeneficiario.getIndirizzoBeneficiario());
		anagraficaCreditore.setLocalita(enteBeneficiario.getLocalitaBeneficiario());
		anagraficaCreditore.setNazione(enteBeneficiario.getNazioneBeneficiario());
		anagraficaCreditore.setProvincia(enteBeneficiario.getProvinciaBeneficiario());
		anagraficaCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
		
		ricevuta.setDominioCreditore(dominioCreditore);
		ricevuta.setCodDominio(dominioCreditore.getCodDominio());
		
		ricevuta.setAnagraficaCreditore(anagraficaCreditore);
		
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		ricevuta.setCodAvviso(auxDigit+applicationCode+ datiPagamento.getIdentificativoUnivocoVersamento());
		ricevuta.setIuv(datiPagamento.getIdentificativoUnivocoVersamento());
		
		ricevuta.setCcp(datiPagamento.getCodiceContestoPagamento());
		ricevuta.setImportoPagato(datiPagamento.getImportoTotalePagato());
		
		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
		if(datiSingoloPagamento!= null && datiSingoloPagamento.size() >0){
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0);
			ricevuta.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
			ricevuta.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione()); 
			ricevuta.setCausale(ctDatiSingoloPagamentoRT.getCausaleVersamento());
			ricevuta.setCommissioni(ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP());
//			ricevuta.setCausale(v.getCausaleVersamento() != null ? v.getCausaleVersamento().getSimple() : ctDatiSingoloPagamentoRT.getCausaleVersamento()); 
		}
		
//		ricevuta.setDataScadenza(v.getDataScadenza());
//		ricevuta.setImportoDovuto(v.getImportoTotale());
		if(rt.getIstitutoAttestante() != null){
			CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();
			Anagrafica anagraficaArttestante = new Anagrafica();
			anagraficaArttestante.setCodUnivoco(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			anagraficaArttestante.setRagioneSociale(istitutoAttestante.getDenominazioneAttestante());
			ricevuta.setAnagraficaAttestante(anagraficaArttestante);
			ricevuta.setPsp(istitutoAttestante.getDenominazioneAttestante());
		}
		
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		anagraficaDebitore.setRagioneSociale(soggettoPagatore.getAnagraficaPagatore());
		ricevuta.setAnagraficaDebitore(anagraficaDebitore);
		
		if(rt.getSoggettoVersante() != null){
			CtSoggettoVersante soggettoVersante = rt.getSoggettoVersante();
			Anagrafica anagraficaVersante = new Anagrafica();
			anagraficaVersante.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
			anagraficaVersante.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
			ricevuta.setAnagraficaVersante(anagraficaVersante);
		}
		
		ricevuta.setLogoDominioCreditore(logoDominio);
		
		return getPdfRicevutaPagamento(ricevuta, osAPPdf, log);
	}
	
//	public static List<String> getPdfRicevutaPagamento(String pathLoghi, CtRicevutaTelematica rt,GpChiediStatoVersamentoResponse chiediStatoVersamentoResponse,String auxDigit, String applicationCode, OutputStream osAPPdf, Logger log) throws Exception {
//
//		RicevutaPagamento ricevuta = new RicevutaPagamento();
//		
//		CtEnteBeneficiario enteBeneficiario = rt.getEnteBeneficiario();
//		Dominio dominioCreditore = new Dominio();
//		dominioCreditore.setCodDominio(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
//		dominioCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
//		Anagrafica anagraficaCreditore = new Anagrafica();
//		anagraficaCreditore.setCap(enteBeneficiario.getCapBeneficiario());
//		anagraficaCreditore.setCivico(enteBeneficiario.getCivicoBeneficiario());
//		anagraficaCreditore.setCodUnivoco(enteBeneficiario.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco());
//		anagraficaCreditore.setIndirizzo(enteBeneficiario.getIndirizzoBeneficiario());
//		anagraficaCreditore.setLocalita(enteBeneficiario.getLocalitaBeneficiario());
//		anagraficaCreditore.setNazione(enteBeneficiario.getNazioneBeneficiario());
//		anagraficaCreditore.setProvincia(enteBeneficiario.getProvinciaBeneficiario());
//		anagraficaCreditore.setRagioneSociale(enteBeneficiario.getDenominazioneBeneficiario());
//		
//		ricevuta.setDominioCreditore(dominioCreditore);
//		ricevuta.setCodDominio(dominioCreditore.getCodDominio());
//		ricevuta.setAnagraficaCreditore(anagraficaCreditore);
//		
//		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
//		ricevuta.setCodAvviso(auxDigit+applicationCode+ datiPagamento.getIdentificativoUnivocoVersamento());
//		ricevuta.setIuv(datiPagamento.getIdentificativoUnivocoVersamento());
//		
//		ricevuta.setCcp(datiPagamento.getCodiceContestoPagamento());
//		ricevuta.setImportoPagato(datiPagamento.getImportoTotalePagato());
//		
//		List<CtDatiSingoloPagamentoRT> datiSingoloPagamento = datiPagamento.getDatiSingoloPagamento();
//		if(datiSingoloPagamento!= null && datiSingoloPagamento.size() >0){
//			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiSingoloPagamento.get(0);
//			ricevuta.setDataPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
//			ricevuta.setIdRiscossione(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione()); 
//			ricevuta.setCausale(chiediStatoVersamentoResponse.getCausale() != null ? chiediStatoVersamentoResponse.getCausale() : ctDatiSingoloPagamentoRT.getCausaleVersamento()); 
//			ricevuta.setCommissioni(ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP());
//		}
//		
//		ricevuta.setDataScadenza(chiediStatoVersamentoResponse.getDataScadenza());
//		ricevuta.setImportoDovuto(chiediStatoVersamentoResponse.getImportoTotale());
//		
//		if(rt.getIstitutoAttestante() != null){
//			CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();
//			Anagrafica anagraficaArttestante = new Anagrafica();
//			anagraficaArttestante.setCodUnivoco(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
//			anagraficaArttestante.setRagioneSociale(istitutoAttestante.getDenominazioneAttestante());
//			ricevuta.setAnagraficaAttestante(anagraficaArttestante);
//			ricevuta.setPsp(istitutoAttestante.getDenominazioneAttestante());
//		}
//		
//		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
//		Anagrafica anagraficaDebitore = new Anagrafica();
//		anagraficaDebitore.setCodUnivoco(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
//		anagraficaDebitore.setRagioneSociale(soggettoPagatore.getAnagraficaPagatore());
//		ricevuta.setAnagraficaDebitore(anagraficaDebitore);
//		
//		if(rt.getSoggettoVersante() != null){
//			CtSoggettoVersante soggettoVersante = rt.getSoggettoVersante();
//			Anagrafica anagraficaVersante = new Anagrafica();
//			anagraficaVersante.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
//			anagraficaVersante.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
//			ricevuta.setAnagraficaVersante(anagraficaVersante);
//		}
//		
//		return getPdfRicevutaPagamento(pathLoghi, ricevuta, osAPPdf, log);
//	}
	
	public static List<String> getPdfRicevutaPagamento(RicevutaPagamento ricevuta, OutputStream osAPPdf, Logger log) throws Exception {
		List<String> msgs = new ArrayList<String>();
		try{
			// 1. Prelevo le properties
			String codDominio = ricevuta.getDominioCreditore().getCodDominio();
			RicevutaPagamentoProperties ricevutaPagamentoProperties = RicevutaPagamentoProperties.getInstance();
			Properties propertiesAvvisoPagamentoDominioTributo = getRicevutaPagamentoPropertiesPerDominioTributo(ricevutaPagamentoProperties, codDominio, log);

			IRicevutaPagamento ricevutaPagamentoBuilder = RicevutaPagamentoFactory.getRicevutaPagamentoBuilder(propertiesAvvisoPagamentoDominioTributo.getProperty(RicevutaPagamentoProperties.RICEVUTA_PAGAMENTO_CLASSNAME_PROP_KEY), log);

			msgs.add(ricevutaPagamentoBuilder.getPdfRicevutaPagamento(ricevuta, propertiesAvvisoPagamentoDominioTributo, osAPPdf, log));

			return msgs;
		}catch(Exception e){
			log.error(e,e);
			throw e;
		}
	}

	public static Properties getRicevutaPagamentoPropertiesPerDominioTributo(RicevutaPagamentoProperties ricevutaPagamentoProperties,String codDominio,Logger log) throws Exception {
		Properties p = null;
		String key = null;
		// Ricerca per codDominio
		if(p == null && StringUtils.isNotBlank(codDominio)){
			key = codDominio;
			try{
				log.debug("Ricerca delle properties per la chiave ["+key+"]");
				p = ricevutaPagamentoProperties.getProperties(key);
			}catch(Exception e){
				log.debug("Non sono state trovate properties per la chiave ["+key+"]: " + e.getMessage()); 
			}
		}

		// utilizzo le properties di default
		try{
			log.debug("Ricerca delle properties di default");
			p = ricevutaPagamentoProperties.getProperties(null);
		}catch(Exception e){
			log.debug("Non sono state trovate properties di default: " + e.getMessage());
			throw e;
		}

		return p;
	}
}
