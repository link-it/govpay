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
 * Stato del pagamento  * IN_CORSO: Pagamento in corso  * ANNULLATO: Pagamento annullato dall&#x27;utente o scaduto per timeout  * FALLITO: Pagamento non perfezionato per errori della piattaforma  * ESEGUITO: Pagamento concluso con successo  * NON_ESEGUITO: Processo concluso senza il perfezionamento del pagamento  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito
 **/


/**
 * Stato del pagamento  * IN_CORSO: Pagamento in corso  * ANNULLATO: Pagamento annullato dall'utente o scaduto per timeout  * FALLITO: Pagamento non perfezionato per errori della piattaforma  * ESEGUITO: Pagamento concluso con successo  * NON_ESEGUITO: Processo concluso senza il perfezionamento del pagamento  * ESEGUITO_PARZIALE: Pagamento parzialmente eseguito
 */
public enum StatoPagamento {




  IN_CORSO("IN_CORSO"),


  ESEGUITO("ESEGUITO"),


  NON_ESEGUITO("NON_ESEGUITO"),


  ESEGUITO_PARZIALE("ESEGUITO_PARZIALE"),


  ANNULLATO("ANNULLATO"),


  FALLITO("FALLITO");




  private String value;

  StatoPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(this.value);
  }

  public static StatoPagamento fromValue(String text) {
    for (StatoPagamento b : StatoPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



