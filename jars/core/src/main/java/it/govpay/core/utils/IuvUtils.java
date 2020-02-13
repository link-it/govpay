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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.business.model.Iuv;

public class IuvUtils {

	private static byte[] buildQrCode002(String codDominio, int auxDigit, int applicationCode, String iuv, BigDecimal importoTotale) {
		// Da "L’Avviso di pagamento analogico nel sistema pagoPA" par. 2.1
		String qrCode = null; 
		if(auxDigit == 0)
			qrCode = "PAGOPA|002|0" + String.format("%02d", applicationCode) + iuv + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));
		else 
			qrCode = "PAGOPA|002|" + auxDigit + iuv + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));

		return qrCode.getBytes();
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("00.00", new DecimalFormatSymbols(Locale.ENGLISH));

	private static String buildBarCode(String gln, int auxDigit, int applicationCode, String iuv, BigDecimal importoTotale) {
		// Da Guida Tecnica di Adesione PA 3.8 pag 25 
		String payToLoc = "415";
		String refNo = "8020";
		String amount = "3902";
		String importo = nFormatter.format(importoTotale).replace(".", "");

		if(auxDigit == 3)
			return payToLoc + gln + refNo + "3" + iuv + amount + importo;
		else 
			return payToLoc + gln + refNo + "0" + String.format("%02d", applicationCode) + iuv + amount + importo;
	}

	public static Iuv toIuv(Applicazione applicazione, Dominio dominio, it.govpay.model.Iuv iuv, BigDecimal importoTotale) throws ServiceException {
		Iuv iuvGenerato = new Iuv();
		iuvGenerato.setCodApplicazione(applicazione.getCodApplicazione());
		iuvGenerato.setCodDominio(dominio.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuv.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuv.getIuv());
		if(iuv.getAuxDigit() == 0)
			iuvGenerato.setNumeroAvviso(iuv.getAuxDigit() + String.format("%02d", iuv.getApplicationCode()) + iuv.getIuv());
		else
			iuvGenerato.setNumeroAvviso(iuv.getAuxDigit() + iuv.getIuv());
		iuvGenerato.setBarCode(buildBarCode(dominio.getGln(), dominio.getAuxDigit(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale).getBytes());
		iuvGenerato.setQrCode(buildQrCode002(dominio.getCodDominio(), dominio.getAuxDigit(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale));

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

	public static Iuv toIuv(Versamento versamento, Applicazione applicazione, Dominio dominio) throws ServiceException {
		Iuv iuvGenerato = new Iuv();
		iuvGenerato.setCodApplicazione(applicazione.getCodApplicazione());
		iuvGenerato.setCodDominio(dominio.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		iuvGenerato.setIuv(versamento.getIuvVersamento());

		if(dominio.getAuxDigit() == 0)
			iuvGenerato.setNumeroAvviso(dominio.getAuxDigit() + String.format("%02d", dominio.getStazione().getApplicationCode()) + versamento.getIuvVersamento());
		else
			iuvGenerato.setNumeroAvviso(dominio.getAuxDigit() + versamento.getIuvVersamento());
		iuvGenerato.setBarCode(buildBarCode(dominio.getGln(), dominio.getAuxDigit(), dominio.getStazione().getApplicationCode(), versamento.getIuvVersamento(), versamento.getImportoTotale()).getBytes());
		iuvGenerato.setQrCode(buildQrCode002(dominio.getCodDominio(), dominio.getAuxDigit(), dominio.getStazione().getApplicationCode(), versamento.getIuvVersamento(), versamento.getImportoTotale()));

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
}
