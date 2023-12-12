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
package it.govpay.pagamento.v3.beans;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class TipoRiferimentoDocumento  implements OneOfTipoRiferimentoDocumento  {

	@Schema(required = true, description = "")
	  private VincoloPagamento soglia = null;
	 /**
	   * Get soglia
	   * @return soglia
	  **/
	  @JsonProperty("soglia")
	  @NotNull
	  public VincoloPagamento getSoglia() {
	    return soglia;
	  }

	  public void setSoglia(VincoloPagamento soglia) {
	    this.soglia = soglia;
	  }

	  public TipoRiferimentoDocumento soglia(VincoloPagamento soglia) {
	    this.soglia = soglia;
	    return this;
	  }

	  @Schema(example = "1", required = true, description = "Rata del documento")
	  /**
	    * Rata del documento
	   **/
	   private BigDecimal rata = null;
	  /**
	    * Rata del documento
	    * minimum: 1
	    * @return rata
	   **/
	   @JsonProperty("rata")
	   @NotNull
	  @DecimalMin("1")  public BigDecimal getRata() {
	     return rata;
	   }

	   public void setRata(BigDecimal rata) {
	     this.rata = rata;
	   }

	   public TipoRiferimentoDocumento rata(BigDecimal rata) {
	     this.rata = rata;
	     return this;
	   }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoRiferimentoDocumento {\n");

    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
    sb.append("    rata: ").append(toIndentedString(rata)).append("\n");
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
