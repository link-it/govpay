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
package it.govpay.pagamento.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Esito della richiesta di pagamento:  * IN_CORSO: Pagamento in corso  * RIFIUTATO: Pagamento rifiutato  * ESEGUITO: Pagamento eseguito  * NON_ESEGUITO: Pagamento non eseguito  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito  * DECORRENZA: Decorrenza termini  * DECORRENZA_PARZIALE: Decorrenza termini parziale
 */
public enum EsitoRpp {
  IN_CORSO("IN_CORSO"),

  RIFIUTATO("RIFIUTATO"),

  ESEGUITO("ESEGUITO"),

  NON_ESEGUITO("NON_ESEGUITO"),

  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),

  DECORRENZA("DECORRENZA"),

  DECORRENZA_PARZIALE("DECORRENZA_PARZIALE");

  private String value;

  EsitoRpp(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EsitoRpp fromValue(String text) {
    for (EsitoRpp b : EsitoRpp.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

	public static EsitoRpp fromRptEsitoPagamento(String esitoRpt) {
		switch (esitoRpt) {
		case "DECORRENZA_TERMINI":
			return DECORRENZA;
		case "DECORRENZA_TERMINI_PARZIALE":
			return DECORRENZA_PARZIALE;
		case "IN_CORSO":
			return IN_CORSO;
		case "PAGAMENTO_ESEGUITO":
			return ESEGUITO;
		case "PAGAMENTO_NON_ESEGUITO":
			return NON_ESEGUITO;
		case "PAGAMENTO_PARZIALMENTE_ESEGUITO":
			return ESEGUITO_PARZIALE;
		case "RIFIUTATO":
			return RIFIUTATO;
		}
		return null;
	}

}
