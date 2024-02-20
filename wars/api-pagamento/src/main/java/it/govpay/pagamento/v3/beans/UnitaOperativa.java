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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class UnitaOperativa   {

  @Schema(example = "1.23456789E+9", description = "Codice fiscale")
 /**
   * Codice fiscale
  **/
  private String idUnita = null;

  @Schema(example = "Ufficio due", required = true, description = "Ragione sociale")
 /**
   * Ragione sociale
  **/
  private String ragioneSociale = null;
 /**
   * Codice fiscale
   * @return idUnita
  **/
  @JsonProperty("idUnita")
 @Size(min=1,max=35)  public String getIdUnita() {
    return idUnita;
  }

  public void setIdUnita(String idUnita) {
    this.idUnita = idUnita;
  }

  public UnitaOperativa idUnita(String idUnita) {
    this.idUnita = idUnita;
    return this;
  }

 /**
   * Ragione sociale
   * @return ragioneSociale
  **/
  @JsonProperty("ragioneSociale")
  @NotNull
 @Size(min=1,max=70)  public String getRagioneSociale() {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public UnitaOperativa ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitaOperativa {\n");

    sb.append("    idUnita: ").append(toIndentedString(idUnita)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
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
