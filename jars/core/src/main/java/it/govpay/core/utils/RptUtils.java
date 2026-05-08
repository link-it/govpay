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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.model.Anagrafica;

public class RptUtils {
	
	private RptUtils () { /* Static Only */ }

	public static CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo) {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		Anagrafica anagrafica = dominio.getAnagrafica();
		enteBeneficiario.setCapBeneficiario(RptUtils.getNotEmpty(anagrafica.getCap()));
		enteBeneficiario.setCivicoBeneficiario(RptUtils.getNotEmpty(anagrafica.getCivico()));
		enteBeneficiario.setIndirizzoBeneficiario(RptUtils.getNotEmpty(anagrafica.getIndirizzo()));
		enteBeneficiario.setLocalitaBeneficiario(RptUtils.getNotEmpty(anagrafica.getLocalita()));
		enteBeneficiario.setNazioneBeneficiario(RptUtils.getNotEmpty(anagrafica.getNazione()));
		enteBeneficiario.setProvinciaBeneficiario(RptUtils.getNotEmpty(anagrafica.getProvincia()));

		Anagrafica anagraficaUo = uo.getAnagrafica();
		if(!uo.getCodUo().equals(it.govpay.model.Dominio.EC) && anagraficaUo != null) {
			enteBeneficiario.setCodiceUnitOperBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getCodUnivoco()));
			enteBeneficiario.setDenomUnitOperBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getRagioneSociale()));
			enteBeneficiario.setIndirizzoBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getIndirizzo()));
			enteBeneficiario.setCivicoBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getCivico()));
			enteBeneficiario.setCapBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getCap()));
			enteBeneficiario.setLocalitaBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getLocalita()));
			enteBeneficiario.setProvinciaBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getProvincia()));
			enteBeneficiario.setNazioneBeneficiario(RptUtils.getNotEmpty(anagraficaUo.getNazione()));
		}
		return enteBeneficiario;
	}

	public static String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	public static String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale, String descrizione, String descrizioneCausaleRPT) {
		StringBuilder sb = new StringBuilder();
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		if(IuvUtils.checkISO11640(iuv)) {
			sb.append("/RFS/");
			// Issue #366. Formato causale RFS prevede uno spazio ogni 4 cifre dello IUV
			sb.append(formattaCausaleRFS(iuv).trim());
		}else { 
			sb.append("/RFB/");
			sb.append(iuv);
		}
		
		sb.append("/");
		sb.append(nFormatter.format(importoTotale));
		if(StringUtils.isNotEmpty(descrizioneCausaleRPT)) {
			sb.append("/TXT/").append(descrizioneCausaleRPT);
		} else {
			if(StringUtils.isNotEmpty(descrizione)) {
				sb.append("/TXT/").append(descrizione);
			}
		}
		
		if(sb.toString().length() > 140)
			return sb.toString().substring(0, 140);
		
		return sb.toString();
	}

	public static String formattaCausaleRFS(String iuv) {
		return iuv.replaceAll("(.{4})", "$1 ");
	}
}
