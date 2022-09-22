/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
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
package it.govpay.pagopa.v2.utils;

import it.govpay.core.exceptions.ValidationException;

public class IuvUtils {
	
	/**
	 * Conversione del numero avviso in iuv e controlli base di validita'
	 * 
	 * @param numeroAvviso
	 * @return
	 * @throws ValidationException
	 */
	public static String toIuv(String numeroAvviso) throws IllegalArgumentException {
		if(numeroAvviso == null)
			return null;

		if(numeroAvviso.length() != 18)
			throw new IllegalArgumentException("Numero Avviso [" + numeroAvviso + "] fornito non valido: Consentite 18 cifre trovate ["+numeroAvviso.length()+"].");

		try {
			Long.parseLong(numeroAvviso);
		}catch(Exception e) {
			throw new IllegalArgumentException("Numero Avviso [" + numeroAvviso + "] fornito non valido: non e' in formato numerico.");
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
			throw new IllegalArgumentException("Numero Avviso [" + numeroAvviso + "] fornito non valido: prima cifra non e' [0|1|2|3]");
	}
}