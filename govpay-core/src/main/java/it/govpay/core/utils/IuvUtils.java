/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.CtNumeroAvviso;
import it.gov.spcoop.avvisopagamentopa.informazioniversamentoqr.InformazioniVersamento;
import it.govpay.model.Applicazione;
import it.govpay.model.Dominio;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.IuvGenerato;

public class IuvUtils {

	private static byte[] buildQrCode001(String codDominio, int applicationCode, String iuv, BigDecimal importoTotale) throws JAXBException, SAXException {
		InformazioniVersamento info = new InformazioniVersamento();
		info.setCodiceIdentificativoEnte(codDominio);
		info.setImportoVersamento(importoTotale);
		CtNumeroAvviso numeroAvviso = new CtNumeroAvviso();
		numeroAvviso.setAuxDigit("0");
		numeroAvviso.setApplicationCode(String.format("%02d", applicationCode));
		numeroAvviso.setIUV(iuv);
		info.setNumeroAvviso(numeroAvviso);
		byte[] infoByte = JaxbUtils.toByte(info);
		return infoByte;
	}
	
	private static byte[] buildQrCode002(String codDominio, int applicationCode, String iuv, BigDecimal importoTotale) throws JAXBException, SAXException {
		// Da "L’Avviso di pagamento analogico nel sistema pagoPA" par. 2.1
		String qrCode = "PAGOPA|002|0" + String.format("%02d", applicationCode) + iuv + "|" + codDominio + "|" + (nFormatter.format(importoTotale).replace(".", ""));
		return qrCode.getBytes();
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	
	private static String buildBarCode(String gln, int applicationCode, String iuv, BigDecimal importoTotale) {
		// Da Guida Tecnica di Adesione PA 3.8 pag 25 
		String payToLoc = "415";
		String refNo = "8020";
		String amount = "3902";
		String importo = nFormatter.format(importoTotale).replace(".", "");
		return payToLoc + gln + refNo + "0" + String.format("%02d", applicationCode) + iuv + amount + importo;
	}
	
	public static IuvGenerato toIuvGenerato(Applicazione applicazione, Dominio dominio, it.govpay.model.Iuv iuv, BigDecimal importoTotale, Versione versione) throws ServiceException {
		IuvGenerato iuvGenerato = new IuvGenerato();
		iuvGenerato.setCodApplicazione(applicazione.getCodApplicazione());
		iuvGenerato.setCodDominio(dominio.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuv.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuv.getIuv());
		if(versione.compareTo(Versione.GP_02_03_00) >= 0) {
			iuvGenerato.setNumeroAvviso(iuv.getAuxDigit() + String.format("%02d", iuv.getApplicationCode()) + iuv.getIuv());
		}
		iuvGenerato.setBarCode(buildBarCode(dominio.getGln(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale).getBytes());
		try {
		switch (GovpayConfig.getInstance().getVersioneAvviso()) {
			case v001:
				iuvGenerato.setQrCode(buildQrCode001(dominio.getCodDominio(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale));
				break;
			case v002:
				iuvGenerato.setQrCode(buildQrCode002(dominio.getCodDominio(), iuv.getApplicationCode(), iuv.getIuv(), importoTotale));
				break;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return iuvGenerato;
	}
	
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyyyHHmmSSsss");
	
	public static String buildCCP(){
		 Date today = new Date();
		 return DATE_FORMAT.format(today);
	}

	public static boolean checkIuvNumerico(String iuv, int auxDigit, int applicationCode) {
		if(iuv.length() != 15) return false;
		String reference = iuv.substring(0, 13);
		long resto93 = (Long.parseLong(String.valueOf(auxDigit) + String.format("%02d", applicationCode) + reference)) % 93;
		return iuv.equals(reference + String.format("%02d", resto93));
	}
}
