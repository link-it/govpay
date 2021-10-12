package it.govpay.core.utils.tracciati;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.resources.Charset;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivoco;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.business.TracciatiNotificaPagamenti;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Contabilita;
import it.govpay.model.QuotaContabilita;
import it.govpay.model.Rpt.Versione;
import it.govpay.model.TipoVersamento;

public class TracciatiNotificaPagamentiUtils {
	
	private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generaIdentificativoTracciato() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

	
	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, Charset.UTF_8.getValue());
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static String getURLDownloadTracciato(ConnettoreNotificaPagamenti connettore, TracciatoNotificaPagamenti tracciato) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(connettore.getDownloadBaseURL());
		
		if(!connettore.getDownloadBaseURL().endsWith("/"))
			sb.append("/");
		
		sb.append(tracciato.getId());
		
		sb.append("?").append("secID").append("=").append(tracciato.getIdentificativo());
		
		return sb.toString();
		
	}
	
	
	public static String creaNomeEntryFlussoRendicontazione(String idFlusso, String dataFlussoS) {
//		return TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX+idFlusso+".xml";
		return TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX+idFlusso+"_"+dataFlussoS+".xml";
	}

	public static String creaNomeEntryRT(String idDominio, String iuv, String ccp) {
		return TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX+idDominio +"_"+ iuv + "_"+ ccp +TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML;
	}
	
	public static String creaNomeEntryRPT(String idDominio, String iuv, String ccp) {
		return TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX+idDominio +"_"+ iuv + "_"+ ccp +TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML;
	}
	
	
	public static String creaPathFlussoRendicontazione(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(".xml")) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".xml"));
		}
		
		fileName = fileName.substring(0, fileName.lastIndexOf("_"));
		
		return fileName;
	}
	
	public static String getDataFlussoRendicontazione(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(".xml")) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".xml"));
		}
		
		fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
		
		return fileName;
	}

	public static String creaPathRT(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML)) {
			fileName = fileName.substring(0, fileName.lastIndexOf(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML));
		}
		
		return fileName.replace('_', '/');
	}
	
	public static String creaPathRPT(String fileName) {
		if(fileName.startsWith(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX)) {
			fileName = fileName.substring(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX.length());
		}
		
		if(fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML)) {
			fileName = fileName.substring(0, fileName.lastIndexOf(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML));
		}
		
		return fileName.replace('_', '/');
	}
	
	public static boolean isRPT(String fileName) {
		return (fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RPT_XML));
	}
	
	public static boolean isRT(String fileName) {
		return (fileName.endsWith(TracciatiNotificaPagamenti.SUFFIX_FILE_RT_XML));
	}
	
	public static String completaValoreCampoConFiller(Logger log, String entryKey, String nomeCampo, String valoreCampo, int dimensioneTotaleCampo, boolean numerico, boolean left) {
		String filler = " ";
		if(numerico) {
			filler = "0";
		} 
		
		if(valoreCampo == null) {
			valoreCampo = "";
		}
		
		String tmp = left ? StringUtils.leftPad(valoreCampo, dimensioneTotaleCampo, filler) : StringUtils.rightPad(valoreCampo, dimensioneTotaleCampo, filler);

		return impostaLunghezzaMassimaCampo(log, entryKey, nomeCampo, tmp, dimensioneTotaleCampo);
	}
	
	public static String rimuoviCaratteriDaStringa(Logger log, String entryKey, String nomeCampo, String valoreCampo, String tokenToDelete) {
		return sostituisciCaratteriDaStringa(log, entryKey, nomeCampo, valoreCampo, tokenToDelete, "");
	}
	
	public static String sostituisciCaratteriDaStringa(Logger log, String entryKey, String nomeCampo, String valoreCampo, String tokenToDelete, String tokenToReplace) {
		if(valoreCampo != null) {
			if(valoreCampo.contains(tokenToDelete)) {
				String nuovoValoreCampo = valoreCampo.replace(tokenToDelete, tokenToReplace);
				log.warn("Entry ["+entryKey+"]: Campo ["+nomeCampo+"], valore ["+valoreCampo+"] contiene il token ["+ tokenToDelete+"], tutte le occorrenza vengono sostituite con il token ["+tokenToReplace+"]: ["+nuovoValoreCampo+"].");
				return nuovoValoreCampo;
			}
		}
		
		return valoreCampo;
	}

	public static String impostaLunghezzaMassimaCampo(Logger log, String entryKey, String nomeCampo, String valoreCampo, int dimensioneTotaleCampo) {
		if(valoreCampo == null) {
			valoreCampo = "";
		}
		
		if(valoreCampo.length() > dimensioneTotaleCampo) {
			String nuovoValoreCampo = valoreCampo.substring(0,dimensioneTotaleCampo);
			log.warn("Entry ["+entryKey+"]: Campo ["+nomeCampo+"], valore ["+valoreCampo+"] lunghezza ["+ valoreCampo.length()+"] viene troncato alla dimensione massima consentita ["+dimensioneTotaleCampo+"]: ["+nuovoValoreCampo+"].");
			return nuovoValoreCampo;
		}
		
		return valoreCampo;
	}
	
	public static boolean validaCampo(String nomeCampo, String valoreCampo, int dimensioneCampo) throws ValidationException {
		if(valoreCampo.length() != dimensioneCampo) {
			throw new ValidationException("Il valore contenuto nel campo [" + nomeCampo + "] non rispetta la lunghezza previsti [" + dimensioneCampo + "], trovati [" + valoreCampo.length() + "]");
		}
		
		return true;
	}
	
	public static String printImporto(BigDecimal value, boolean removeDecimalSeparator) {
		DecimalFormatSymbols custom=new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(custom);
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		
		String formatValue = format.format(value);
		
		if(removeDecimalSeparator) {
			formatValue = formatValue.replace(".", "");
		}
		
		return formatValue;
	}
	
	public static List<String> aggiungiCampiVuoti(int numero){
		List<String> lst = new ArrayList<>();
		for (int i = 0; i < numero; i++) {
			lst.add("");
		}
		
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public static String [] creaLineaCsvMyPivotRpt_SANP23(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<String> linea = new ArrayList<String>();

		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);

		String contabilitaString = singoloVersamento.getContabilita();
		String tipoDovuto = null;
		String bilancio = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta tipoDovuto
						if(parse.containsKey("tipoDovuto")) {
							tipoDovuto = (String) parse.get("tipoDovuto");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta tipoDovuto
					if(parse.containsKey("tipoDovuto")) {
						tipoDovuto = (String) parse.get("tipoDovuto");
					}
				}
			}
			
			// bilancio a partire dalle quote ricevute nell'oggetto contabilita'
			if(contabilita.getQuote() != null && contabilita.getQuote().size() > 0) {
				StringBuilder sb = new StringBuilder();
				
				sb.append("<bilancio>");
				for (QuotaContabilita quota : contabilita.getQuote()) {
					sb.append("<capitolo>");
					
					sb.append("<codice>");
					sb.append(quota.getCapitolo());
					sb.append("</codice>");
					
					sb.append("<importo>");
					sb.append(TracciatiNotificaPagamentiUtils.printImporto(quota.getImporto(), false));
					sb.append("</importo>");
					
					sb.append("</capitolo>");
				}
				sb.append("</bilancio>");
				
				bilancio = sb.toString();
			}
			
		}

		// IUD cod_applicazione@cod_versamento_ente
		linea.add(applicazione.getCodApplicazione() + "@" + versamento.getCodVersamentoEnte());
		// codIuv: rt.datiPagamento.identificativoUnivocoVersamento
		linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
		// tipoIdentificativoUnivoco: rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.tipoIdentificativoUnivoco
		linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco().value());
		// codiceIdentificativoUnivoco: rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.codiceIdentificativoUnivoco
		linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		// anagraficaPagatore: rt.datiPagamento.soggettoPagatore.anagraficaPagatore
		linea.add(soggettoPagatore.getAnagraficaPagatore());
		// indirizzoPagatore: rt.datiPagamento.soggettoPagatore.indirizzoPagatore
		linea.add(soggettoPagatore.getIndirizzoPagatore());
		// civicoPagatore: rt.datiPagamento.soggettoPagatore.civicoPagatore
		linea.add(soggettoPagatore.getCivicoPagatore());
		// capPagatore: rt.datiPagamento.soggettoPagatore.capPagatore
		linea.add(soggettoPagatore.getCapPagatore());
		// localitaPagatore: rt.datiPagamento.soggettoPagatore.localitaPagatore
		linea.add(soggettoPagatore.getLocalitaPagatore());
		// provinciaPagatore: rt.datiPagamento.soggettoPagatore.provinciaPagatore
		linea.add(soggettoPagatore.getProvinciaPagatore());
		// nazionePagatore: rt.datiPagamento.soggettoPagatore.nazionePagatore
		linea.add(soggettoPagatore.getNazionePagatore());
		// e-mailPagatore: rt.datiPagamento.soggettoPagatore.e-mailPagatore
		linea.add(soggettoPagatore.getEMailPagatore());
		// dataEsecuzionePagamento: rt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento()));
		// importoDovutoPagato: rt.datiPagamento.importoTotalePagato
		linea.add(TracciatiNotificaPagamentiUtils.printImporto(datiPagamento.getImportoTotalePagato(), false));
		// commissioneCaricoPa: vuoto
		linea.add("");
		// tipoDovuto: versamento.datiAllegati.mypivot.tipoDovuto o versamento.tassonomiaEnte o versamento.codTipoPendenza
		if(tipoDovuto == null) {
			tipoDovuto = StringUtils.isNotBlank(versamento.getTassonomiaAvviso()) 
					? versamento.getTassonomiaAvviso() : versamento.getTipoVersamento(configWrapper).getCodTipoVersamento();
		}
		linea.add(tipoDovuto);
		// tipoVersamento: vuoto
		linea.add("");
		// causaleVersamento: rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
		linea.add(ctDatiSingoloPagamentoRT.getCausaleVersamento());
		// datiSpecificiRiscossione: rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
		linea.add(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
		// bilancio: versamento.datiAllegati.mypivot.bilancio o vuoto
		linea.add(bilancio != null ? bilancio : "");

		return linea.toArray(new String[linea.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public static String [] creaLineaCsvMyPivotRpt_SANP24(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<String> linea = new ArrayList<String>();

		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
		CtReceipt ctReceipt = paSendRTReq_RT.getReceipt();
		CtSubject soggettoPagatore = ctReceipt.getDebtor();
		CtTransferPA ctTransferPA = ctReceipt.getTransferList().getTransfer().get(0);

		String contabilitaString = singoloVersamento.getContabilita();
		String tipoDovuto = null;
		String bilancio = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta tipoDovuto
						if(parse.containsKey("tipoDovuto")) {
							tipoDovuto = (String) parse.get("tipoDovuto");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta tipoDovuto
					if(parse.containsKey("tipoDovuto")) {
						tipoDovuto = (String) parse.get("tipoDovuto");
					}
				}
			}
			
			// bilancio a partire dalle quote ricevute nell'oggetto contabilita'
			if(contabilita.getQuote() != null && contabilita.getQuote().size() > 0) {
				StringBuilder sb = new StringBuilder();
				
				sb.append("<bilancio>");
				for (QuotaContabilita quota : contabilita.getQuote()) {
					sb.append("<capitolo>");
					
					sb.append("<codice>");
					sb.append(quota.getCapitolo());
					sb.append("</codice>");
					
					sb.append("<importo>");
					sb.append(TracciatiNotificaPagamentiUtils.printImporto(quota.getImporto(), false));
					sb.append("</importo>");
					
					sb.append("</capitolo>");
				}
				sb.append("</bilancio>");
				
				bilancio = sb.toString();
			}
			
		}

		// IUD cod_applicazione@cod_versamento_ente
		linea.add(applicazione.getCodApplicazione() + "@" + versamento.getCodVersamentoEnte());
		// codIuv: ctReceipt.getCreditorReferenceId
		linea.add(ctReceipt.getCreditorReferenceId());
		// tipoIdentificativoUnivoco: ctReceipt.debtor.uniqueIdentifier.entityUniqueIdentifierType
		linea.add(soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierType().value());
		// codiceIdentificativoUnivoco: ctReceipt.debtor.uniqueIdentifier.entityUniqueIdentifierValue
		linea.add(soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue());
		// anagraficaPagatore: ctReceipt.debtor.anagraficaPagatore
		linea.add(soggettoPagatore.getFullName());
		// indirizzoPagatore: ctReceipt.debtor.indirizzoPagatore
		linea.add(soggettoPagatore.getStreetName());
		// civicoPagatore: ctReceipt.debtor.civicoPagatore
		linea.add(soggettoPagatore.getCivicNumber());
		// capPagatore: ctReceipt.debtor.capPagatore
		linea.add(soggettoPagatore.getPostalCode());
		// localitaPagatore: ctReceipt.debtor.localitaPagatore
		linea.add(soggettoPagatore.getCity());
		// provinciaPagatore: ctReceipt.debtor.provinciaPagatore
		linea.add(soggettoPagatore.getStateProvinceRegion());
		// nazionePagatore: ctReceipt.debtor.nazionePagatore
		linea.add(soggettoPagatore.getCountry());
		// e-mailPagatore: ctReceipt.debtor.e-mailPagatore
		linea.add(soggettoPagatore.getEMail());
		// dataEsecuzionePagamento: rt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
		linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctReceipt.getPaymentDateTime()));
		// importoDovutoPagato: rt.datiPagamento.importoTotalePagato
		linea.add(TracciatiNotificaPagamentiUtils.printImporto(ctReceipt.getPaymentAmount(), false));
		// commissioneCaricoPa: vuoto
		linea.add("");
		// tipoDovuto: versamento.datiAllegati.mypivot.tipoDovuto o versamento.tassonomiaEnte o versamento.codTipoPendenza
		if(tipoDovuto == null) {
			tipoDovuto = StringUtils.isNotBlank(versamento.getTassonomiaAvviso()) 
					? versamento.getTassonomiaAvviso() : versamento.getTipoVersamento(configWrapper).getCodTipoVersamento();
		}
		linea.add(tipoDovuto);
		// tipoVersamento: vuoto
		linea.add("");
		// causaleVersamento: rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
		linea.add(ctTransferPA.getRemittanceInformation());
		// datiSpecificiRiscossione: rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
		linea.add(ctTransferPA.getTransferCategory());
		// bilancio: versamento.datiAllegati.mypivot.bilancio o vuoto
		linea.add(bilancio != null ? bilancio : "");

		return linea.toArray(new String[linea.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public static void creaLineaCsvSecimRpt_SANP23(Logger log, Rpt rpt, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore, OutputStream secimOS, OutputStream noSecimOS) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		StringBuilder sb = new StringBuilder();
		
		
		// NUOVA LINEA 
		if(numeroLinea > 1)
			sb.append("\n");

		String entryKey = "RPT_" + rpt.getCodDominio() + "_" + rpt.getIuv() + "_" + rpt.getCcp();
		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(0);
		CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();

		String contabilitaString = singoloVersamento.getContabilita();
		String riferimentoCreditore = null;
		String tipoflusso = null;
		String tipoRiferimentoCreditore = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta
						if(parse.containsKey("riferimentoCreditore")) {
							riferimentoCreditore = (String) parse.get("riferimentoCreditore");
						}
						if(parse.containsKey("tipoflusso")) {
							tipoflusso = (String) parse.get("tipoflusso");
						}
						if(parse.containsKey("tipoRiferimentoCreditore")) {
							tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta
					if(parse.containsKey("riferimentoCreditore")) {
						riferimentoCreditore = (String) parse.get("riferimentoCreditore");
					}
					if(parse.containsKey("tipoflusso")) {
						tipoflusso = (String) parse.get("tipoflusso");
					}
					if(parse.containsKey("tipoRiferimentoCreditore")) {
						tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
					}
				}
			}
		}
		
