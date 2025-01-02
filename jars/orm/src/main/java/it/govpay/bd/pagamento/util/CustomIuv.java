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
package it.govpay.bd.pagamento.util;

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import it.govpay.bd.model.Applicazione;
import it.govpay.model.Dominio;

public class CustomIuv {

//	public final String getCodApplicazione(Dominio dominio, String iuv, Applicazione applicazioneDefault) throws ServiceException {
//		try {
//			return getCodApplicazione(dominio.getCodDominio(), iuv);
//		} catch (NotImplementedException e){
//			return applicazioneDefault != null ? applicazioneDefault.getCodApplicazione() : null;
//		}
//	}
	public String buildPrefix(Applicazione applicazione, Dominio dominio, Map<String, String> values) {
		return buildPrefix(applicazione, dominio.getIuvPrefix(), values);
	}

	public String buildPrefix(Applicazione applicazione, String prefix, Map<String, String> values) {
		if(prefix == null) return "";
		
		StringSubstitutor sub = new StringSubstitutor(values, "%(", ")");
		String result = sub.replace(prefix);

		return result;
	}

	public boolean isNumericOnly(Applicazione applicazione, it.govpay.bd.model.Dominio dominio, Map<String, String> allIuvProps) {
		return false;
	}
}
