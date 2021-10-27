package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class RataDocumento  implements OneOfTipoRiferimentoDocumento  {
  
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

  public RataDocumento rata(BigDecimal rata) {
    this.rata = rata;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RataDocumento {\n");
    
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
