package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class VocePendenzaPagata extends VocePendenza  {
  
  @Schema(required = true, description = "")
  private RiscossioneVocePagata riscossione = null;
 /**
   * Get riscossione
   * @return riscossione
  **/
  @JsonProperty("riscossione")
  @NotNull
  public RiscossioneVocePagata getRiscossione() {
    return riscossione;
  }

  public void setRiscossione(RiscossioneVocePagata riscossione) {
    this.riscossione = riscossione;
  }

  public VocePendenzaPagata riscossione(RiscossioneVocePagata riscossione) {
    this.riscossione = riscossione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenzaPagata {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    riscossione: ").append(toIndentedString(riscossione)).append("\n");
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
