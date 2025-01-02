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
package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tipologia della voce riscossa  * ENTRATA: Entrata in tesoreria  * MBT: Marca da bollo telematica  * ALTRO_INTERMEDIARIO: Entrata relativa a un intermediario esterno  * ENTRATA_PA_NON_INTERMEDIATA: Entrata relativa ad un ente creditore non intermediato da GovPay 
 */
public enum TipoRiscossione {
  ENTRATA("ENTRATA"),
  
  MBT("MBT"),
  
  ALTRO_INTERMEDIARIO("ALTRO_INTERMEDIARIO"),
  
  ENTRATA_PA_NON_INTERMEDIATA("ENTRATA_PA_NON_INTERMEDIATA");

  private String value;

  TipoRiscossione(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoRiscossione fromValue(String text) {
    for (TipoRiscossione b : TipoRiscossione.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