//		CODICE ISTITUTO	1	5	5	Numerico	5	0	SI	Codice in rt.istitutoAttestante.identificativoUnivocoAttestante.codiceIdentificativoUnivoco se rt.istitutoAttestante.identificativoUnivocoAttestante.tipoIdentificativoUnivoco == ‘A’
		String codiceIstituto = istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco();
		StTipoIdentificativoUnivoco tipoIdentificativoUnivocoATtestante = istitutoAttestante.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco();
		if(connettore.getCodiceIstituto() != null) {
			codiceIstituto = connettore.getCodiceIstituto();
		} else {
			switch (tipoIdentificativoUnivocoATtestante) {
			case A:
				codiceIstituto = istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco();
				break;
			case B:
			case G:
				codiceIstituto = "00000";
				break;
			}
		}
		
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE ISTITUTO", codiceIstituto, 5);
		sb.append(codiceIstituto);
		
//		CODICE CLIENTE	6	12	7	Numerico	7	0	SI	Codice Ente dal portale Ente Creditore
		String codiceCliente = connettore.getCodiceCliente(); 
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE CLIENTE", codiceCliente, 7);
		sb.append(codiceCliente);
		
//		FILLER	13	22	10	
		String filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 1", "", 10, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 1", filler, 10);
		sb.append(filler);
		
