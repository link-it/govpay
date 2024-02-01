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
package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VincoloPagamento   {

  @Schema(example = "5", required = true, description = "numero di giorni vincolo per il pagamento")
 /**
   * numero di giorni vincolo per il pagamento
  **/
  private BigDecimal giorni = null;

  @Schema(required = true, description = "")
  private TipoSogliaVincoloPagamento tipo = null;
 /**
   * numero di giorni vincolo per il pagamento
   * minimum: 1
   * @return giorni
  **/
  @JsonProperty("giorni")
  @NotNull
 @DecimalMin("1")  public BigDecimal getGiorni() {
    return giorni;
  }

  public void setGiorni(BigDecimal giorni) {
    this.giorni = giorni;
  }

  public VincoloPagamento giorni(BigDecimal giorni) {
    this.giorni = giorni;
    return this;
  }

 /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public TipoSogliaVincoloPagamento getTipo() {
    return tipo;
  }

  public void setTipo(TipoSogliaVincoloPagamento tipo) {
    this.tipo = tipo;
  }

  public VincoloPagamento tipo(TipoSogliaVincoloPagamento tipo) {
    this.tipo = tipo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VincoloPagamento {\n");

    sb.append("    giorni: ").append(toIndentedString(giorni)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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
