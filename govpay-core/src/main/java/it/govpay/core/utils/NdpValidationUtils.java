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

import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIstitutoAttestante;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;

public class NdpValidationUtils {
	
	public static String validaSemantica(CtEnteBeneficiario rpt, CtEnteBeneficiario rt) {
		if(!equals(rpt.getDenominazioneBeneficiario(), rt.getDenominazioneBeneficiario())) return "DenominazioneBeneficiario non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco(), rt.getIdentificativoUnivocoBeneficiario().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoBeneficiario non corrisponde";
		if(!equals(rpt.getProvinciaBeneficiario(), rt.getProvinciaBeneficiario())) return "ProvinciaBeneficiario non corrisponde";
		if(!equals(rpt.getNazioneBeneficiario(), rt.getNazioneBeneficiario())) return "NazioneBeneficiario non corrisponde";
		return null;
	}

	public static String validaSemantica(CtDominio rpt, CtDominio rt) {
		if(!equals(rpt.getIdentificativoDominio(),rt.getIdentificativoDominio())) return "IdentificativoDominio non corrisponde";
		if(!equals(rpt.getIdentificativoStazioneRichiedente(),rt.getIdentificativoStazioneRichiedente())) return "IdentificativoStazioneRichiedente non corrisponde";
		return null;
	}

	public static String validaSemantica(CtSoggettoVersante rpt, CtSoggettoVersante rt) {
		if(rpt == null && rt == null) return null;
		if(rpt == null || rt == null) return "SoggettoVersante non corriponde";

		if(!equals(rpt.getAnagraficaVersante(),rt.getAnagraficaVersante())) return "AnagraficaVersante non corrisponde";
		if(!equals(rpt.getCapVersante(),rt.getCapVersante())) return "CapVersante non corrisponde";
		if(!equals(rpt.getCivicoVersante(),rt.getCivicoVersante())) return "CivicoVersante non corrisponde";
		if(!equals(rpt.getEMailVersante(),rt.getEMailVersante())) return "EMailVersante non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoVersante non corrisponde";
		if(!equals(rpt.getIndirizzoVersante(),rt.getIndirizzoVersante())) return "IndirizzoVersante non corrisponde";
		if(!equals(rpt.getLocalitaVersante(),rt.getLocalitaVersante())) return "LocaltaVersante non corrisponde";
		if(!equals(rpt.getNazioneVersante(),rt.getNazioneVersante())) return "NazioneVersante non corrisponde";
		if(!equals(rpt.getProvinciaVersante(),rt.getProvinciaVersante())) return "ProvinciaVersante non corrisponde";
		return null;
	}

	public static String validaSemantica(CtSoggettoPagatore rpt, CtSoggettoPagatore rt) {
		if(!equals(rpt.getAnagraficaPagatore(),rt.getAnagraficaPagatore())) return "AnagraficaPagatore non corrisponde";
		if(!equals(rpt.getCapPagatore(),rt.getCapPagatore())) return "CapPagatore non corrisponde";
		if(!equals(rpt.getCivicoPagatore(),rt.getCivicoPagatore())) return "CivicoPagatore non corrisponde";
		if(!equals(rpt.getEMailPagatore(),rt.getEMailPagatore())) return "EMailPagatore non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoPagatore non corrisponde";
		if(!equals(rpt.getIndirizzoPagatore(),rt.getIndirizzoPagatore())) return "IndirizzoPagatore non corrisponde";
		if(!equals(rpt.getLocalitaPagatore(),rt.getLocalitaPagatore())) return "LocaltaPagatore non corrisponde";
		if(!equals(rpt.getNazionePagatore(),rt.getNazionePagatore())) return "NazionePagatore non corrisponde";
		if(!equals(rpt.getProvinciaPagatore(),rt.getProvinciaPagatore())) return "ProvinciaPagatore non corrisponde";
		return null;
	}
	
	public static String validaSemantica(CtIstitutoAttestante rpt, CtIstitutoAttestante rt) {
		if(!equals(rpt.getDenominazioneAttestante(),rt.getDenominazioneAttestante())) return "DenominazioneAttestante non corrisponde";
		if(!equals(rpt.getDenomUnitOperAttestante(),rt.getDenomUnitOperAttestante())) return "DenomUnitOperAttestante non corrisponde";
		if(!equals(rpt.getCapAttestante(),rt.getCapAttestante())) return "CapAttestante non corrisponde";
		if(!equals(rpt.getCodiceUnitOperAttestante(),rt.getCodiceUnitOperAttestante())) return "CodiceUnitOperAttestante non corrisponde";
		if(!equals(rpt.getCivicoAttestante(),rt.getCivicoAttestante())) return "CivicoAttestante non corrisponde";
		if(!equals(rpt.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco(),rt.getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco())) return "IdentificativoUnivocoAttestante non corrisponde";
		if(!equals(rpt.getIndirizzoAttestante(),rt.getIndirizzoAttestante())) return "IndirizzoAttestante non corrisponde";
		if(!equals(rpt.getLocalitaAttestante(),rt.getLocalitaAttestante())) return "LocaltaAttestante non corrisponde";
		if(!equals(rpt.getNazioneAttestante(),rt.getNazioneAttestante())) return "NazioneAttestante non corrisponde";
		if(!equals(rpt.getProvinciaAttestante(),rt.getProvinciaAttestante())) return "ProvinciaPagatore non corrisponde";
		return null;
	}

	public static boolean equals(String s1, String s2) {
		if(s1==null && s2==null) return true;
		if(s1==null) return false;
		return s1.compareTo(s2)==0;
	}

}
