/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.ec.v1.beans;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

public class NotificaAnnullamento  {
  
  // @Schema(example = "ERR_001", description = "Codice Operazione")
 /**
   * Codice Operazione  
  **/
  private String codice = null;
  
  // @Schema(example = "Errore gestione nel PSP", description = "Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento")
 /**
   * Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento  
  **/
  private String motivazione = null;
 /**
   * Codice Operazione
   * @return codice
  **/
  @JsonProperty("codice")
  @Valid
  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public NotificaAnnullamento codice(String codice) {
    this.codice = codice;
    return this;
  }

 /**
   * Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento
   * @return motivazione
  **/
  @JsonProperty("motivazione")
  @Valid
  public String getMotivazione() {
    return motivazione;
  }

  public void setMotivazione(String motivazione) {
    this.motivazione = motivazione;
  }

  public NotificaAnnullamento motivazione(String motivazione) {
    this.motivazione = motivazione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificaAnnullamento {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    motivazione: ").append(toIndentedString(motivazione)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