//		TIPO FLUSSO	23	30	8	Carattere			SI	Dato di configurazione assegnato da Poste alla PA datiallegati.tipoflusso
		if(tipoflusso == null) {
			tipoflusso = "NDP001C0";
		}
		tipoflusso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "TIPO FLUSSO", tipoflusso, 8, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO FLUSSO", tipoflusso, 8);
		sb.append(tipoflusso);
		
//		DATA CREAZIONE FLUSSO	31	38	8	Numerico	8	0	SI	Data Creazione di questo flusso?
		Date dataCreazioneFlusso = new Date();
		String dataCreazione = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(dataCreazioneFlusso);
		TracciatiNotificaPagamentiUtils.validaCampo("DATA CREAZIONE FLUSSO", dataCreazione, 8);
		sb.append(dataCreazione);
		
//		FILLER	39	77	39	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 2", "", 39, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 2", filler, 39);
		sb.append(filler);
		
//		PROGRESSIVO RECORD	78	90	13	Numerico	13	0		Numero di record incrementale
		String progressivoRecord = "" + numeroLinea;
		progressivoRecord = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "PROGRESSIVO RECORD", progressivoRecord, 13, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("PROGRESSIVO RECORD", progressivoRecord, 13);
		sb.append(progressivoRecord);
		
//		OPERAZIONE	91	93	3	Carattere			SI	nel pdf si parla di RIV nell’esempio c’e’ RIS
		String operazione = "RIS";
		TracciatiNotificaPagamentiUtils.validaCampo("OPERAZIONE", operazione, 3);
		sb.append(operazione);
		
//		FILLER	94	163	70	Carattere	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 3", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 3", filler, 70);
		sb.append(filler);
		
//		TIPO PRESENTAZIONE	164	169	6	Carattere	
		String tipoPresentazione = "BOL_PA";
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO PRESENTAZIONE", tipoPresentazione, 6);
		sb.append(tipoPresentazione);
		
//		CODICE PRESENTAZIONE	170	187	18	Carattere				versamento.numero_avviso
		String codicePresentazione = versamento.getNumeroAvviso() != null ? versamento.getNumeroAvviso() : "";
		codicePresentazione = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE PRESENTAZIONE", codicePresentazione, 18, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE PRESENTAZIONE", codicePresentazione, 18);
		sb.append(codicePresentazione);
		
//		FILLER	188	204	17	Carattere	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 4", "", 17, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 4", filler, 17);
		sb.append(filler);
		
//		IUV	205	239	35	Carattere				versamento.iuv
		String iuvVersamento = datiPagamento.getIdentificativoUnivocoVersamento();
		iuvVersamento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IUV", iuvVersamento, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("IUV", iuvVersamento, 35);
		sb.append(iuvVersamento);
		
//		RATA	240	274	35	Carattere				versamento.cod_rata
		String prefixRata = versamento.getNumeroRata() != null ? "S" : "T";
		Integer numeroRata = versamento.getNumeroRata() != null ? versamento.getNumeroRata() : 1;
		String rata = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RATA", numeroRata+"", 8, true, true); // Aggiungo zeri a sx fino ad arrivare a 8 caratteri
		rata = prefixRata + rata; // aggiungo prefisso
		rata = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RATA", rata, 35, false, false); // completo con spazi bianchi a dx fino a 35 caratteri
		TracciatiNotificaPagamentiUtils.validaCampo("RATA", rata, 35);
		sb.append(rata);
		
//		FILLER	275	344	70	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 5", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 5", filler, 70);
		sb.append(filler);
		
//		RIFERIMENTO CREDITORE	345	379	35	Carattere		SECIM +	$pendenza.{datiAllegati}.secim.riferimentoCreditore o, in sua assenza, il campo $pendenza.voce[0].idVoce
		// il prefisso SECIM viene valorizzato cosi se il campo $pendenza.{datiAllegati}.secim.tipoRiferimentoCreditore e' vuoto, altrimenti ci viene messo il valore ricevuto
		if(riferimentoCreditore == null) {
			riferimentoCreditore = singoloVersamento.getCodSingoloVersamentoEnte();
		}
		riferimentoCreditore = (tipoRiferimentoCreditore == null) ? ("SECIM" + riferimentoCreditore) : (tipoRiferimentoCreditore + riferimentoCreditore); 
		riferimentoCreditore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RIFERIMENTO CREDITORE", riferimentoCreditore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("RIFERIMENTO CREDITORE", riferimentoCreditore, 35);
		sb.append(riferimentoCreditore);
		
//		FILLER	380	457	78	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 6", "", 78, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 6", filler, 78);
		sb.append(filler);
		
//		IMPORTO VERSAMENTO	458	472	15	Numerico	13	2	SI	singolo_versamento.importo_singolo_versamento o versamento.importo_totale
		String importoTotalePagato = TracciatiNotificaPagamentiUtils.printImporto(versamento.getImportoTotale(), true);
		importoTotalePagato = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO VERSAMENTO", importoTotalePagato, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO VERSAMENTO", importoTotalePagato, 15);
		sb.append(importoTotalePagato);
		
//		FILLER	473	517	45	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 7", "", 45, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 7", filler, 45);
		sb.append(filler);
		
//		CAUSALE VERSAMENTO	518	657	140	Carattere			SI	singolo_versamento.descrizione_causale_RPT o versamento.causale
		String causaleVersamento = ctDatiSingoloPagamentoRT.getCausaleVersamento();
		causaleVersamento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CAUSALE VERSAMENTO", causaleVersamento, 140, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CAUSALE VERSAMENTO", causaleVersamento, 140);
		sb.append(causaleVersamento);
		
