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
package it.govpay.backoffice.v1.beans;


/**
 * Modulo interno che ha emesso l&#x27;evento
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Modulo interno che ha emesso l'evento
 */
public enum ComponenteEvento {




  BACKOFFICE("API_BACKOFFICE"),


  ENTE("API_ENTE"),


  PAGOPA("API_PAGOPA"),


  PAGAMENTO("API_PAGAMENTO"),


  PENDENZE("API_PENDENZE"),


  RAGIONERIA("API_RAGIONERIA"),


  BACKEND_IO("API_BACKEND_IO"),


  SECIM("API_SECIM"),


  MYPIVOT("API_MYPIVOT"),


  MAGGIOLI_JPPA("API_MAGGIOLI_JPPA"),


  GOVPAY("API_GOVPAY"),


  HYPERSIC_APK("API_HYPERSIC_APK"),


  USER("API_USER"),


  GOVPAY_INTERNO("GOVPAY");




  private String value;

  ComponenteEvento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ComponenteEvento fromValue(String text) {
    for (ComponenteEvento b : ComponenteEvento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



