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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Macro categoria della pendenza Agid
 **/


/**
 * Macro categoria della pendenza Agid
 */
public enum TassonomiaAvviso {




  CARTELLE_ESATTORIALI("Cartelle esattoriali"),


  DIRITTI_E_CONCESSIONI("Diritti e concessioni"),


  IMPOSTE_E_TASSE("Imposte e tasse"),


  IMU_TASI_E_ALTRE_TASSE_COMUNALI("IMU, TASI e altre tasse comunali"),


  INGRESSI_A_MOSTRE_E_MUSEI("Ingressi a mostre e musei"),


  MULTE_E_SANZIONI_AMMINISTRATIVE("Multe e sanzioni amministrative"),


  PREVIDENZA_E_INFORTUNI("Previdenza e infortuni"),


  SERVIZI_EROGATI_DAL_COMUNE("Servizi erogati dal comune"),


  SERVIZI_EROGATI_DA_ALTRI_ENTI("Servizi erogati da altri enti"),


  SERVIZI_SCOLASTICI("Servizi scolastici"),


  TASSA_AUTOMOBILISTICA("Tassa automobilistica"),


  TICKET_E_PRESTAZIONI_SANITARIE("Ticket e prestazioni sanitarie"),


  TRASPORTI_MOBILIT_E_PARCHEGGI("Trasporti, mobilità e parcheggi");




  private String value;

  TassonomiaAvviso(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  public static TassonomiaAvviso fromValue(String text) {
    for (TassonomiaAvviso b : TassonomiaAvviso.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