//		FILLER	658	713	56	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 8", "", 56, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 8", filler, 56);
		sb.append(filler);
		
//		TIPO DEBITORE	714	716	3	Carattere				versamento.debitore_tipo
		StTipoIdentificativoUnivocoPersFG tipoIdentificativoUnivoco = soggettoPagatore.getIdentificativoUnivocoPagatore().getTipoIdentificativoUnivoco();
		String tipoDebitore = tipoIdentificativoUnivoco.equals(StTipoIdentificativoUnivocoPersFG.F) ? "F" : "G";
		tipoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "TIPO DEBITORE", tipoDebitore, 3, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO DEBITORE", tipoDebitore, 3);
		sb.append(tipoDebitore);
		
//		TIPO CODICE DEBITORE	717	718	2	Carattere			SI	Tipologia del dato versamento.debitore_identificativo
		String tipoCodiceDebitore = tipoIdentificativoUnivoco.equals(StTipoIdentificativoUnivocoPersFG.F) ? "CF" : "PI";
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO CODICE DEBITORE", tipoCodiceDebitore, 2);
		sb.append(tipoCodiceDebitore);
		
//		CODICE DEBITORE	719	753	35	Carattere			SI	versamento.debitore_identificativo
		String codiceDebitore = soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco();
		codiceDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE DEBITORE", codiceDebitore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE DEBITORE", codiceDebitore, 35);
		sb.append(codiceDebitore);
		
//		ANAGRAFICA DEBITORE	754	803	50	Carattere			SI	versamento.debitore_anagrafica
		String anagraficaDebitore = soggettoPagatore.getAnagraficaPagatore();
		anagraficaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "ANAGRAFICA DEBITORE", anagraficaDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("ANAGRAFICA DEBITORE", anagraficaDebitore, 50);
		sb.append(anagraficaDebitore);
		
//		FILLER	804	838	35	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 9", "", 35, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 9", filler, 35);
		sb.append(filler);
		
//		INDIRIZZO DEBITORE	839	888	50	Carattere				versamento.debitore_indirizzo
		String indirizzoDebitore = soggettoPagatore.getIndirizzoPagatore();
		indirizzoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "INDIRIZZO DEBITORE", indirizzoDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("INDIRIZZO DEBITORE", indirizzoDebitore, 50);
		sb.append(indirizzoDebitore);
		
//		NUMERO CIVICO DEBITORE	889	893	5	Carattere				versamento.debitore_civico
		String numeroCivicoDebitore = soggettoPagatore.getCivicoPagatore();
		numeroCivicoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO CIVICO DEBITORE", numeroCivicoDebitore, 5, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO CIVICO DEBITORE", numeroCivicoDebitore, 5);
		sb.append(numeroCivicoDebitore);
		
//		CAP DEBITORE	894	898	5	Carattere				versamento.debitore_cap
		String capDebitore = soggettoPagatore.getCapPagatore();
		capDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CAP DEBITORE", capDebitore, 5, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CAP DEBITORE", capDebitore, 5);
		sb.append(capDebitore);
		
//		LOCALITA DEBITORE	899	948	50	Carattere				versamento.debitore_localita
		String localitaDebitore = soggettoPagatore.getLocalitaPagatore();
		localitaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "LOCALITA DEBITORE", localitaDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("LOCALITA DEBITORE", localitaDebitore, 50);
		sb.append(localitaDebitore);
		
//		PROVINCIA DEBITORE	949	950	2	Carattere				versamento.debitore_provincia
		String provinciaDebitore = soggettoPagatore.getProvinciaPagatore();
		provinciaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "PROVINCIA DEBITORE", provinciaDebitore, 2, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("PROVINCIA DEBITORE", provinciaDebitore, 2);
		sb.append(provinciaDebitore);
		
//		STATO DEBITORE	951	985	35	Carattere				versamento.debitore_nazione
		String nazioneDebitore = soggettoPagatore.getNazionePagatore();
		nazioneDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "STATO DEBITORE", nazioneDebitore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("STATO DEBITORE", nazioneDebitore, 35);
		sb.append(nazioneDebitore);
		
//		FILLER	986	1055	70	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 10", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 10", filler, 70);
		sb.append(filler);
		
//		DATA PAGAMENTO	1056	1063	8	Numerico	8	0		versamento.data_pagamento
		String dataPagamento = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
		TracciatiNotificaPagamentiUtils.validaCampo("DATA PAGAMENTO", dataPagamento, 8);
		sb.append(dataPagamento);
		
//		DATA INCASSO	1064	1071	8	Numerico	8	0		fr.data_ora_flusso se disponibile
		String dataIncasso = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
		dataIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "DATA INCASSO", dataIncasso, 8, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("DATA INCASSO", dataIncasso, 8);
		sb.append(dataIncasso);
		
//		ESERCIZIO DI RIFERIMENTO	1072	1075	4	Numerico	4	0		??? nel file di esempio vale sempre 0000
		String esercizioRiferimento = "";
		esercizioRiferimento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "ESERCIZIO DI RIFERIMENTO", esercizioRiferimento, 4, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("ESERCIZIO DI RIFERIMENTO", esercizioRiferimento, 4);
		sb.append(esercizioRiferimento);
		
//		NUMERO PROVVISORIO	1076	1082	7	Numerico	7	0		??? nel file di esempio vale sempre 0000000
		String numeroProvvisorio = "";
		numeroProvvisorio = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO PROVVISORIO", numeroProvvisorio, 7, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO PROVVISORIO", numeroProvvisorio, 7);
		sb.append(numeroProvvisorio);
		
//		CODICE RETE INCASSO	1083	1085	3	Carattere				NDP nei casi normali, PST se non si ha la RT ma il pagamento e’ stato solamente rendicontato da un flusso con codice esito = 9
		String codiceReteIncasso = "NDP";
		codiceReteIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE RETE INCASSO", codiceReteIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE RETE INCASSO", codiceReteIncasso, 3);
		sb.append(codiceReteIncasso);
		
//		CODICE CANALE INCASSO	1086	1088	3	Carattere				Dal PSP che ha e’ stato utilizzato
		String codiceCanaleIncasso = "";
		codiceCanaleIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE CANALE INCASSO", codiceCanaleIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE CANALE INCASSO", codiceCanaleIncasso, 3);
		sb.append(codiceCanaleIncasso);
		
//		CODICE STRUMENTO INCASSO	1089	1091	3	Carattere				NDP se il campo precedente e’ di tipo PSP, altrimenti bisogna chiedere il codice bollettino
		String codiceStrumentoIncasso = "NDP";
		codiceStrumentoIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3);
		sb.append(codiceStrumentoIncasso);

//		NUMERO BOLLETTA	1092	1104	13	Numerico	13	0		fr.trn se disponibile
		String numeroBolletta = "";
		numeroBolletta = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO BOLLETTA", numeroBolletta, 13, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO BOLLETTA", numeroBolletta, 13);
		sb.append(numeroBolletta);
		
//		IMPORTO PAGATO	1105	1119	15	Numerico	13	2		versamento.importo_pagato o rendicontazione.importo_pagato
		String importoPagato = TracciatiNotificaPagamentiUtils.printImporto(datiPagamento.getImportoTotalePagato(), true);
		importoPagato = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO PAGATO", importoPagato, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO PAGATO", importoPagato, 15);
		sb.append(importoPagato);
		
//		FILLER	1120	1172	53		
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 11", "", 53, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 11", filler, 53);
		sb.append(filler);
		
