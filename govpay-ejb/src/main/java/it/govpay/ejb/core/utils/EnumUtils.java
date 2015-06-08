/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.utils;

import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.core.model.PagamentoModel.EnumStatoPagamento;

import java.lang.reflect.Method;

public class EnumUtils {

	public static <T extends Enum<?> & EnumChiaveDescrizione> T findByChiave(String chiave, Class<T> e) {
		return findByMethod(chiave, e, "getChiave");
	}
	public static <T extends Enum<?> & EnumChiaveDescrizione> T findByDescrizione(String descrizione, Class<T> e) {
		return findByMethod(descrizione, e, "getDescrizione");
	}

	public static <T extends Enum<?>> T findByMethod(Object value, Class<T> e, String methodName) {
		for (T v : e.getEnumConstants()) {
			try {
				Method method = e.getMethod(methodName);
				if (method.invoke(v).equals(value)) {
					return v;
				}
			} catch (Exception exc) {
				throw new IllegalArgumentException(value.toString(), exc);
			}
		}
		return null;
	}
	
	/**
	 * Propaga lo stato del pagamento sui pagamenti della distinta in modo congruente
	 * @param stDist
	 * @return
	 */
	public static EnumStatoPagamento mapStatoDistintaToStatoPagamento(EnumStatoDistinta stDist) {

		if (stDist.equals(EnumStatoDistinta.PARZIALMENTE_ESEGUITO)) 
			throw new IllegalArgumentException("Impossibile propagare lo stato  della distinta sul singolo pagamento se lo stato distinta è: "+EnumStatoDistinta.PARZIALMENTE_ESEGUITO);


		switch (stDist) {

		case IN_CORSO:
			return EnumStatoPagamento.IC;

		case IN_ERRORE:
			return EnumStatoPagamento.IE;

		case ESEGUITO:
			return EnumStatoPagamento.ES;

		case ESEGUITO_SBF:
			return EnumStatoPagamento.EF;

		case NON_ESEGUITO:
			return EnumStatoPagamento.NE;

		case STORNATO:
			return EnumStatoPagamento.ST;

		case ANNULLATO:
			return EnumStatoPagamento.AN;

		case ANNULLATO_OPE:
			return EnumStatoPagamento.AO;

		default:
			return null;

		}

	}
	
	
}
