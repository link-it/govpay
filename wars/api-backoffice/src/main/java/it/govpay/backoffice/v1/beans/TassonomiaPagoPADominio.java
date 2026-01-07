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
package it.govpay.backoffice.v1.beans;


/**
 * Classificazione ente della tassonomia pagoPA  * 01: COMUNE / UNIONE DI COMUNI / CONSORZI  * 02: PROVINCIA / CITTA&#x27; METROPOLITANA  * 03: REGIONE  * 04: ORDINI PROFESSIONALI  * 05: SERVIZIO SANITARIO NAZIONALE  * 06: UNIVERSITÀ / SCUOLA STATALE  * 07: PUBBLICHE AMMINISTRAZIONI CENTRALI  * 08: ALTRE AMMINISTRAZIONI  * 09: GESTORI PUBBLICI SERVIZI  * 10: AUTORITA&#x27; AMMINISTRATIVE INDIPENDENTI  * 11: ENTI DI NATURA PRIVATISTICA  * 12: AGENZIE FISCALI
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Classificazione ente della tassonomia pagoPA  * 01: COMUNE / UNIONE DI COMUNI / CONSORZI  * 02: PROVINCIA / CITTA' METROPOLITANA  * 03: REGIONE  * 04: ORDINI PROFESSIONALI  * 05: SERVIZIO SANITARIO NAZIONALE  * 06: UNIVERSITÀ / SCUOLA STATALE  * 07: PUBBLICHE AMMINISTRAZIONI CENTRALI  * 08: ALTRE AMMINISTRAZIONI  * 09: GESTORI PUBBLICI SERVIZI  * 10: AUTORITA' AMMINISTRATIVE INDIPENDENTI  * 11: ENTI DI NATURA PRIVATISTICA  * 12: AGENZIE FISCALI
 */
public enum TassonomiaPagoPADominio {




  _01("01"),


  _02("02"),


  _03("03"),


  _04("04"),


  _05("05"),


  _06("06"),


  _07("07"),


  _08("08"),


  _09("09"),


  _10("10"),


  _11("11"),


  _12("12");




  private String value;

  TassonomiaPagoPADominio(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TassonomiaPagoPADominio fromValue(String text) {
    for (TassonomiaPagoPADominio b : TassonomiaPagoPADominio.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