//		IMPORTO COMMISSIONE PA	1173	1187	15	Numerico	13	2		0
		String importoCommissionePA = "";
		importoCommissionePA = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO COMMISSIONE PA", importoCommissionePA, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO COMMISSIONE PA", importoCommissionePA, 15);
		sb.append(importoCommissionePA);
		
//		IMPORTO COMMISSIONE DEBITORE	1188	1202	15	Numerico	13	2		Da RT? rt.datiPagamento.datiSingoloPagamento[i].commissioniApplicatePSP
		BigDecimal commissioniApplicatePSP = ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP() != null ? ctDatiSingoloPagamentoRT.getCommissioniApplicatePSP() : BigDecimal.ZERO;
		String importoCommissioniDebitore = TracciatiNotificaPagamentiUtils.printImporto(commissioniApplicatePSP, true);
		importoCommissioniDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO COMMISSIONE DEBITORE", importoCommissioniDebitore, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO COMMISSIONE DEBITORE", importoCommissioniDebitore, 15);
		sb.append(importoCommissioniDebitore);
		
//		FILLER	1203	1589	387	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 12", "", 387, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 12", filler, 387);
		sb.append(filler);
		
//		CCP	1590	1601	12	Numerico				numero conto corrente postale?
		String ccp = "";
		ccp = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CCP", ccp, 12, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CCP", ccp, 12);
		sb.append(ccp);
		
//		FILLER	1602	2000	399	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 13", "", 399, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 13", filler, 399);
		sb.append(filler);
		
		if(tipoRiferimentoCreditore == null) {
			secimOS.write(sb.toString().getBytes());
		} else {
			noSecimOS.write(sb.toString().getBytes());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void creaLineaCsvSecimRpt_SANP24(Logger log, Rpt rpt, BDConfigWrapper configWrapper, int numeroLinea, ConnettoreNotificaPagamenti connettore, OutputStream secimOS, OutputStream noSecimOS) throws ServiceException, JAXBException, SAXException, ValidationException, java.io.IOException { 
		StringBuilder sb = new StringBuilder();
		
		
		// NUOVA LINEA 
		if(numeroLinea > 1)
			sb.append("\n");

		String entryKey = "RPT_" + rpt.getCodDominio() + "_" + rpt.getIuv() + "_" + rpt.getCcp();
		Versamento versamento = rpt.getVersamento();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		SingoloVersamento singoloVersamento = singoliVersamenti.get(0);
		PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
		CtReceipt ctReceipt = paSendRTReq_RT.getReceipt();
		CtSubject soggettoPagatore = ctReceipt.getDebtor();
		CtTransferPA ctTransferPA = ctReceipt.getTransferList().getTransfer().get(0);
		
		String contabilitaString = singoloVersamento.getContabilita();
		String riferimentoCreditore = null;
		String tipoflusso = null;
		String tipoRiferimentoCreditore = null;
		if(contabilitaString != null && contabilitaString.length() > 0) {
			Contabilita contabilita = JSONSerializable.parse(contabilitaString, Contabilita.class);
			
			Object proprietaCustomObj = contabilita.getProprietaCustom();
			
			if(proprietaCustomObj != null) {
				if(proprietaCustomObj instanceof String) {
					String proprietaCustom = (String) proprietaCustomObj;
					if(proprietaCustom != null && proprietaCustom.length() > 0) {
						Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
						// leggo proprieta
						if(parse.containsKey("riferimentoCreditore")) {
							riferimentoCreditore = (String) parse.get("riferimentoCreditore");
						}
						if(parse.containsKey("tipoflusso")) {
							tipoflusso = (String) parse.get("tipoflusso");
						}
						if(parse.containsKey("tipoRiferimentoCreditore")) {
							tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
						}
					}
				}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
					
					// leggo proprieta
					if(parse.containsKey("riferimentoCreditore")) {
						riferimentoCreditore = (String) parse.get("riferimentoCreditore");
					}
					if(parse.containsKey("tipoflusso")) {
						tipoflusso = (String) parse.get("tipoflusso");
					}
					if(parse.containsKey("tipoRiferimentoCreditore")) {
						tipoRiferimentoCreditore = (String) parse.get("tipoRiferimentoCreditore");
					}
				}
			}
		}
		
//		CODICE ISTITUTO	1	5	5	Numerico	5	0	SI	Codice in rt.istitutoAttestante.identificativoUnivocoAttestante.codiceIdentificativoUnivoco se rt.istitutoAttestante.identificativoUnivocoAttestante.tipoIdentificativoUnivoco == ‘A’
		String codiceIstituto = ctReceipt.getIdPSP();
//		StTipoIdentificativoUnivoco tipoIdentificativoUnivocoATtestante = istitutoAttestante.getIdentificativoUnivocoAttestante().getTipoIdentificativoUnivoco();
		if(connettore.getCodiceIstituto() != null) {
			codiceIstituto = connettore.getCodiceIstituto();
		} else {
//			switch (tipoIdentificativoUnivocoATtestante) {
//			case A:
//				codiceIstituto = istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco();
//				break;
//			case B:
//			case G:
				codiceIstituto = "00000";
//				break;
//			}
		}
		
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE ISTITUTO", codiceIstituto, 5);
		sb.append(codiceIstituto);
		
//		CODICE CLIENTE	6	12	7	Numerico	7	0	SI	Codice Ente dal portale Ente Creditore
		String codiceCliente = connettore.getCodiceCliente(); 
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE CLIENTE", codiceCliente, 7);
		sb.append(codiceCliente);
		
//		FILLER	13	22	10	
		String filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 1", "", 10, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 1", filler, 10);
		sb.append(filler);
		
//		TIPO FLUSSO	23	30	8	Carattere			SI	Dato di configurazione assegnato da Poste alla PA datiallegati.tipoflusso
		if(tipoflusso == null) {
			tipoflusso = "NDP001C0";
		}
		tipoflusso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "TIPO FLUSSO", tipoflusso, 8, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO FLUSSO", tipoflusso, 8);
		sb.append(tipoflusso);
		
//		DATA CREAZIONE FLUSSO	31	38	8	Numerico	8	0	SI	Data Creazione di questo flusso?
		Date dataCreazioneFlusso = new Date();
		String dataCreazione = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(dataCreazioneFlusso);
		TracciatiNotificaPagamentiUtils.validaCampo("DATA CREAZIONE FLUSSO", dataCreazione, 8);
		sb.append(dataCreazione);
		
//		FILLER	39	77	39	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 2", "", 39, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 2", filler, 39);
		sb.append(filler);
		
//		PROGRESSIVO RECORD	78	90	13	Numerico	13	0		Numero di record incrementale
		String progressivoRecord = "" + numeroLinea;
		progressivoRecord = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "PROGRESSIVO RECORD", progressivoRecord, 13, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("PROGRESSIVO RECORD", progressivoRecord, 13);
		sb.append(progressivoRecord);
		
//		OPERAZIONE	91	93	3	Carattere			SI	nel pdf si parla di RIV nell’esempio c’e’ RIS
		String operazione = "RIS";
		TracciatiNotificaPagamentiUtils.validaCampo("OPERAZIONE", operazione, 3);
		sb.append(operazione);
		
//		FILLER	94	163	70	Carattere	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 3", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 3", filler, 70);
		sb.append(filler);
		
//		TIPO PRESENTAZIONE	164	169	6	Carattere	
		String tipoPresentazione = "BOL_PA";
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO PRESENTAZIONE", tipoPresentazione, 6);
		sb.append(tipoPresentazione);
		
//		CODICE PRESENTAZIONE	170	187	18	Carattere				versamento.numero_avviso
		String codicePresentazione = versamento.getNumeroAvviso() != null ? versamento.getNumeroAvviso() : "";
		codicePresentazione = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE PRESENTAZIONE", codicePresentazione, 18, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE PRESENTAZIONE", codicePresentazione, 18);
		sb.append(codicePresentazione);
		
