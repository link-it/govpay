package it.govpay.ec.v2.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VincoloDocumento  implements OneOfTipoRiferimentoDocumento  {
  
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

  public VincoloDocumento soglia(VincoloPagamento soglia) {
    this.soglia = soglia;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VincoloDocumento {\n");
    
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
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
