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
package it.govpay.pagamento.v3.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets TipoServizio
 */
public enum TipoServizio {
  ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
  
  ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
  
  ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
  
  ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
  
  PAGAMENTI("Pagamenti"),
  
  PENDENZE("Pendenze"),
  
  RENDICONTAZIONI_E_INCASSI("Rendicontazioni e Incassi"),
  
  GIORNALE_DEGLI_EVENTI("Giornale degli Eventi"),
  
  CONFIGURAZIONE_E_MANUTENZIONE("Configurazione e manutenzione");

  private String value;

  TipoServizio(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoServizio fromValue(String text) {
    for (TipoServizio b : TipoServizio.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
  
}
