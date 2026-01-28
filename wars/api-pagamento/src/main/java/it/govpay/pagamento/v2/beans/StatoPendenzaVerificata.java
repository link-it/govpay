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
package it.govpay.pagamento.v2.beans;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Stato della pendenza:   * NON_ESEGUITA: Pendenza da pagare  * ANNULLATA: Pendenza annullata  * SCADUTA: Termini per il pagamento decorsi  * DUPLICATA: Pendenza gia&#x27; pagata  * SCONOSCIUTA: Pendenza non trovata
 **/


/**
 * Stato della pendenza:   * NON_ESEGUITA: Pendenza da pagare  * ANNULLATA: Pendenza annullata  * SCADUTA: Termini per il pagamento decorsi  * DUPLICATA: Pendenza gia' pagata  * SCONOSCIUTA: Pendenza non trovata
 */
public enum StatoPendenzaVerificata {




  NON_ESEGUITA("NON_ESEGUITA"),


  ANNULLATA("ANNULLATA"),


  SCADUTA("SCADUTA"),


  DUPLICATA("DUPLICATA"),


  SCONOSCIUTA("SCONOSCIUTA");




  private String value;

  StatoPendenzaVerificata(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  public static StatoPendenzaVerificata fromValue(String text) {
    for (StatoPendenzaVerificata b : StatoPendenzaVerificata.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



