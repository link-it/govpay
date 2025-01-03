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
package it.govpay.ec.v1.beans;

import javax.validation.Valid;
//import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
 **/
// @Schema(description="Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.")
public class DatiEntrataCodice  {
  
  // @Schema(requiredMode = RequiredMode.REQUIRED, description = "")
  private String codEntrata = null;
 /**
   * Get codEntrata
   * @return codEntrata
  **/
  @JsonProperty("codEntrata")
  @NotNull
  @Valid
  public String getCodEntrata() {
    return codEntrata;
  }

  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public DatiEntrataCodice codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiEntrataCodice {\n");
    
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
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
