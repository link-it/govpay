package it.govpay.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.checkout.CartRequest;
import it.govpay.core.beans.checkout.PaymentNotice;
import it.govpay.core.beans.checkout.ReturnUrls;
import it.govpay.core.utils.tracciati.TracciatiNotificaPagamentiUtils;

public class CheckoutUtils {
	
	public static CartRequest createCartRequest(Logger log, BDConfigWrapper configWrapper,  String returnUrl, String lang, List<Versamento> versamenti, String codiceConvenzione, String email) throws ServiceException, UnsupportedEncodingException{
		CartRequest cartRequest = new CartRequest();
		
		log.debug("=== Richiesta Modello 1 SANP 3.2.1 ===");
		
		log.debug("EmailNotice: ["+email+"]");
		cartRequest.setEmailNotice(email);
		List<PaymentNotice> paymentNotices = new ArrayList<>();
		
		log.debug("\t=== Elenco Avvisi ===");
		
		for (int i = 0; i < versamenti.size(); i++) {
			Versamento vTmp = versamenti.get(i);
			
			String idDominio = vTmp.getDominio(configWrapper).getCodDominio();
			String ragioneSociale = vTmp.getDominio(configWrapper).getRagioneSociale();
			String numeroAvviso = vTmp.getNumeroAvviso();
			BigDecimal importoTotale = vTmp.getImportoTotale();
			String causale = vTmp.getCausaleVersamento().getSimple();
			causale = causale.length() > 140 ? (causale.substring(0, 137) + "...") : causale;
			
			PaymentNotice paymentNotice = new PaymentNotice();
			
			//fiscalcode
			log.debug("FiscalCode: ["+idDominio+"]");
			paymentNotice.setFiscalCode(idDominio);
			
			//noticenumber
			log.debug("NoticeNumber: ["+numeroAvviso+"]");
			paymentNotice.setNoticeNumber(numeroAvviso);
			
			// importo
			log.debug("Amount: ["+importoTotale+"]");
			String currencyAsString = TracciatiNotificaPagamentiUtils.printImporto(importoTotale, true);
			Integer importo = Integer.parseInt(currencyAsString);
			paymentNotice.setAmount(importo);			
			
			// ragione sociale
			log.debug("CompanyName: ["+ragioneSociale+"]");
			paymentNotice.setCompanyName(ragioneSociale);
			
			// causale
			log.debug("Description: ["+causale+"]");
			paymentNotice.setDescription(causale);
			
			paymentNotices.add(paymentNotice);
		}
		
		cartRequest.setPaymentNotices(paymentNotices );
		
		ReturnUrls returnUrls = new ReturnUrls();
		
		// aggiungo i parametri con l'esito
		boolean hasParameter = returnUrl.contains("?");
		String returnOkUrl = hasParameter ? returnUrl.concat("&esito=OK") : returnUrl.concat("?esito=OK");
		log.debug("ReturnUrlOk: ["+returnOkUrl+"]");
		returnUrls.setReturnOkUrl(returnOkUrl);
		String returnErrorUrl = hasParameter ? returnUrl.concat("&esito=ERROR") : returnUrl.concat("?esito=ERROR");
		log.debug("ReturnUrlError: ["+returnErrorUrl+"]");
		returnUrls.setReturnErrorUrl(returnErrorUrl);
		String returnCancelUrl = hasParameter ? returnUrl.concat("&esito=CANCEL") : returnUrl.concat("?esito=CANCEL");
		log.debug("ReturnUrlCancel: ["+returnCancelUrl+"]");
		returnUrls.setReturnCancelUrl(returnCancelUrl);
		
		cartRequest.setReturnUrls(returnUrls);
		
		log.debug("=== Fine Richiesta Modello 1 SANP 3.2.1 ===");
		
		return cartRequest;
	}

}