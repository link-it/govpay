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
package it.govpay.ragioneria.v3.beans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Definisce i dettagli di una entrata.
 **/
@Schema(description="Definisce i dettagli di una entrata.")
public class Entrata  implements OneOfTipoRiferimentoVocePendenza  {

  @Schema(example = "IT60X0542811101000000123456", required = true, description = "")
  private String ibanAccredito = null;

  @Schema(example = "IT60X0542811101000000123456", description = "")
  private String ibanAppoggio = null;

  @Schema(example = "9/3321", required = true, description = "Tassonomia pagoPA")
 /**
   * Tassonomia pagoPA
  **/
  private String codiceTassonomicoPagoPA = null;
 /**
   * Get ibanAccredito
   * @return ibanAccredito
  **/
  @JsonProperty("ibanAccredito")
  @NotNull
 @Pattern(regexp="[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")  public String getIbanAccredito() {
    return ibanAccredito;
  }

  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public Entrata ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

 /**
   * Get ibanAppoggio
   * @return ibanAppoggio
  **/
  @JsonProperty("ibanAppoggio")
 @Pattern(regexp="[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")  public String getIbanAppoggio() {
    return ibanAppoggio;
  }

  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  public Entrata ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

 /**
   * Tassonomia pagoPA
   * @return codiceTassonomicoPagoPA
  **/
  @JsonProperty("codiceTassonomicoPagoPA")
  @NotNull
 @Size(min=1,max=140)  public String getCodiceTassonomicoPagoPA() {
    return codiceTassonomicoPagoPA;
  }

  public void setCodiceTassonomicoPagoPA(String codiceTassonomicoPagoPA) {
    this.codiceTassonomicoPagoPA = codiceTassonomicoPagoPA;
  }

  public Entrata codiceTassonomicoPagoPA(String codiceTassonomicoPagoPA) {
    this.codiceTassonomicoPagoPA = codiceTassonomicoPagoPA;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entrata {\n");

    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    codiceTassonomicoPagoPA: ").append(toIndentedString(codiceTassonomicoPagoPA)).append("\n");
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
