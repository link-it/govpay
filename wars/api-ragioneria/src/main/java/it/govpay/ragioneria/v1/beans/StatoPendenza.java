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
package it.govpay.ragioneria.v1.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Stato della pendenza:   * ESEGUITA: Pagata  * NON_ESEGUITA: Da pagare  * ESEGUITA_PARZIALE: Pagata parzialmente  * ANNULLATA: Annullata  * SCADUTA: Scaduta
 */
public enum StatoPendenza {




  ESEGUITA("ESEGUITA"),


  NON_ESEGUITA("NON_ESEGUITA"),


  ESEGUITA_PARZIALE("ESEGUITA_PARZIALE"),


  ANNULLATA("ANNULLATA"),


  SCADUTA("SCADUTA");




  private String value;

  StatoPendenza(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  @JsonCreator
  public static StatoPendenza fromValue(String text) {
    for (StatoPendenza b : StatoPendenza.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



