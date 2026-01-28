/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.exceptions.ValidationException;

public class IuvUtils {
	
	private IuvUtils() {}

	private static byte[] buildQrCode002(String codDominio, int auxDigit, int applicationCode, String iuv, BigDecimal importoTotale, String numeroAvviso) {
		// Da "L’Avviso di pagamento analogico nel sistema pagoPA" par. 2.1
		String qrCode = null; 
		if(numeroAvviso == null) {
			if(auxDigit == 0)
				qrCode = "PAGOPA|002|0" + String.format("%02d", applicationCode) + iuv + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));
			else 
				qrCode = "PAGOPA|002|" + auxDigit + iuv + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));
		} else {
				qrCode = "PAGOPA|002|" + numeroAvviso + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));
		}

		return qrCode.getBytes();
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("00.00", new DecimalFormatSymbols(Locale.ENGLISH));

	private static String buildBarCode(String gln, int auxDigit, int applicationCode, String iuv, BigDecimal importoTotale, String numeroAvviso) {
		// Da Guida Tecnica di Adesione PA 3.8 pag 25 
		String payToLoc = "415";
		String refNo = "8020";
		String amount = "3902";
		String importo = nFormatter.format(importoTotale).replace(".", "");

		if(numeroAvviso == null) {
		if(auxDigit == 3)
			return payToLoc + gln + refNo + "3" + iuv + amount + importo;
		else 
			return payToLoc + gln + refNo + "0" + String.format("%02d", applicationCode) + iuv + amount + importo;
		} else {
			return payToLoc + gln + refNo + numeroAvviso + amount + importo;
		}
	}

	public static Iuv toIuv(Applicazione applicazione, Dominio dominio, it.govpay.model.Iuv iuv, BigDecimal importoTotale) {
		Iuv iuvGenerato = new Iuv();
		iuvGenerato.setCodApplicazione(applicazione.getCodApplicazione());
		iuvGenerato.setCodDominio(dominio.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuv.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuv.getIuv());
		if(iuv.getAuxDigit() == 0)
			iuvGenerato.setNumeroAvviso(iuv.getAuxDigit() + String.format("%02d", iuv.getApplicationCode()) + iuv.getIuv());
		else
			iuvGenerato.setNumeroAvviso(iuv.getAuxDigit() + iuv.getIuv());
		iuvGenerato.setBarCode(buildBarCode(dominio.getGln(), dominio.getAuxDigit(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale, null).getBytes());
		iuvGenerato.setQrCode(buildQrCode002(dominio.getCodDominio(), dominio.getAuxDigit(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale, null));

		return iuvGenerato;
	}

	public static String buildCCP(){
		Date today = new Date();
		return SimpleDateFormatUtils.newSimpleDateFormatIuvUtils().format(today);
	}

	public static boolean checkIuvNumerico(String iuv, int auxDigit, int applicationCode) {
		if(iuv.length() == 15 && auxDigit == 0) {
			String reference = iuv.substring(0, 13);
			long resto93 = (Long.parseLong(String.valueOf(auxDigit) + String.format("%02d", applicationCode) + reference)) % 93;
			return iuv.equals(reference + String.format("%02d", resto93));
		} else if(iuv.length() == 17 && auxDigit == 3) {
			String reference = iuv.substring(0, 15);
			long resto93 = (Long.parseLong(String.valueOf(auxDigit) + reference)) % 93;
			return iuv.equals(reference + String.format("%02d", resto93));
		} else {
			return false;
		}
	}

	public static Iuv toIuv(Versamento versamento, Applicazione applicazione, Dominio dominio) {
		return toIuv(versamento, applicazione, dominio, true);
	}
	
	public static Iuv toIuvFromNumeroAvviso(Versamento versamento, Applicazione applicazione, Dominio dominio) {
		return toIuv(versamento, applicazione, dominio, false);
	}

	private static Iuv toIuv(Versamento versamento, Applicazione applicazione, Dominio dominio, boolean generaNumeroAvviso) {
		Iuv iuvGenerato = new Iuv();
		iuvGenerato.setCodApplicazione(applicazione.getCodApplicazione());
		iuvGenerato.setCodDominio(dominio.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		iuvGenerato.setIuv(versamento.getIuvVersamento());
		
		String numeroAvviso = null;
		
		if(!generaNumeroAvviso) {
			numeroAvviso = versamento.getNumeroAvviso();
			iuvGenerato.setNumeroAvviso(numeroAvviso);
		} else {
			iuvGenerato.setNumeroAvviso(toNumeroAvviso(versamento.getIuvVersamento(), dominio));
		}
		iuvGenerato.setBarCode(buildBarCode(dominio.getGln(), dominio.getAuxDigit(), dominio.getStazione().getApplicationCode(), versamento.getIuvVersamento(), versamento.getImportoTotale(), numeroAvviso).getBytes());
		iuvGenerato.setQrCode(buildQrCode002(dominio.getCodDominio(), dominio.getAuxDigit(), dominio.getStazione().getApplicationCode(), versamento.getIuvVersamento(), versamento.getImportoTotale(), numeroAvviso));

		return iuvGenerato;
	}
	
	public static String toNumeroAvviso(String iuv, Dominio dominio) { 
		String iuvGenerato = dominio.getAuxDigit() + iuv;
		if(dominio.getAuxDigit() == 0) {
			iuvGenerato = dominio.getAuxDigit() + String.format("%02d", dominio.getStazione().getApplicationCode()) + iuv;
		}
		
		return iuvGenerato;
	}

	public static String toIuv(String numeroAvviso) throws ValidationException {
		if(numeroAvviso == null)
			return null;

		if(numeroAvviso.length() != 18)
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: Consentite 18 cifre trovate ["+numeroAvviso.length()+"].");

		try {
			Long.parseLong(numeroAvviso);
		}catch(Exception e) {
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: non e' in formato numerico.");
		}

		if(numeroAvviso.startsWith("0")) // '0' + applicationCode(2) + ref(13) + check(2)
			return numeroAvviso.substring(3);
		else if(numeroAvviso.startsWith("1")) // '1' + reference(17)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("2")) // '2' + ref(15) + check(2)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("3")) // '3' + segregationCode(2) +  ref(13) + check(2) 
			return numeroAvviso.substring(1);
		else 
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: prima cifra non e' [0|1|2|3]");
	}
	
	public static boolean isIuvInterno(Logger log, Dominio dominio, String iuv) {

		if(dominio == null) {
			LogUtils.logDebug(log, "Dominio non censito, IUV:{} non interno", iuv);
			// Se il dominio non e' censito, allora sicuramente non e' interno
			return false;
		}

		boolean isNumerico;

		try {
			new BigInteger(iuv);
			isNumerico = true;
		} catch (Exception e) {
			isNumerico = false;
		}
		
		LogUtils.logDebug(log, "Dominio:{}, AuxDigit:{}, Codice segregazione:{}", dominio.getCodDominio(), dominio.getAuxDigit(), dominio.getSegregationCode());
		LogUtils.logDebug(log, "IUV:{}, lunghezza:{} di tipo numerico: {}", iuv, iuv.length(), (isNumerico ? "SI" : "NO"));

		// AuxDigit 0: Ente monointermediato. 
		// Per i pagamenti di tipo 1 e 2, se non ho trovato il pagamento e sono arrivato qui, posso assumere che non e' interno.
		// Per i pagamenti di tipo 3, e' mio se e' di 15 cifre.
		// Quindi controllo solo se e' numerico e di 15 cifre.
		if(dominio.getAuxDigit() == 0 && isNumerico && iuv.length() == 15) {
			LogUtils.logDebug(log, "AuxDigit 0 -> EC Monointermediato, iuv numerico di lunghezza 15: e' interno.");
			return true;
		}
		
		// AuxDigit 1: Ente monointermediato. 
		// Per i pagamenti di tipo 1 e 2, se non ho trovato il pagamento e sono arrivato qui, posso assumere che non e' interno.
		// Per i pagamenti di tipo 3, e' mio se e' di 17 cifre.
		// Quindi controllo solo se e' numerico e di 17 cifre.
		if(dominio.getAuxDigit() == 1 && isNumerico && iuv.length() == 17) {
			LogUtils.logDebug(log, "AuxDigit 1 -> EC Monointermediato, iuv numerico di lunghezza 17: e' interno.");
			return true;
		}

		if(dominio.getAuxDigit() == 3) {
			// AuxDigit 3: Ente plurintermediato.
			// 
			// Gli IUV generati da GovPay sono nelle forme:
			// RF <check digit (2n)><codice segregazione (2n)><codice alfanumerico (max 19)>
			// <codice segregazione (2n)><IUV base (max 13n)><IUV check digit (2n)>

			// Pagamenti tipo 1 e 2 operati da GovPay
			if(iuv.startsWith("RF") && iuv.substring(4, 6).equals(String.format("%02d", dominio.getSegregationCode()))) {
				LogUtils.logDebug(log, "AuxDigit 3 -> EC Plurintermediato, iuv non numerico contenente il codice di segregazione: e' interno.");
				return true;
			}

			// Pagamenti tipo 3
			if(isNumerico && iuv.length() == 17 && iuv.startsWith(String.format("%02d", dominio.getSegregationCode()))) {
				LogUtils.logDebug(log, "AuxDigit 3 -> EC Plurintermediato, iuv numerico di lunghezza 17, inizia con il codice di segregazione: e' interno.");
				return true;
			}
		}
		
		LogUtils.logDebug(log, "IUV {} non interno.", iuv);
		return false;
	}
	
	public static boolean isNavSondaPagoPA(String numeroAvviso) {
        return GovpayConfig.getInstance().getNavSondaPagoPA().contains(numeroAvviso);
	}
}
