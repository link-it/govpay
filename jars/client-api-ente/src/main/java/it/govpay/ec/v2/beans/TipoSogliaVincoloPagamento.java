/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
 * Indica se il pagamento deve avvenire entro o oltre la soglia di giorni indicata
 */
public enum TipoSogliaVincoloPagamento {
  ENTRO("ENTRO"),
  
  OLTRE("OLTRE"), 
  
  SCONTATO ("SCONTATO"), 
  
  RIDOTTO ("RIDOTTO");

  private String value;

  TipoSogliaVincoloPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoSogliaVincoloPagamento fromValue(String text) {
    for (TipoSogliaVincoloPagamento b : TipoSogliaVincoloPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