//		FILLER	188	204	17	Carattere	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 4", "", 17, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 4", filler, 17);
		sb.append(filler);
		
//		IUV	205	239	35	Carattere				versamento.iuv
		String iuvVersamento = ctReceipt.getCreditorReferenceId();
		iuvVersamento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IUV", iuvVersamento, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("IUV", iuvVersamento, 35);
		sb.append(iuvVersamento);
		
//		RATA	240	274	35	Carattere				versamento.cod_rata
		String prefixRata = versamento.getNumeroRata() != null ? "S" : "T";
		Integer numeroRata = versamento.getNumeroRata() != null ? versamento.getNumeroRata() : 1;
		String rata = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RATA", numeroRata+"", 8, true, true); // Aggiungo zeri a sx fino ad arrivare a 8 caratteri
		rata = prefixRata + rata; // aggiungo prefisso
		rata = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RATA", rata, 35, false, false); // completo con spazi bianchi a dx fino a 35 caratteri
		TracciatiNotificaPagamentiUtils.validaCampo("RATA", rata, 35);
		sb.append(rata);
		
//		FILLER	275	344	70	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 5", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 5", filler, 70);
		sb.append(filler);
		
//		RIFERIMENTO CREDITORE	345	379	35	Carattere		SECIM +	$pendenza.{datiAllegati}.secim.riferimentoCreditore o, in sua assenza, il campo $pendenza.voce[0].idVoce
		// il prefisso SECIM viene valorizzato cosi se il campo $pendenza.{datiAllegati}.secim.tipoRiferimentoCreditore e' vuoto, altrimenti ci viene messo il valore ricevuto
		if(riferimentoCreditore == null) {
			riferimentoCreditore = singoloVersamento.getCodSingoloVersamentoEnte();
		}
		riferimentoCreditore = (tipoRiferimentoCreditore == null) ? ("SECIM" + riferimentoCreditore) : (tipoRiferimentoCreditore + riferimentoCreditore); 
		riferimentoCreditore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "RIFERIMENTO CREDITORE", riferimentoCreditore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("RIFERIMENTO CREDITORE", riferimentoCreditore, 35);
		sb.append(riferimentoCreditore);
		
//		FILLER	380	457	78	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 6", "", 78, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 6", filler, 78);
		sb.append(filler);
		
//		IMPORTO VERSAMENTO	458	472	15	Numerico	13	2	SI	singolo_versamento.importo_singolo_versamento o versamento.importo_totale
		String importoTotalePagato = TracciatiNotificaPagamentiUtils.printImporto(versamento.getImportoTotale(), true);
		importoTotalePagato = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO VERSAMENTO", importoTotalePagato, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO VERSAMENTO", importoTotalePagato, 15);
		sb.append(importoTotalePagato);
		
//		FILLER	473	517	45	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 7", "", 45, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 7", filler, 45);
		sb.append(filler);
		
//		CAUSALE VERSAMENTO	518	657	140	Carattere			SI	singolo_versamento.descrizione_causale_RPT o versamento.causale
		String causaleVersamento = ctTransferPA.getRemittanceInformation();
		causaleVersamento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CAUSALE VERSAMENTO", causaleVersamento, 140, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CAUSALE VERSAMENTO", causaleVersamento, 140);
		sb.append(causaleVersamento);
		
//		FILLER	658	713	56	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 8", "", 56, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 8", filler, 56);
		sb.append(filler);
		
//		TIPO DEBITORE	714	716	3	Carattere				versamento.debitore_tipo
		StEntityUniqueIdentifierType tipoIdentificativoUnivoco = soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierType();
		String tipoDebitore = tipoIdentificativoUnivoco.equals(StEntityUniqueIdentifierType.F) ? "F" : "G";
		tipoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "", tipoDebitore, 3, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO DEBITORE", tipoDebitore, 3);
		sb.append(tipoDebitore);
		
//		TIPO CODICE DEBITORE	717	718	2	Carattere			SI	Tipologia del dato versamento.debitore_identificativo
		String tipoCodiceDebitore = tipoIdentificativoUnivoco.equals(StEntityUniqueIdentifierType.F) ? "CF" : "PI";
		TracciatiNotificaPagamentiUtils.validaCampo("TIPO CODICE DEBITORE", tipoCodiceDebitore, 2);
		sb.append(tipoCodiceDebitore);
		
//		CODICE DEBITORE	719	753	35	Carattere			SI	versamento.debitore_identificativo
		String codiceDebitore = soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue();
		codiceDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE DEBITORE", codiceDebitore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE DEBITORE", codiceDebitore, 35);
		sb.append(codiceDebitore);
		
//		ANAGRAFICA DEBITORE	754	803	50	Carattere			SI	versamento.debitore_anagrafica
		String anagraficaDebitore = soggettoPagatore.getFullName();
		anagraficaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "ANAGRAFICA DEBITORE", anagraficaDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("ANAGRAFICA DEBITORE", anagraficaDebitore, 50);
		sb.append(anagraficaDebitore);
		
//		FILLER	804	838	35	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 9", "", 35, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 9", filler, 35);
		sb.append(filler);
		
//		INDIRIZZO DEBITORE	839	888	50	Carattere				versamento.debitore_indirizzo
		String indirizzoDebitore = soggettoPagatore.getStreetName();
		indirizzoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "INDIRIZZO DEBITORE", indirizzoDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("INDIRIZZO DEBITORE", indirizzoDebitore, 50);
		sb.append(indirizzoDebitore);
		
//		NUMERO CIVICO DEBITORE	889	893	5	Carattere				versamento.debitore_civico
		String numeroCivicoDebitore = soggettoPagatore.getCivicNumber();
		numeroCivicoDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO CIVICO DEBITORE", numeroCivicoDebitore, 5, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO CIVICO DEBITORE", numeroCivicoDebitore, 5);
		sb.append(numeroCivicoDebitore);
		
//		CAP DEBITORE	894	898	5	Carattere				versamento.debitore_cap
		String capDebitore = soggettoPagatore.getPostalCode();
		capDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CAP DEBITORE", capDebitore, 5, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("CAP DEBITORE", capDebitore, 5);
		sb.append(capDebitore);
		
//		LOCALITA DEBITORE	899	948	50	Carattere				versamento.debitore_localita
		String localitaDebitore = soggettoPagatore.getCity();
		localitaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "LOCALITA DEBITORE", localitaDebitore, 50, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("LOCALITA DEBITORE", localitaDebitore, 50);
		sb.append(localitaDebitore);
		
//		PROVINCIA DEBITORE	949	950	2	Carattere				versamento.debitore_provincia
		String provinciaDebitore = soggettoPagatore.getStateProvinceRegion();
		provinciaDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "PROVINCIA DEBITORE", provinciaDebitore, 2, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("PROVINCIA DEBITORE", provinciaDebitore, 2);
		sb.append(provinciaDebitore);
		
//		STATO DEBITORE	951	985	35	Carattere				versamento.debitore_nazione
		String nazioneDebitore = soggettoPagatore.getCountry();
		nazioneDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "STATO DEBITORE", nazioneDebitore, 35, false, false);
		TracciatiNotificaPagamentiUtils.validaCampo("STATO DEBITORE", nazioneDebitore, 35);
		sb.append(nazioneDebitore);
		
//		FILLER	986	1055	70	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 10", "", 70, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 10", filler, 70);
		sb.append(filler);
		
