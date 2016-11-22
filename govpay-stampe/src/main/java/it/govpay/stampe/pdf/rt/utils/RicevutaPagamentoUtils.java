package it.govpay.stampe.pdf.rt.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.stampe.pdf.rt.IRicevutaPagamento;
import it.govpay.stampe.pdf.rt.factory.RicevutaPagamentoFactory;

public class RicevutaPagamentoUtils {

	public static List<String> getPdfRicevutaPagamento(String pathLoghi, CtRicevutaTelematica rt,String  causale, OutputStream osAPPdf, Logger log) throws Exception {
		List<String> msgs = new ArrayList<String>();
		try{
			// 1. Prelevo le properties
			String codDominio = rt.getEnteBeneficiario().getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco();
			String codTributo = null; //rt.getDatiPagamento().getDatiSingoloPagamento().get(0).getCodiceTributo();
			RicevutaPagamentoProperties ricevutaPagamentoProperties = RicevutaPagamentoProperties.getInstance();
			Properties propertiesAvvisoPagamentoDominioTributo = getRicevutaPagamentoPropertiesPerDominioTributo(ricevutaPagamentoProperties, codDominio, codTributo, log);

			IRicevutaPagamento ricevutaPagamentoBuilder = RicevutaPagamentoFactory.getRicevutaPagamentoBuilder(propertiesAvvisoPagamentoDominioTributo.getProperty(RicevutaPagamentoProperties.RICEVUTA_PAGAMENTO_CLASSNAME_PROP_KEY), log);

			msgs.add(ricevutaPagamentoBuilder.getPdfRicevutaPagamento(pathLoghi, rt, causale, propertiesAvvisoPagamentoDominioTributo, osAPPdf, log));

			return msgs;
		}catch(Exception e){
			log.error(e,e);
			throw e;
		}
	}

	public static Properties getRicevutaPagamentoPropertiesPerDominioTributo(RicevutaPagamentoProperties ricevutaPagamentoProperties,String codDominio,String codTributo,Logger log) throws Exception {
		Properties p = null;
		String key = null;
		// 1. ricerca delle properties per la chiave "codDominio.codTributo";
		if(StringUtils.isNotBlank(codDominio) && StringUtils.isNotBlank(codTributo)){
			key = codDominio + "." + codTributo;
			try{
				log.debug("Ricerca delle properties per la chiave ["+key+"]");
				p = ricevutaPagamentoProperties.getProperties(key);
			}catch(Exception e){
				log.debug("Non sono state trovate properties per la chiave ["+key+"]: " + e.getMessage()); 
			}
		}
		// 2 . ricerca per codDominio
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
