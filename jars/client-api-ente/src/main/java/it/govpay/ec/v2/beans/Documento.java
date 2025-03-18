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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class Documento extends TipoRiferimentoDocumento  {
  
  @Schema(example = "abcdef12345_1", requiredMode = RequiredMode.REQUIRED, description = "Identificativo del documento")
 /**
   * Identificativo del documento  
  **/
  private String identificativo = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", requiredMode = RequiredMode.REQUIRED, description = "descrizione del documento")
 /**
   * descrizione del documento  
  **/
  private String descrizione = null;
 /**
   * Identificativo del documento
   * @return identificativo
  **/
  @JsonProperty("identificativo")
  @NotNull
 @Size(min=1,max=35)  public String getIdentificativo() {
    return identificativo;
  }

  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  public Documento identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

 /**
   * descrizione del documento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
 @Size(min=1,max=80)  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Documento descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Documento {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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