//		DATA PAGAMENTO	1056	1063	8	Numerico	8	0		versamento.data_pagamento
		String dataPagamento = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctReceipt.getPaymentDateTime());
		TracciatiNotificaPagamentiUtils.validaCampo("DATA PAGAMENTO", dataPagamento, 8);
		sb.append(dataPagamento);
		
//		DATA INCASSO	1064	1071	8	Numerico	8	0		fr.data_ora_flusso se disponibile
		String dataIncasso = SimpleDateFormatUtils.newSimpleDateFormatSoloDataSenzaSpazi().format(ctReceipt.getPaymentDateTime());
		dataIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "DATA INCASSO", dataIncasso, 8, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("DATA INCASSO", dataIncasso, 8);
		sb.append(dataIncasso);
		
//		ESERCIZIO DI RIFERIMENTO	1072	1075	4	Numerico	4	0		??? nel file di esempio vale sempre 0000
		String esercizioRiferimento = "";
		esercizioRiferimento = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "ESERCIZIO DI RIFERIMENTO", esercizioRiferimento, 4, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("ESERCIZIO DI RIFERIMENTO", esercizioRiferimento, 4);
		sb.append(esercizioRiferimento);
		
//		NUMERO PROVVISORIO	1076	1082	7	Numerico	7	0		??? nel file di esempio vale sempre 0000000
		String numeroProvvisorio = "";
		numeroProvvisorio = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO PROVVISORIO", numeroProvvisorio, 7, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO PROVVISORIO", numeroProvvisorio, 7);
		sb.append(numeroProvvisorio);
		
//		CODICE RETE INCASSO	1083	1085	3	Carattere				NDP nei casi normali, PST se non si ha la RT ma il pagamento e’ stato solamente rendicontato da un flusso con codice esito = 9
		String codiceReteIncasso = "NDP";
		codiceReteIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE RETE INCASSO", codiceReteIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE RETE INCASSO", codiceReteIncasso, 3);
		sb.append(codiceReteIncasso);
		
//		CODICE CANALE INCASSO	1086	1088	3	Carattere				Dal PSP che ha e’ stato utilizzato
		String codiceCanaleIncasso = "";
		codiceCanaleIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE CANALE INCASSO", codiceCanaleIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE CANALE INCASSO", codiceCanaleIncasso, 3);
		sb.append(codiceCanaleIncasso);
		
//		CODICE STRUMENTO INCASSO	1089	1091	3	Carattere				NDP se il campo precedente e’ di tipo PSP, altrimenti bisogna chiedere il codice bollettino
		String codiceStrumentoIncasso = "NDP";
		codiceStrumentoIncasso = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CODICE STRUMENTO INCASSO", codiceStrumentoIncasso, 3);
		sb.append(codiceStrumentoIncasso);

//		NUMERO BOLLETTA	1092	1104	13	Numerico	13	0		fr.trn se disponibile
		String numeroBolletta = "";
		numeroBolletta = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "NUMERO BOLLETTA", numeroBolletta, 13, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("NUMERO BOLLETTA", numeroBolletta, 13);
		sb.append(numeroBolletta);
		
//		IMPORTO PAGATO	1105	1119	15	Numerico	13	2		versamento.importo_pagato o rendicontazione.importo_pagato
		String importoPagato = TracciatiNotificaPagamentiUtils.printImporto(ctReceipt.getPaymentAmount(), true);
		importoPagato = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO PAGATO", importoPagato, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO PAGATO", importoPagato, 15);
		sb.append(importoPagato);
		
//		FILLER	1120	1172	53		
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 11", "", 53, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 11", filler, 53);
		sb.append(filler);
		
//		IMPORTO COMMISSIONE PA	1173	1187	15	Numerico	13	2		0
		String importoCommissionePA = "";
		importoCommissionePA = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO COMMISSIONE PA", importoCommissionePA, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO COMMISSIONE PA", importoCommissionePA, 15);
		sb.append(importoCommissionePA);
		
//		IMPORTO COMMISSIONE DEBITORE	1188	1202	15	Numerico	13	2		Da RT? rt.datiPagamento.datiSingoloPagamento[i].commissioniApplicatePSP
		BigDecimal commissioniApplicatePSP = ctReceipt.getFee() != null ? ctReceipt.getFee() : BigDecimal.ZERO;
		String importoCommissioniDebitore = TracciatiNotificaPagamentiUtils.printImporto(commissioniApplicatePSP, true);
		importoCommissioniDebitore = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "IMPORTO COMMISSIONE DEBITORE", importoCommissioniDebitore, 15, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("IMPORTO COMMISSIONE DEBITORE", importoCommissioniDebitore, 15);
		sb.append(importoCommissioniDebitore);
		
//		FILLER	1203	1589	387	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 12", "", 387, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 12", filler, 387);
		sb.append(filler);
		
//		CCP	1590	1601	12	Numerico				numero conto corrente postale?
		String ccp = "";
		ccp = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "CCP", ccp, 12, true, true);
		TracciatiNotificaPagamentiUtils.validaCampo("CCP", ccp, 12);
		sb.append(ccp);
		
