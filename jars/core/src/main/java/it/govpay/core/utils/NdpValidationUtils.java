/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.core.utils.RtUtils.EsitoValidazione;

public class NdpValidationUtils {
	
	protected NdpValidationUtils() { 
		// static only
	}
	
	public static void validaSemantica(CtEnteBeneficiario rpt, CtEnteBeneficiario rt, EsitoValidazione esito) {
		valida(rpt.getDenominazioneBeneficiario(), rt.getDenominazioneBeneficiario(), esito, "DenominazioneBeneficiario non corrisponde", false);
		valida(rpt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco(), rt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco(), esito, "IdentificativoUnivocoBeneficiario non corrisponde", true);
		valida(rpt.getProvinciaBeneficiario(), rt.getProvinciaBeneficiario(), esito, "ProvinciaBeneficiario non corrisponde", false);
		valida(rpt.getNazioneBeneficiario(), rt.getNazioneBeneficiario(), esito, "NazioneBeneficiario non corrisponde", false);
	}

	public static void validaSemantica(CtDominio rpt, CtDominio rt, EsitoValidazione esito) {
		valida(rpt.getIdentificativoDominio(),rt.getIdentificativoDominio(), esito, "IdentificativoDominio non corrisponde", true);
		valida(rpt.getIdentificativoStazioneRichiedente(),rt.getIdentificativoStazioneRichiedente(), esito, "IdentificativoStazioneRichiedente non corrisponde", false);
	}

	public static void validaSemantica(CtSoggettoVersante rpt, CtSoggettoVersante rt, EsitoValidazione esito) {
		if(rpt == null && rt == null) return;
		if(rpt == null || rt == null) { esito.addErrore("SoggettoVersante non corrisponde", false);  return; }

		valida(rpt.getAnagraficaVersante(),rt.getAnagraficaVersante(), esito, "AnagraficaVersante non corrisponde", false);
		valida(rpt.getCapVersante(),rt.getCapVersante(), esito, "CapVersante non corrisponde", false);
		valida(rpt.getCivicoVersante(),rt.getCivicoVersante(), esito, "CivicoVersante non corrisponde", false);
		valida(rpt.getEMailVersante(),rt.getEMailVersante(), esito, "EMailVersante non corrisponde", false);
		valida(rpt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco(), esito, "IdentificativoUnivocoVersante non corrisponde", true);
		valida(rpt.getIndirizzoVersante(),rt.getIndirizzoVersante(), esito, "IndirizzoVersante non corrisponde", false);
		valida(rpt.getLocalitaVersante(),rt.getLocalitaVersante(), esito, "LocaltaVersante non corrisponde", false);
		valida(rpt.getNazioneVersante(),rt.getNazioneVersante(), esito, "NazioneVersante non corrisponde", false);
		valida(rpt.getProvinciaVersante(),rt.getProvinciaVersante(), esito, "ProvinciaVersante non corrisponde", false);
	}

	public static void validaSemantica(CtSoggettoPagatore rpt, CtSoggettoPagatore rt, EsitoValidazione esito) {
		valida(rpt.getAnagraficaPagatore(),rt.getAnagraficaPagatore(), esito, "AnagraficaPagatore non corrisponde", false);
		valida(rpt.getCapPagatore(),rt.getCapPagatore(), esito, "CapPagatore non corrisponde", false);
		valida(rpt.getCivicoPagatore(),rt.getCivicoPagatore(), esito, "CivicoPagatore non corrisponde", false);
		valida(rpt.getEMailPagatore(),rt.getEMailPagatore(), esito, "EMailPagatore non corrisponde", false);
		valida(rpt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco(), esito, "IdentificativoUnivocoPagatore non corrisponde", true);
		valida(rpt.getIndirizzoPagatore(),rt.getIndirizzoPagatore(), esito, "IndirizzoPagatore non corrisponde", false);
		valida(rpt.getLocalitaPagatore(),rt.getLocalitaPagatore(), esito, "LocaltaPagatore non corrisponde", false);
		valida(rpt.getNazionePagatore(),rt.getNazionePagatore(), esito, "NazionePagatore non corrisponde", false);
		valida(rpt.getProvinciaPagatore(),rt.getProvinciaPagatore(), esito, "ProvinciaPagatore non corrisponde", false);
	}
	
	public static void validaSemantica(CtIstitutoAttestante rpt, CtIstitutoAttestante rt, EsitoValidazione esito) {
		valida(rpt.getDenominazioneAttestante(),rt.getDenominazioneAttestante(), esito, "DenominazioneAttestante non corrisponde", false);
		valida(rpt.getDenomUnitOperAttestante(),rt.getDenomUnitOperAttestante(), esito, "DenomUnitOperAttestante non corrisponde", false);
		valida(rpt.getCapAttestante(),rt.getCapAttestante(), esito, "CapAttestante non corrisponde", false);
		valida(rpt.getCodiceUnitOperAttestante(),rt.getCodiceUnitOperAttestante(), esito, "CodiceUnitOperAttestante non corrisponde", false);
		valida(rpt.getCivicoAttestante(),rt.getCivicoAttestante(), esito, "CivicoAttestante non corrisponde", false);
		valida(rpt.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco(), esito, "IdentificativoUnivocoAttestante non corrisponde", false);
		valida(rpt.getIndirizzoAttestante(),rt.getIndirizzoAttestante(), esito, "IndirizzoAttestante non corrisponde", false);
		valida(rpt.getLocalitaAttestante(),rt.getLocalitaAttestante(), esito, "LocaltaAttestante non corrisponde", false);
		valida(rpt.getNazioneAttestante(),rt.getNazioneAttestante(), esito, "NazioneAttestante non corrisponde", false);
		valida(rpt.getProvinciaAttestante(),rt.getProvinciaAttestante(), esito, "ProvinciaAttestante non corrisponde", false);
	}
	
	public static void valida(String s1, String s2, EsitoValidazione esito, String errore, boolean fatal) {
		if(!equals(s1,s2)) esito.addErrore(errore + " [Atteso:\"" + (s1 != null ? s1 : "<null>") + "\" Ricevuto:\"" + (s2 != null ? s2 : "<null>") + "\"]", fatal);
	}
	
	private static boolean equals(String s1, String s2) {
		if(s1==null && s2==null) return true;
		if(s1==null || s2==null) return false;
		return s1.equals(s2);
	}

}