//		FILLER	1602	2000	399	
		filler = TracciatiNotificaPagamentiUtils.completaValoreCampoConFiller(log, entryKey, "FILLER 13", "", 399, false, true);
		TracciatiNotificaPagamentiUtils.validaCampo("FILLER 13", filler, 399);
		sb.append(filler);
		
		if(tipoRiferimentoCreditore == null) {
			secimOS.write(sb.toString().getBytes());
		} else {
			noSecimOS.write(sb.toString().getBytes());
		}
	}
	
	public static List<List<String>> creaLineaCsvGovPayRpt_SANP23(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<List<String>> linee = new ArrayList<List<String>>();
		

		Versamento versamento = rpt.getVersamento();
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
		CtRicevutaTelematica rt = JaxbUtils.toRT(rpt.getXmlRt(), false);
		CtDatiVersamentoRT datiPagamento = rt.getDatiPagamento();
		CtSoggettoPagatore soggettoPagatore = rt.getSoggettoPagatore();
		CtIstitutoAttestante istitutoAttestante = rt.getIstitutoAttestante();
		String datiAllegati = versamento.getDatiAllegati();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
		Documento documento = versamento.getDocumento(configWrapper);
		String causaleVersamento = null;
		try {
			causaleVersamento = versamento.getCausaleVersamento().getSimple();
		} catch (UnsupportedEncodingException e) {
			causaleVersamento = "";
		}
		
		for(int indiceDati = 0; indiceDati < datiPagamento.getDatiSingoloPagamento().size(); indiceDati ++) {
			CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = datiPagamento.getDatiSingoloPagamento().get(indiceDati);
			
			SingoloVersamento singoloVersamento = singoliVersamenti.get(indiceDati);
			String datiAllegatiSV = singoloVersamento.getDatiAllegati();
			
			List<String> linea = new ArrayList<String>();
			
//			idA2A: da pendenza
			linea.add(applicazione.getCodApplicazione());
//			idPendenza: da pendenza
			linea.add(versamento.getCodVersamentoEnte());
//			idDocumento: da pendenza
			String codDocumento = documento != null ? documento.getCodDocumento() : "";
			linea.add(codDocumento);
			// descrizioneDocumento: da pendenza
			String descrizioneDocumento = documento != null ? documento.getDescrizione() : "";
			linea.add(descrizioneDocumento);
//			codiceRata: da pendenza
			linea.add(versamento.getNumeroRata() != null ? versamento.getNumeroRata() + "" : "");
//			dataScadenza: da pendenza
			String dataScadenzaS = versamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamento.getDataScadenza()): "";
			linea.add(dataScadenzaS);
//			idVocePendenza: da pendenza
			linea.add(singoloVersamento.getCodSingoloVersamentoEnte());
			// descrizioneVocePendenza
			linea.add(singoloVersamento.getDescrizione());
//			idTipoPendenza: da pendenza
			linea.add(tipoVersamento.getCodTipoVersamento());
			// descrizione  pendenza.causale
			linea.add(causaleVersamento);
//			anno: da pendenza
			linea.add(versamento.getCodAnnoTributario() != null ? versamento.getCodAnnoTributario() + "" : "");
//			identificativoDebitore: da RT rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.codiceIdentificativoUnivoco
			linea.add(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
//			anagraficaDebitore: da RT rt.datiPagamento.soggettoPagatore.anagraficaPagatore
			linea.add(soggettoPagatore.getAnagraficaPagatore());
//			identificativoDominio: da RT
			linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
//			identificativoUnivocoVersamento: da RT rt.datiPagamento.identificativoUnivocoVersamento
			linea.add(datiPagamento.getIdentificativoUnivocoVersamento());
//			codiceContestoPagamento:da RT
			linea.add(datiPagamento.getCodiceContestoPagamento());
//			indiceDati: da RT
			linea.add((indiceDati + 1) + "");
//			identificativoUnivocoRiscossione: da RT
			linea.add(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
			// modelloPagamento da RT
			linea.add(rpt.getModelloPagamento().getCodifica()+"");
//			singoloImportoPagato: da RT
			linea.add(TracciatiNotificaPagamentiUtils.printImporto(ctDatiSingoloPagamentoRT.getSingoloImportoPagato(), false));
//			dataEsitoSingoloPagamento: da RTrt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
			linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento()));
//			causaleVersamento: da RT rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
			linea.add(ctDatiSingoloPagamentoRT.getCausaleVersamento()); 
//			datiSpecificiRiscossione: da RT rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
			linea.add(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
//			datiAllegati: da Pendenza
			linea.add(datiAllegati != null ? datiAllegati : "");
//			datiAllegatiVoce: da vocePendenza
			linea.add(datiAllegatiSV != null ? datiAllegatiSV : "");
//			denominazioneAttestante: da RT
			linea.add(istitutoAttestante.getDenominazioneAttestante());
//			identificativoAttestante: da RT
			linea.add(istitutoAttestante.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			// contabilita
			if(singoloVersamento.getContabilita() != null && singoloVersamento.getContabilita().length() > 0) {
				linea.add(singoloVersamento.getContabilita());
			} else {
				linea.add("");
			}
			
			linee.add(linea);
		}
		

		return linee;
	}
	
	public static List<List<String>> creaLineaCsvGovPayRpt_SANP24(Rpt rpt, BDConfigWrapper configWrapper) throws ServiceException, JAXBException, SAXException, ValidationException { 
		List<List<String>> linee = new ArrayList<List<String>>();
		

		Versamento versamento = rpt.getVersamento();
		Applicazione applicazione = versamento.getApplicazione(configWrapper);
		TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
		PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
		CtReceipt ctReceipt = paSendRTReq_RT.getReceipt();
		CtSubject soggettoPagatore = ctReceipt.getDebtor();
		String datiAllegati = versamento.getDatiAllegati();
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti();
		Documento documento = versamento.getDocumento(configWrapper);
		String causaleVersamento = null;
		try {
			causaleVersamento = versamento.getCausaleVersamento().getSimple();
		} catch (UnsupportedEncodingException e) {
			causaleVersamento = "";
		}
		
		for(int indiceDati = 0; indiceDati < ctReceipt.getTransferList().getTransfer().size(); indiceDati ++) {
			CtTransferPA ctTransferPA = ctReceipt.getTransferList().getTransfer().get(indiceDati);
			
			SingoloVersamento singoloVersamento = singoliVersamenti.get(indiceDati);
			String datiAllegatiSV = singoloVersamento.getDatiAllegati();
			
			List<String> linea = new ArrayList<String>();
			
//			idA2A: da pendenza
			linea.add(applicazione.getCodApplicazione());
//			idPendenza: da pendenza
			linea.add(versamento.getCodVersamentoEnte());
//			idDocumento: da pendenza
			String codDocumento = documento != null ? documento.getCodDocumento() : "";
			linea.add(codDocumento);
			// descrizioneDocumento: da pendenza
			String descrizioneDocumento = documento != null ? documento.getDescrizione() : "";
			linea.add(descrizioneDocumento);
//			codiceRata: da pendenza
			linea.add(versamento.getNumeroRata() != null ? versamento.getNumeroRata() + "" : "");
//			dataScadenza: da pendenza
			String dataScadenzaS = versamento.getDataScadenza() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamento.getDataScadenza()): "";
			linea.add(dataScadenzaS);
//			idVocePendenza: da pendenza
			linea.add(singoloVersamento.getCodSingoloVersamentoEnte());
			// descrizioneVocePendenza
			linea.add(singoloVersamento.getDescrizione());
//			idTipoPendenza: da pendenza
			linea.add(tipoVersamento.getCodTipoVersamento());
			// descrizione  pendenza.causale
			linea.add(causaleVersamento);
//			anno: da pendenza
			linea.add(versamento.getCodAnnoTributario() != null ? versamento.getCodAnnoTributario() + "" : "");
//			identificativoDebitore: da RT rt.datiPagamento.soggettoPagatore.identificativoUnivocoPagatore.codiceIdentificativoUnivoco
			linea.add(soggettoPagatore.getUniqueIdentifier().getEntityUniqueIdentifierValue());
//			anagraficaDebitore: da RT rt.datiPagamento.soggettoPagatore.anagraficaPagatore
			linea.add(soggettoPagatore.getFullName());
//			identificativoDominio: da RT
			linea.add(ctTransferPA.getFiscalCodePA());
//			identificativoUnivocoVersamento: da RT rt.datiPagamento.identificativoUnivocoVersamento
			linea.add(ctReceipt.getCreditorReferenceId());
//			codiceContestoPagamento:da RT
			linea.add(rpt.getCcp());
//			indiceDati: da RT
			linea.add((indiceDati + 1) + "");
//			identificativoUnivocoRiscossione: da RT
			linea.add(ctReceipt.getReceiptId());
			// modelloPagamento da RT
			linea.add(rpt.getModelloPagamento().getCodifica()+"");
//			singoloImportoPagato: da RT
			linea.add(TracciatiNotificaPagamentiUtils.printImporto(ctTransferPA.getTransferAmount(), false));
//			dataEsitoSingoloPagamento: da RTrt.datiPagamento.datiSingoloPagamento[0].dataEsitoSingoloPagamento [YYYY]-[MM]-[DD]
			linea.add(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(ctReceipt.getPaymentDateTime()));
//			causaleVersamento: da RT rt.datiPagamento.datiSingoloPagamento[0].causaleVersamento
			linea.add(ctTransferPA.getRemittanceInformation()); 
//			datiSpecificiRiscossione: da RT rt.datiPagamento.datiSingoloPagamento[0].datiSpecificiRiscossione
			linea.add(ctTransferPA.getTransferCategory());
//			datiAllegati: da Pendenza
			linea.add(datiAllegati != null ? datiAllegati : "");
//			datiAllegatiVoce: da vocePendenza
			linea.add(datiAllegatiSV != null ? datiAllegatiSV : "");
//			denominazioneAttestante: da RT
			linea.add(ctReceipt.getPSPCompanyName());
//			identificativoAttestante: da RT
			linea.add(ctReceipt.getIdPSP());
			// contabilita
			if(singoloVersamento.getContabilita() != null && singoloVersamento.getContabilita().length() > 0) {
				linea.add(singoloVersamento.getContabilita());
			} else {
				linea.add("");
			}
			
			linee.add(linea);
		}
		

		return linee;
	}
}
